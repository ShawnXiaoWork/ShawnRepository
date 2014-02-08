package com.shawn.gpdaccounting;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainTable extends FragmentActivity {
private FragmentTabHost m_tabhost = null;
private LayoutInflater m_inflater = null;
@SuppressWarnings("rawtypes")
private Class m_page[] = {Main.class,DataList.class,Statistics.class};

private int m_page_txt[] = {R.string.main,R.string.list,R.string.statistics};
@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main_tab);
		initView();
	}
private void initView(){
	m_inflater = LayoutInflater.from(this);
	m_tabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
	m_tabhost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
	int count = m_page.length;
	DisplayMetrics mDisplayMetrics = new DisplayMetrics();
	getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
	Config.SCREEN_WIDTH = mDisplayMetrics.widthPixels;
	Config.SCREEN_HEIGHT = mDisplayMetrics.heightPixels;
	for(int i = 0;i<count;i++){
		TabSpec tab_spec = m_tabhost.newTabSpec(getString(m_page_txt[i])).setIndicator(getTabItemView(i));
		m_tabhost.addTab(tab_spec,m_page[i],null);
	}
}
private View getTabItemView(int index){
	View view = m_inflater.inflate(R.layout.tab_item, null);
//	ImageView image_view = (ImageView) view.findViewById(R.id.tab_item_image);
	TextView text_view = (TextView) view.findViewById(R.id.tab_item_txt);
	text_view.setText(m_page_txt[index]);
	return view;
}
}
