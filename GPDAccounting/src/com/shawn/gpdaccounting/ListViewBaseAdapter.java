package com.shawn.gpdaccounting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class ListViewBaseAdapter extends BaseAdapter implements OnCheckedChangeListener{
	private ArrayList<Map<String,Object>> list_item;
	private ArrayList<Map<String,Object>> select_list;
	private LayoutInflater m_inflater;
	private int check_num = 0;//是否单选标记
	private OnListItemCallBack checked_change_call_back; 
	public ListViewBaseAdapter(Context context,OnListItemCallBack checkedCallBack,ArrayList<Map<String, Object>> list){
		list_item = list;
		checked_change_call_back = checkedCallBack;
		select_list = new ArrayList<Map<String,Object>>();
		m_inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_item.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public void refreshListData(ArrayList<Map<String, Object>> list){
		list_item = list;
		select_list.clear();
		notifyDataSetChanged();
	}
	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder holder;
		System.out.println("this is position:"+ position);
		if(view == null){
			holder = new ViewHolder();
			view = m_inflater.inflate(R.layout.data_show_item, null);
			holder.select_cb = (CheckBox) view.findViewById(R.id.data_item_select);
			holder.money_txt = (TextView) view.findViewById(R.id.data_item_money);
			holder.name_txt = (TextView) view.findViewById(R.id.data_item_name);
			holder.time_txt = (TextView) view.findViewById(R.id.data_item_time);
			holder.remark_txt = (TextView) view.findViewById(R.id.data_item_remark);
			view.setTag(holder);
		}else
			holder = (ViewHolder) view.getTag();
			Map<String, Object> map = list_item.get(position);
			holder.money_txt.setText(map.get(Config.ITEM_MONEY).toString());
			holder.name_txt.setText(map.get(Config.ITEM_NAME).toString());
			holder.time_txt.setText(map.get(Config.ITEM_TIME).toString());
			holder.remark_txt.setText(map.get(Config.ITEM_REMARK).toString());
			holder.select_cb.setChecked(false);
			holder.select_cb.setOnCheckedChangeListener(this);
			holder.select_cb.setTag(position);
		return view;
	}
	public class ViewHolder{
	public CheckBox select_cb;
	public TextView time_txt,name_txt,money_txt,remark_txt;
	}
	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean checked) {
		if (checked){
			check_num +=1;
			int position = (Integer) arg0.getTag();
			HashMap<String, Object> m = (HashMap<String, Object>) list_item.get(position);
			m.put(Config.ITEM_INDEX,position);
			select_list.add(m);
		}else{
			int position = (Integer) arg0.getTag();
			HashMap<String, Object> m = (HashMap<String, Object>) list_item.get(position);
			m.put(Config.ITEM_INDEX,position);
			select_list.remove(m);
			check_num -=1;
		}
		checked_change_call_back.checkedCallBack(check_num);
	}
	
	public ArrayList<Map<String,Object>> getSelectList(){
		return select_list;
	}
}
