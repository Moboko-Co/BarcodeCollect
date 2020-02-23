package com.moboko.barcodecollect.util;

public class Consts {

    public static final int CAPTURE_REQUEST = 9000;
    public static final int INSERT_REQUEST = 9001;

    public static final String SELECTED_ERROR = "削除対象がありません";
    public static final String CANCELED_MESSAGE = "キャンセルされました";
    public static final String SUCCESS_MESSAGE = "登録に成功しました";

    public static final int RE_CAPUTRE_RESPONSE = 10;

    public static final String ID_PROC = "idProc";
    public static final String INSERT_FLAG = "1";
    public static final String UPDATE_FLAG = "2";

    public static final String UPDATE_PROC = "updateProc";
    public static final String INSERT_PROC = "insertProc";

    // データーベースのバージョン
    public static final int DATABASE_VERSION = 1;

    // データーベース名
    public static final String DATABASE_NAME = "itemList.db";

    public static final String FAVORITE_LIST_TABLE = "favoriteList";
    public static final String FAVORITE_FLAG = "favoriteFlag";

    public static final String ITEM_LIST_TABLE = "itemList";
    public static final String ID = "_id";
    public static final String REGISTER_DAY = "registerDay";
    public static final String JAN_CD = "janCd";
    public static final String CATEGORY_CD = "categoryCd";
    public static final String PRICE = "price";
    public static final String TAX_DIV = "taxDiv";
    public static final String MEMO1 = "memo1";
    public static final String MEMO2 = "memo2";
    public static final String DELETE_FLAG = "deleteFlag";

    public static final int NORMAL_MODE = 1;
    public static final int OPTION_MODE = 2;
    public static final int FAVORITE_SHOW_MODE = 3;
    public static final int OPTION_ALL_SELECT = 4;
    public static final int OPTION_ZERO_SELECT = 5;
    public static final int OPTION_ALL_FAVORITE = 6;
    public static final int OPTION_ZERO_FAVORITE = 7;


    public static final String SQL_CREATE_ITEM_LIST =
            "CREATE TABLE IF NOT EXISTS " + ITEM_LIST_TABLE + " (" +
                    ID + " integer primary key autoincrement," +
                    JAN_CD + " TEXT," +
                    CATEGORY_CD + " TEXT," +
                    PRICE + " INTEGER," +
                    TAX_DIV + " TEXT," +
                    MEMO1 + " TEXT," +
                    MEMO2 + " TEXT," +
                    DELETE_FLAG + " TEXT," +
                    REGISTER_DAY + " TEXT" +
                    ")";

    public static final String SQL_CREATE_FAVORITE_LIST =
            "CREATE TABLE IF NOT EXISTS " + FAVORITE_LIST_TABLE + " (" +
                    ID + " integer primary key autoincrement," +
                    FAVORITE_FLAG + " TEXT" +
                    ")";

    public static final String SQL_DELETE_ITEM_LIST =
            "DROP TABLE IF EXISTS " + ITEM_LIST_TABLE;

    public static final String SQL_DELETE_FAVORITE_LIST =
            "DROP TABLE IF EXISTS " + FAVORITE_LIST_TABLE;
    public static final String SELECT_LIST =
              "SELECT a._id,a.janCd,a.categoryCd,a.price,a.taxDiv,a.memo1,a.memo2,a.registerDay,b.favoriteFlag"
            + " FROM   itemList a,favoriteList b"
            + " WHERE a._id = b._id";

    public static final String SELECT_MAX_LIST =
            "SELECT max(a._id),a.janCd,a.categoryCd,a.price,a.taxDiv,a.memo1,a.memo2,a.registerDay,b.favoriteFlag"
                    + " FROM   itemList a,favoriteList b"
                    + " WHERE a._id = b._id";

    public static final String SELECT_COUNT_LIST =
            "SELECT count(a._id)"
                    + " FROM   itemList a,favoriteList b"
                    + " WHERE a._id = b._id";

    public static final String WHERE_DELETE_FLAG = " AND a.deleteFlag = '0'";
    public static final String WHERE_FAVORITE_FLAG = " AND b.favoriteFlag = ?";
    public static final String WHERE_ID = " AND a._id = ?";
    public static final String WHERE_JAN_CD = " AND a.janCd = ?";

    public static final String ORDER_BY_LIST = " ORDER BY a._id desc"
            + "         ,a.categoryCd";

    public static final String ALERT_TITLE = "過去履歴あり";
    public static final String ALERT_MESSAGE = "履歴から参照入力しますか？";

}
