package com.shawn.gpdaccounting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DataList extends Fragment implements OnItemLongClickListener,
		OnItemClickListener {
	private ArrayList<Map<String, Object>> list_data;
	private DataListAdapter m_adapter;
	private ListView m_list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.inquiry, null);
		m_list = (ListView) view.findViewById(R.id.inquiry_list);
		setData();
		return view;
	}

	private void setData() {
		SQLiteDatabase db_read = SQLData.getDbRead();
		Cursor cur = db_read.query(Config.DATA_TABLE_NAME, null,
				Config.DATA_USER + " = ?", new String[] { Config.LOGIN_USER },
				null, null, Config.DATA_TIME+" desc");
		list_data = new ArrayList<Map<String, Object>>();
		if (cur.moveToFirst()) {
			for (int i = 0; i < cur.getCount(); i++) {
				cur.moveToPosition(i);
				int id = cur.getInt(0);
				String name = cur.getString(1);
				String time = cur.getString(2);
				float money = cur.getFloat(3);
				String picture_path = cur.getString(4);//"/storage/sdcard0/MTXX/DSC_0473_mh000.jpg";
				String remark = cur.getString(5);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(Config.ITEM_ID, id);
				map.put(Config.ITEM_NAME, name);
				map.put(Config.ITEM_TIME, time);
				map.put(Config.ITEM_MONEY, money);
				map.put(Config.ITEM_IMAGE,picture_path);
				map.put(Config.ITEM_REMARK, remark);
				list_data.add(map);
			}
		}
		cur.close();
		db_read.close();
		m_adapter = new DataListAdapter(getActivity(), list_data);
		m_list.setAdapter(m_adapter);
		m_list.setOnItemLongClickListener(this);
		m_list.setOnItemClickListener(this);
	}

	private Bitmap getSalceBitmapFromPath(String path) {
		Bitmap bigBitmap = BitmapFactory.decodeFile(path);
		Bitmap small_bitmap = null;
		if (bigBitmap != null) {
			int bmp_width = bigBitmap.getWidth();
			int bmp_height = bigBitmap.getHeight();
			float scale = (float) Config.SCREEN_WIDTH / (bmp_width * 2);
			Matrix m = new Matrix();
			m.postScale(scale, scale);
			small_bitmap = Bitmap.createBitmap(bigBitmap, 0, 0, bmp_width,
					bmp_height, m, true);
			bigBitmap.recycle();
			bigBitmap = null;
		}
		return small_bitmap;
	}

	public class DataListAdapter extends BaseAdapter {
		private ArrayList<Map<String, Object>> list_item;
		private LayoutInflater m_inflater;

		public DataListAdapter(Context context,
				ArrayList<Map<String, Object>> list) {
			list_item = list;
			m_inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return list_item.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder holder;
			if (view == null) {
				holder = new ViewHolder();
				view = m_inflater.inflate(R.layout.data_list_item, null);
				holder.money_txt = (TextView) view
						.findViewById(R.id.list_item_title);
				holder.name_txt = (TextView) view
						.findViewById(R.id.list_item_user_name);
				holder.time_txt = (TextView) view
						.findViewById(R.id.list_item_time);
				holder.remark_txt = (TextView) view
						.findViewById(R.id.list_item_remark);
				holder.data_image = (ImageView) view
						.findViewById(R.id.list_item_image);
				holder.data_deleted = (Button) view
						.findViewById(R.id.list_item_deleted);
				view.setTag(holder);
			} else
				holder = (ViewHolder) view.getTag();
			final Map<String, Object> map = list_item.get(position);
			holder.money_txt.setText(map.get(Config.ITEM_MONEY).toString());
			holder.name_txt.setText(map.get(Config.ITEM_NAME).toString());
			holder.time_txt.setText(map.get(Config.ITEM_TIME).toString());
			holder.remark_txt.setText(map.get(Config.ITEM_REMARK).toString());
			String image_path = map.get(Config.ITEM_IMAGE).toString();
			holder.data_image.setVisibility(View.VISIBLE);
			holder.data_deleted.setVisibility(View.GONE);
			holder.data_deleted.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String deleted_string = map.get(Config.ITEM_ID).toString();
					if (SQLData.deletedData(deleted_string) != -1) {
						list_item.remove(map);
						m_adapter.notifyDataSetChanged();
					}
				}
			});
			System.out.println("image_path:"+image_path);
			if (!image_path.equals("")){
				holder.data_image.setTag(image_path);
				new MyImageAsyncTask().execute(holder.data_image);//“Ï≤Ωº”‘ÿÕº∆¨
			}else
				holder.data_image.setVisibility(View.GONE);
			return view;
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view,
			int position, long arg3) {
		ViewHolder holder = null;
		if (null != view) {
			holder = (ViewHolder) view.getTag();
			holder.data_deleted.setVisibility(View.VISIBLE);
		}
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		m_adapter.notifyDataSetChanged();
	}

	public class ViewHolder {
		public TextView time_txt, name_txt, money_txt, remark_txt;
		public ImageView data_image;
		public Button data_deleted;
	}
	
	public class MyImageAsyncTask extends AsyncTask<ImageView, Void,Bitmap>{
		private ImageView m_view = null;
		@Override
		protected Bitmap doInBackground(ImageView... params) {
		Bitmap image = getSalceBitmapFromPath((String) params[0].getTag());
		m_view = params[0];
		return image;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if(m_view != null)
				m_view.setImageBitmap(result);
		}
	}
}
