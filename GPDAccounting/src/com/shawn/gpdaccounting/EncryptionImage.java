package com.shawn.gpdaccounting;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore.Images.Thumbnails;
import android.widget.ImageView;

public class EncryptionImage extends Activity{
	private ArrayList<HashMap<String,String>> image_list_path;
	private ContentResolver m_content_resolver = null;
	private final int SHOW_IMAGE_NUM = 12;
	private ImageView[] image_view;
	private int image_index = 1;
	private int max_image_num = 0;//所有图片的数目（开发中发现有好些图片没有在thumbnails中存有缩略图,但在medio的数据库中有该索引）
	private int have_null_num = 0;//在数据库中有索引但是在thumbnalis中没有文件的图片的数目
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.encryption_image);
		image_list_path = new ArrayList<HashMap<String,String>>();
		m_content_resolver = getContentResolver();
		image_view = new ImageView[SHOW_IMAGE_NUM];
		int image_r_id = R.id.grid_image_1;
		for(int i = 0;i<SHOW_IMAGE_NUM;i++){
			image_view[i] = (ImageView) findViewById(image_r_id);
			image_r_id += 1;
		}
		String[] projection = {Thumbnails._ID,Thumbnails.IMAGE_ID,Thumbnails.DATA};
		Cursor cursor = m_content_resolver.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
		if(cursor.moveToFirst()){
			int image_index_id;
			int image_id;
			String image_path;
			int id_col = cursor.getColumnIndex(Thumbnails._ID);
			int image_id_col = cursor.getColumnIndex(Thumbnails.IMAGE_ID);
			int data_col = cursor.getColumnIndex(Thumbnails.DATA);
			do{
				image_index_id = cursor.getInt(id_col);
				image_id = cursor.getInt(image_id_col);
				image_path = cursor.getString(data_col);
				HashMap<String,String> hash = new HashMap<String,String>();
				hash.put(Config.IMAGE_INDEX_ID, image_index_id+"");
				hash.put(Config.IMAGE_ID, image_id+"");
				hash.put(Config.IMAGE_PATH, image_path);
				max_image_num += 1;
				image_list_path.add(hash);
			}while(cursor.moveToNext());
		}
		RefreshImage();
	}
	
	/**
	 * 刷新图片
	 */
	private void RefreshImage(){
		for(int i = 0;i<SHOW_IMAGE_NUM;){
			int index = image_index*SHOW_IMAGE_NUM+i+have_null_num;
			String image_path = image_list_path.get(index).get(Config.IMAGE_PATH);
			Bitmap image = BitmapFactory.decodeFile(image_path);
			if(image != null){
				image_view[i].setImageBitmap(image);
				i++;
			}else if(index < max_image_num)
				have_null_num++;
			else
				return;
		}
	}
}
