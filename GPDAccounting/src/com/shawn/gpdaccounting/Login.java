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

public class Login extends Activity implements OnClickListener{
	private EditText user_name_edt = null;
	private EditText password_edt = null;
	private Button login_btn = null;
	private Button register_btn = null;
	private SharedPreferences shared = null;
	private SharedPreferences.Editor editor = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		SQLData.SQLDataInit(this);
		shared = getSharedPreferences(Config.SHARED_NAME,MODE_PRIVATE);
		editor = shared.edit();
		String user_name = shared.getString(Config.SHARED_USER_NAME,"");
		String pass_word = shared.getString(Config.SHARED_PASSWORD, "");
		boolean set_flag = shared.getBoolean(Config.SHARED_FLAG, false);
		user_name_edt = (EditText) findViewById(R.id.login_username);
		password_edt = (EditText) findViewById(R.id.login_password);
		login_btn = (Button) findViewById(R.id.login_ok);
		register_btn = (Button) findViewById(R.id.login_register);
		login_btn.setOnClickListener(this);
		register_btn.setOnClickListener(this);
		if(SQLData.selectUserPassword(user_name,pass_word) > 0 && set_flag){
			Config.LOGIN_USER = user_name;
			Intent intent = new Intent(this,MainTable.class);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.login_ok){
			String user_name = user_name_edt.getText().toString();
			String password = password_edt.getText().toString();
			if(SQLData.selectUserPassword(user_name, password) > 0){
				Config.LOGIN_USER = user_name;
				editor.putString(Config.SHARED_USER_NAME, user_name);
				editor.putString(Config.SHARED_PASSWORD, password);
				editor.putBoolean(Config.SHARED_FLAG, true);
				editor.commit();
				Intent intent = new Intent(this,MainTable.class);
				startActivity(intent);
				finish();
			}else
			{
				Toast.makeText(this, R.string.login_error,Toast.LENGTH_SHORT).show();
			}
		}else if (v.getId() == R.id.login_register){
			Intent intent = new Intent(this,Register.class);
			startActivity(intent);
		}
	}
	
}
