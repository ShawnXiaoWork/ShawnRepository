package com.shawn.gpdaccounting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity implements OnClickListener{
private EditText username_edt = null;
private EditText password_edt = null;
private EditText repeat_edt = null;
private Button ok_btn = null;
private SharedPreferences m_shared = null;
private SharedPreferences.Editor editor = null;
@Override
protected void onCreate(Bundle savedInstanceState){
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.settings);
	m_shared = getSharedPreferences(Config.SHARED_NAME,MODE_PRIVATE);
	editor = m_shared.edit();
	username_edt = (EditText) findViewById(R.id.set_username);
	password_edt = (EditText) findViewById(R.id.set_passwords);
	repeat_edt = (EditText) findViewById(R.id.set_repeat_passwords);
	ok_btn = (Button) findViewById(R.id.set_ok_btn);
	ok_btn.setOnClickListener(this);
}
@Override
public void onClick(View v) {
	String user_name = username_edt.getText().toString();
	String password = password_edt.getText().toString();
	String repeat = repeat_edt.getText().toString();
	if(user_name.equals("") || password.toString().equals("") || repeat.toString().equals("")){
		Toast.makeText(this, R.string.set_info_uncomplete_error, Toast.LENGTH_SHORT).show();
	}else if(!password.equals(repeat)){
		Toast.makeText(this,R.string.set_passwords_differents_error, Toast.LENGTH_SHORT).show();
	}else if(SQLData.selectHaveUserName(user_name) != 0){
		Toast.makeText(this,R.string.set_have_user, Toast.LENGTH_SHORT).show();
	}else{
		if(SQLData.insertAUser(user_name, password) != -1){
			Config.LOGIN_USER = user_name;
			editor.putString(Config.SHARED_USER_NAME, user_name);
			editor.putString(Config.SHARED_PASSWORD, password);
			editor.putBoolean(Config.SHARED_FLAG, true);
			editor.commit();
			Intent intent = new Intent(this,MainTable.class);
			startActivity(intent);
			finish();
		}
	} 
		
}
}
