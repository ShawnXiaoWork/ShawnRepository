package com.shawn.gpdaccounting;

public class Config {
public static int SCREEN_WIDTH = 480;
public static int SCREEN_HEIGHT = 800;
//数据库字段
public static final int DATA_VERSION = 1;
public static final String DATA_NAME = "accounting.db";
public static final String DATA_TABLE_NAME = "ACCOUNTING_TABLE";
public static final String DATA_KEY = "DATA_KEY";
public static final String DATA_TIME = "DATA_TIME";
public static final String DATA_USER = "DATA_USER";
public static final String DATA_MONEY = "DATA_MONEY";
public static final String DATA_REMARK = "DATA_REMARK";
public static final String DATA_PICTURE_PATH = "DATA_PICTURE_PATH";

public static final String USER_TABLE_NAME = "USER_TABLE";
public static final String USER_KEY = "USER_KEY";
public static final String USER_NAME = "USER_NAME";
public static final String USER_PASSWORD = "USER_PASSWORD";
//SharedPreferences数据
public static final String SHARED_NAME = "user_data";
public static final String SHARED_KEY_ID = "user_key_id";//数据关键key
public static final String SHARED_USER_NAME = "user_name";
public static final String SHARED_PASSWORD = "";
public static final String SHARED_FLAG = "login_flag";//登录标记
//默认的备注参数
public static final String DEFAULT_SPINNER1 = "default_spinner1";
public static final String DEFAULT_SPINNER2 = "default_spinner2";
public static final String DEFAULT_SPINNER3 = "default_spinner3";
public static final String DEFAULT_SPINNER4 = "default_spinner4";
public static final String DEFAULT_SPINNER5 = "default_spinner5";

//listview的item数据索引
public static final String ITEM_ID = "item_id";
public static final String ITEM_INDEX = "item_index";//删除数据时当前选中的item的索引的索引
public static final String ITEM_TIME = "item_time";
public static final String ITEM_NAME = "item_name";
public static final String ITEM_MONEY = "item_money";
public static final String ITEM_IMAGE = "item_image";
public static final String ITEM_REMARK = "item_remark";

//image 路径保存
public static final String IMAGE_INDEX_ID = "image_index_id";
public static final String IMAGE_ID = "image_id";
public static final String IMAGE_PATH = "image_path";
public static String LOGIN_USER = "Shawn";

public static String SELECT_DATE = "select_date";
}
