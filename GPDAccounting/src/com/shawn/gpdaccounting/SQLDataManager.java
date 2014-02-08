package com.shawn.gpdaccounting;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLDataManager extends SQLiteOpenHelper{

	public SQLDataManager(Context context) {
		super(context, Config.DATA_NAME, null, Config.DATA_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String create_data = "CREATE TABLE IF NOT EXISTS " + Config.DATA_TABLE_NAME +"(" +Config.DATA_KEY+" INTEGER PRIMARY KEY," + Config.DATA_USER + " VARCHAR(100),"+
							Config.DATA_TIME +" TIMESTAMP," + Config.DATA_MONEY + " FLOAT,"+ Config.DATA_PICTURE_PATH+" VARCHAR(254), " + Config.DATA_REMARK + " TEXT);";
		db.execSQL(create_data);
		
		String create_user = "CREATE TABLE IF NOT EXISTS " + Config.USER_TABLE_NAME + "(" + Config.USER_NAME + " VARCHAR(100) PRIMARY KEY," + 
							Config.USER_PASSWORD + " TEXT);";
		db.execSQL(create_user);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		 db.execSQL("DROP TABLE IF EXISTS"+Config.DATA_NAME);
	     onCreate(db);
	}
	
}
