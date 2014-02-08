package com.shawn.gpdaccounting;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.shawn.gpdaccounting.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class Main extends Fragment implements OnClickListener{
	private EditText data_money = null;
	private TextView data_user_name = null;
	private Button data_ok = null;
	private Button change_user = null;
	private Button clock_image =null;
	private ImageView data_image = null;
	private EditText data_more_remark = null;
	private SharedPreferences m_share= null;
	private SharedPreferences.Editor m_editor = null;
	public  String[] DEFAULT_REMARK_SPINNER = {"","","","",""};
	public  int data_key_id = 0;//存入数据库中的关键key
	private final int DATA_IMAGE_CAMERA = 1;
	private final int DATA_IMAGE_PICKED = 2;
	private String picture_path = "";
	private Toast m_toast = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main, null);
		Activity activity = getActivity();
		//初始化数据库
		m_share = activity.getSharedPreferences(Config.SHARED_NAME, 0);
		m_editor = m_share.edit();
		data_key_id = m_share.getInt(Config.SHARED_KEY_ID, 0);
		//确认记录按钮
		data_ok = (Button) view.findViewById(R.id.data_ok_btn);
		//金额输入框
		data_money = (EditText) view.findViewById(R.id.data_input_money);
		//照片记录
		data_image = (ImageView) view.findViewById(R.id.data_image);
		//更多备注
		data_more_remark = (EditText) view.findViewById(R.id.data_more_remark);
		//用户名
		data_user_name = (TextView) view.findViewById(R.id.data_user_name);
		//切换用户按钮
		change_user = (Button) view.findViewById(R.id.data_user_change);
		//图片加密按钮
		clock_image = (Button) view.findViewById(R.id.data_image_clock);
		
		data_user_name.setText(Config.LOGIN_USER);
		
		data_ok.setOnClickListener(this);
		data_image.setOnClickListener(this);
		data_more_remark.setOnClickListener(this);
		change_user.setOnClickListener(this);
		clock_image.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.data_ok_btn){
			saveData();
		}else if(v.getId() == R.id.data_image){
			doPickPhotoAction();
		}else if(v.getId() == R.id.data_user_change){
			m_editor.putBoolean(Config.SHARED_FLAG, false);
			m_editor.commit();
			Intent intent = new Intent(getActivity(),Login.class);
			getActivity().startActivity(intent);
			getActivity().finish();
		}else if(v.getId() == R.id.data_image_clock){
			Intent intent = new Intent(getActivity(),EncryptionImage.class);
			getActivity().startActivity(intent);
		}
	}
	/**
	 *		保存当前记录 
	 */
	@SuppressLint("SimpleDateFormat")
	private void saveData(){
		SimpleDateFormat time_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String time = time_format.format(date).toString();
		String s_money= data_money.getText().toString();
		float money = 0;
		if(!s_money.equals(""))
			money = Integer.parseInt(data_money.getText().toString());
		String remark_txt = data_more_remark.getText().toString();
		  if(SQLData.inertAData(data_key_id,Config.LOGIN_USER, time, money,picture_path,remark_txt) != -1){
			data_money.setText(null);
			data_key_id +=1;
			m_editor.putInt(Config.SHARED_KEY_ID,data_key_id);
			m_editor.commit();
			picture_path = "";
			data_more_remark.setText("");
			InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);     
		    imm.hideSoftInputFromWindow(data_money.getWindowToken(), 0);     
		    if( m_toast == null)
		    	m_toast = Toast.makeText(getActivity().getApplicationContext(),R.string.save_data_success,Toast.LENGTH_SHORT);
		    else 
		    	m_toast.setText(R.string.save_data_success);
		    m_toast.show();
		  }
	}
	
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != -1)
			return;
		if(requestCode == DATA_IMAGE_CAMERA){
			Bitmap bitmap  = (Bitmap) data.getExtras().get("data");
			data_image.setImageBitmap(scaleBitmap(bitmap));
		}else if(requestCode == DATA_IMAGE_PICKED){
			 Bitmap photo;
			try {
				photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
				data_image.setImageBitmap(scaleBitmap(photo));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}  
		}
		String filePath = "";
		  if (data.getData() != null) {
            //根据返回的URI获取对应的SQLite信息
            Cursor cursor = getActivity().getContentResolver().query(data.getData(), new String[]{MediaStore.Images.Media.DATA},
                            null, null, null);
            if (cursor.moveToFirst()) {
                    filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));// 获取绝对路径
            }
            cursor.close();
		  }
		  picture_path = filePath;
	}
	
	private void doPickPhotoAction() {
		Context context = getActivity();
		final Context dialogContext = new ContextThemeWrapper(context,
				R.style.image_dialog);
		String[] choices;
		choices = new String[2];
		choices[0] = getString(R.string.take_photo);  //拍照
		choices[1] = getString(R.string.pick_photo);  //从相册中选择
		final ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
				R.layout.simple_spinner_dropdown_item, choices);
	
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				dialogContext);
		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0:{
							String status=Environment.getExternalStorageState();
							if(status.equals(Environment.MEDIA_MOUNTED)){//判断是否有SD卡
								doTakePhoto();// 用户点击了从照相机获取
							}
							break;
						}
						case 1:
							doPickPhotoFromGallery();// 从相册中去获取
							break;
						}
					}
				});
		AlertDialog dialog = builder.create();
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		dialog.getWindow().setWindowAnimations(R.style.dialog_style);
		dialog.show();
	}
	
	private void doTakePhoto(){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, DATA_IMAGE_CAMERA);
	}
	private Bitmap scaleBitmap(Bitmap bigBitmap){
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		int bmp_width = bigBitmap.getWidth();
		int bmp_height = bigBitmap.getHeight();
		int width = mDisplayMetrics.widthPixels;
		float scale  = (float)width/(bmp_width*2);
		Matrix m = new Matrix();
		m.postScale(scale, scale);
		Bitmap small_bitmap = Bitmap.createBitmap(bigBitmap, 0, 0, bmp_width, bmp_height, m,true);
		bigBitmap.recycle();
		return small_bitmap;
	}
    // 请求Gallery程序  
    protected void doPickPhotoFromGallery() {  
        try {  
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);  
            intent.setType("image/*");  
            startActivityForResult(intent, DATA_IMAGE_PICKED);  
        } catch (ActivityNotFoundException e) {  
            e.getMessage();
        }  
    }
}
