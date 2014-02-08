package com.shawn.gpdaccounting;

import java.util.Calendar;
import java.util.Locale;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.Spinner;
import android.widget.TextView;

public class Statistics extends Fragment implements OnClickListener,OnDateChangedListener {
private DatePicker start_date;
private DatePicker end_date;
private Spinner user_spinner;
private TextView show_txt;
private Button statistics_btn;
private ArrayAdapter<String> adapter; 
int s_year,s_month,s_day;
int e_year,e_month,e_day;
@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	View view = inflater.inflate(R.layout.statistics, null);
	start_date = (DatePicker) view.findViewById(R.id.statistics_start_date);
	end_date = (DatePicker) view.findViewById(R.id.statistics_end_date);
	user_spinner = (Spinner) view.findViewById(R.id.statistics_user_spinner);
	statistics_btn = (Button) view.findViewById(R.id.statistics_btn);
	show_txt = (TextView) view.findViewById(R.id.statistice_show_txt); 
	statistics_btn.setOnClickListener(this);
	Calendar c = Calendar.getInstance();  
	int year = c.get(Calendar.YEAR);  
	int month = c.get(Calendar.MONTH);  
	int day = c.get(Calendar.DAY_OF_MONTH);
	start_date.init(year, month, 1,this);
	end_date.init(year,month,day,this);
	s_year = e_year = year;
	s_month = e_month = month;
	s_day = 1;
	e_day = day;
	SQLiteDatabase db = SQLData.getDbRead();
	Cursor cur = db.query(Config.USER_TABLE_NAME, null, null, null, null, null, null);
	cur.moveToFirst();
	int count = cur.getCount();
	String[] spinner_list = new String[count+1];
	for(int i = 0;i<count;i++){
		cur.moveToPosition(i);
		spinner_list[i] = cur.getString(0);
	}
	cur.close();
	db.close();
	spinner_list[count] = getString(R.string.all);
	adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,spinner_list);
	adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
	user_spinner.setAdapter(adapter);
	return view;
	}

@Override
public void onClick(View v) {
	if(v.getId() == R.id.statistics_btn){
		String user_name = user_spinner.getSelectedItem().toString();
		String fs = "%d-%02d-%02d"; 
		String start_time =  String.format(Locale.getDefault(),fs, s_year,s_month+1,s_day);
		String end_time = String.format(Locale.getDefault(),fs,e_year,e_month+1,e_day);
		SQLiteDatabase db = SQLData.getDbRead();
		String select_all_money = "SELECT SUM(" + Config.DATA_MONEY+") FROM " + Config.DATA_TABLE_NAME + " WHERE "+Config.DATA_TIME + " BETWEEN '" + start_time + "' AND '" + end_time+"';"; 
		if (!user_name.equals(getString(R.string.all))){
			select_all_money = "SELECT SUM(" + Config.DATA_MONEY+") FROM " + Config.DATA_TABLE_NAME + " WHERE "+ Config.DATA_USER + " = '"+ user_name+"' AND "+Config.DATA_TIME + " BETWEEN '" + start_time + "' AND '" + end_time+"';"; 
		}
		int all_money = 0;
		System.out.println(" " + select_all_money);
		Cursor cur = db.rawQuery(select_all_money,null);
		if(cur != null){
			cur.moveToFirst();
			all_money = cur.getInt(0);
		}
		cur.close();
		db.close();
		String show_text = String.format(Locale.getDefault(), getResources().getString(R.string.show_txt), start_time,end_time,user_name,all_money+"");
		show_txt.setText(show_text);
	}
}
@Override
public void onDateChanged(DatePicker view, int year, int month, int day) {
	if(view.getId() == R.id.statistics_start_date){
		s_year = year;
		s_month = month;
		s_day = day;
	}else if(view.getId() == R.id.statistics_end_date){
		e_year = year;
		e_month = month;
		e_day = day;
	}
}
}
