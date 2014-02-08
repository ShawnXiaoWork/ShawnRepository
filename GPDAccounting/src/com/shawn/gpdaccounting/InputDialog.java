package com.shawn.gpdaccounting;

import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputDialog extends Dialog implements android.view.View.OnClickListener{
	private EditText dialog_time = null;
	private EditText dialog_user_name = null;
	private EditText dialog_money = null;
	private EditText dialog_remark = null;
	private Button dialog_ok = null;
	private String[] set_data = {"","","","",""};
	private int data_index = -1;
	private DialogCallBack call_back = null;
	public InputDialog(Context context, int theme) {
		super(context, theme);
		setContentView(R.layout.update_data_dialog);
		dialog_time = (EditText) findViewById(R.id.dialog_time);
		dialog_user_name = (EditText) findViewById(R.id.dialog_user_name);
		dialog_money = (EditText) findViewById(R.id.dialog_money);
		dialog_remark = (EditText) findViewById(R.id.dialog_remark);
		dialog_ok = (Button) findViewById(R.id.dialog_ok);
		dialog_ok.setOnClickListener(this);
		call_back = (DialogCallBack) context;
	}
	
	public void setData(String id,String index,String time,String user_name,String money,String remark){
		set_data[0] = id;
		set_data[1] = time;
		set_data[2] = user_name;
		set_data[3] = money;
		set_data[4] = remark;
		data_index = Integer.parseInt(index);
		dialog_time.setText(time);
		dialog_user_name.setText(user_name);
		dialog_money.setText(money);
		dialog_remark.setText(remark);
	}

	@Override
	public void onClick(View v) {
		String time = dialog_time.getText().toString();
		String user_name =dialog_user_name.getText().toString();
		String money = dialog_money.getText().toString();
		String remark = dialog_remark.getText().toString();
		if(!time.equals(set_data[1])|| !user_name.equals(set_data[2])|| !money.equals(set_data[3]) || !remark.equals(set_data[4])){
			if(SQLData.updateData(set_data[0], user_name, time, money, remark)!= -1){
				HashMap<String,Object> map = new HashMap<String,Object>();
				map.put(Config.ITEM_ID,set_data[0]);
				map.put(Config.ITEM_NAME,user_name);
				map.put(Config.ITEM_TIME, time);
				map.put(Config.ITEM_MONEY, money);
				map.put(Config.ITEM_REMARK, remark);
				call_back.DialogOk(data_index, map);
				dismiss();
			}else
				cancel();
		}else
			cancel();
	}
	@Override
	public void cancel() {
		super.cancel();
		Toast.makeText(getContext(), R.string.data_select_error, Toast.LENGTH_SHORT).show();
	}
}
