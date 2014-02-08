package com.shawn.gpdaccounting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLData {
private static SQLiteDatabase db_read = null;
private static SQLiteDatabase db_write = null;
private static SQLDataManager db_manager = null;
public static void SQLDataInit(Context context){
	if (null == db_read && null == db_write){
		db_manager = new SQLDataManager(context);
	}
}
/**	������Ŀ��¼
 * @param id
 * @param userName
 * @param time
 * @param money
 * @param remark
 * @return
 */
public static long inertAData(int id,String userName,String time,float money,String picturePath,String remark){
	 db_write = db_manager.getWritableDatabase();
	 ContentValues values=new ContentValues();
	 values.put(Config.DATA_USER, userName);
	 values.put(Config.DATA_TIME, time);
	 values.put(Config.DATA_MONEY, money);
	 values.put(Config.DATA_PICTURE_PATH, picturePath);
	 values.put(Config.DATA_REMARK, remark);
	 values.put(Config.DATA_KEY,id);
	 long error = db_write.insertOrThrow(Config.DATA_TABLE_NAME,null, values);
	 db_write.close();
	 return error;
}

/**�����û�����
 * @param userName
 * @param password
 * @return
 */
public static long insertAUser(String userName,String password){
	db_write = db_manager.getWritableDatabase();
	ContentValues values = new ContentValues();
	values.put(Config.USER_NAME, userName);
	values.put(Config.USER_PASSWORD, password);
	long error = db_write.insertOrThrow(Config.USER_TABLE_NAME, null, values);
	db_write.close();
	return error;
}

/**������Ŀ��¼
 * @param id
 * @param userName
 * @param time
 * @param money
 * @param remark
 * @return
 */
public static long updateData(String id,String userName,String time,String money,String remark){
	db_write = db_manager.getWritableDatabase();
	ContentValues values = new ContentValues();
	values.put(Config.DATA_USER, userName);
	values.put(Config.DATA_TIME, time);
	values.put(Config.DATA_MONEY, money);
	values.put(Config.DATA_REMARK, remark);
	long error = db_write.update(Config.DATA_TABLE_NAME, values, Config.DATA_KEY + "= ?", new String[]{id});
	db_write.close();
	return error;
}

/**��֤�û��ʺ�����
 * @param userName
 * @param password
 * @return
 */
public static int selectUserPassword(String userName,String password){
	db_read = db_manager.getReadableDatabase();
	Cursor cur = db_read.query(Config.USER_TABLE_NAME, null, Config.USER_NAME + "= ? and " + Config.USER_PASSWORD+ " = ?", new String[]{userName,password}, null, null, null);
	int have_num = cur.getCount();
	System.out.println("have_num:" +have_num);
	cur.close();
	db_read.close();
	return have_num;
}
/**��֤�û����Ƿ��ظ�
 * @param userName
 * @return
 */
public static int selectHaveUserName(String userName){
	db_read = db_manager.getReadableDatabase();
	Cursor cur = db_read.query(Config.USER_TABLE_NAME, null, Config.USER_NAME + "= ?" , new String[]{userName}, null, null, null);
	int have_name_num = cur.getCount();
	cur.close();
	db_read.close();
	return have_name_num;
}


public static SQLiteDatabase getDbRead(){
	db_read = db_manager.getReadableDatabase();
	return db_read;
} 


/**ɾ������
 * @param arg_string
 * @return
 */
public static long deletedData(String arg_string){
	db_write = db_manager.getWritableDatabase();
	long error = db_write.delete(Config.DATA_TABLE_NAME,Config.DATA_KEY+" = ?",new String[]{arg_string});
	db_write.close();
	return error;
}

}
