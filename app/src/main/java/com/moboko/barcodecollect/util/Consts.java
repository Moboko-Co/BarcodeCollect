package com.moboko.barcodecollect.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Consts {

    public static final int CAPTURE_REQUEST = 9000;
    public static final int INSERT_REQUEST = 9001;

    public static final String SELECTED_ERROR = "削除対象がありません";
    public static final String CANCELED_MESSAGE = "キャンセルされました";
    public static final String SUCCESS_MESSAGE = "登録に成功しました";
    public static final String COPY_MESSAGE = "クリップボードにコピーしました";
    public static final String COPY_ERROR_MESSAGE = "コピー対象がありません";
    public static final String GET_NAME_FAILED_MESSAGE = "商品名が取得できませんでした";

    public static final int RE_CAPUTRE_RESPONSE = 10;

    public static final String ID_PROC = "idProc";
    public static final String INSERT_FLAG = "1";
    public static final String UPDATE_FLAG = "2";

    public static final String UPDATE_PROC = "updateProc";
    public static final String INSERT_PROC = "insertProc";

    public static final String MONO_URI_HEAD = "https://mnrate.com/search?kwd=";
    public static final String MONO_URI_TAIL = "&i=all";

    public static final String YAHOO_URL = "https://shopping.yahooapis.jp/ShoppingWebService/V1/json/itemSearch?appid=";
    public static final String YAHOO_APP_ID = "dj00aiZpPVpCN1F6amtEM2lPOSZzPWNvbnN1bWVyc2VjcmV0Jng9Nzk-";
    public static final String YAHOO_ADD_JAN_CODE = "&jan=";

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
    public static final String TAX_PRICE = "taxPrice";
    public static final String SALE_PER = "salePer";
    public static final String MEMO1 = "memo1";
    public static final String DELETE_FLAG = "deleteFlag";
    public static final String ITEM_NM = "itemNm";

    public static final int MODE_DEFAULT = 0;
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
                    ITEM_NM + " TEXT," +
                    CATEGORY_CD + " TEXT," +
                    PRICE + " INTEGER," +
                    TAX_DIV + " TEXT," +
                    SALE_PER + " INTEGER," +
                    TAX_PRICE + " INTEGER," +
                    MEMO1 + " TEXT," +
                    DELETE_FLAG + " TEXT," +
                    REGISTER_DAY + " TEXT" +
                    ")";

    public static final String SQL_CREATE_FAVORITE_LIST =
            "CREATE TABLE IF NOT EXISTS " + FAVORITE_LIST_TABLE + " (" +
                    ID + " integer primary key autoincrement," +
                    FAVORITE_FLAG + " INTEGER" +
                    ")";

    public static final String SQL_DELETE_ITEM_LIST =
            "DROP TABLE IF EXISTS " + ITEM_LIST_TABLE;

    public static final String SQL_DELETE_FAVORITE_LIST =
            "DROP TABLE IF EXISTS " + FAVORITE_LIST_TABLE;
    public static final String SELECT_LIST =
            "SELECT a._id,a.janCd,a.itemNm,a.categoryCd,a.price,a.taxDiv,a.salePer,a.taxPrice,a.memo1,a.registerDay,b.favoriteFlag"
                    + " FROM   itemList a,favoriteList b"
                    + " WHERE a._id = b._id";

    public static final String SELECT_MAX_LIST =
            "SELECT a._id,a.janCd,a.categoryCd,a.price,a.taxDiv,a.taxPrice,a.memo1,a.registerDay,b.favoriteFlag"
                    + " FROM   (SELECT max(c._id) FROM itemList c) a,favoriteList b"
                    + " WHERE a._id = b._id";

    public static final String SELECT_COUNT_LIST =
            "SELECT count(a._id)"
                    + " FROM   itemList a,favoriteList b"
                    + " WHERE a._id = b._id";

    public static final String SELECT_OLD_ITEM_LIST =
            "SELECT a._id"
                    + " FROM   itemList a"
                    + ",(SELECT min(b._id) as _id FROM itemList b WHERE b.deleteFlag = '0') c"
                    + " WHERE a._id = c._id";

    public static final String SELECT_ITEM_COUNT_LIST =
            "SELECT count(a._id)"
                    + " FROM   itemList a,favoriteList b"
                    + " WHERE a._id = b._id"
                    + " AND   a.deleteFlag = '0'";

    public static final int MAX_ITEM_COUNT = 30;


    public static final double TAX_PER_0 = 1;
    public static final double TAX_PER_8 = 1.08;
    public static final double TAX_PER_10 = 1.1;

    public static final String WHERE_DELETE_FLAG = " AND a.deleteFlag = '0'";
    public static final String WHERE_FAVORITE_FLAG = " AND b.favoriteFlag = ?";
    public static final String WHERE_ID = " AND a._id = ?";
    public static final String WHERE_JAN_CD = " AND a.janCd = ?";

    public static final String ORDER_BY_LIST = " ORDER BY a._id desc"
            + "         ,a.categoryCd";

    public static final String ALERT_TITLE = "過去履歴あり";
    public static final String ALERT_MESSAGE = "履歴から参照入力しますか？";

    public static final String ALERT_MAX_ITEM_TITLE = "登録可能件数 ： " + MAX_ITEM_COUNT + "件超過";
    public static final String ALERT_MAX_ITEM_MESSAGE = "最も古い明細を削除します。宜しいでしょうか。";

    public static final String DEL_ALERT_TITLE = "データ削除";
    public static final String DEL_ALERT_MESSAGE = "元に戻すことはできません。宜しいでしょうか。";

    public static final String CLIP_BOARD_TAX_1 = "税込";
    public static final String CLIP_BOARD_TAX_2 = "8%";
    public static final String CLIP_BOARD_TAX_3 = "10%";

    public static final String NEW_ITEM_NM = "新規商品";
    public static final int NEW_DIGIT = 0;

    public static final String ALERT_YES = "はい";
    public static final String ALERT_NO = "いいえ";

    public static final String[] SORT_TITLE = {
            "新しい順",
            "古い順",
            "カテゴリ順",
            "JANコード昇順",
            "JANコード降順",
            "価格の高い順",
            "価格の安い順"
    };

    public static final Map<Integer, String> ORDER_BY = Collections.unmodifiableMap(new HashMap<Integer, String>() {{
        put(0, " ORDER BY a._id desc");
        put(1, " ORDER BY a._id");
        put(2, " ORDER BY a.categoryCd asc,a._id desc");
        put(3, " ORDER BY a.janCd asc,a._id desc");
        put(4, " ORDER BY a.janCd desc,a._id desc");
        put(5, " ORDER BY a.taxPrice desc,a._id desc");
        put(6, " ORDER BY a.taxPrice asc,a._id desc");
    }});


    public static final String FAVORITE_LIST_TABLE_BK = "favoriteList_bk";

    public static final String ITEM_LIST_TABLE_BK = "itemList_bk";


    public static final String SQL_DELETE_ITEM_LIST_BK =
            "DROP TABLE IF EXISTS " + ITEM_LIST_TABLE_BK;

    public static final String SQL_DELETE_FAVORITE_LIST_BK =
            "DROP TABLE IF EXISTS " + FAVORITE_LIST_TABLE_BK;


    public static final String SQL_INSERT_ITEM_LIST_NEW = "INSERT INTO "
            + ITEM_LIST_TABLE + " ("
            + ID + ","
            + JAN_CD + ","
            + ITEM_NM + ","
            + CATEGORY_CD + ","
            + PRICE + ","
            + TAX_DIV + ","
            + SALE_PER + ","
            + TAX_PRICE + ","
            + MEMO1 + ","
            + DELETE_FLAG + ","
            + REGISTER_DAY + ") "
            + " SELECT "
            + ID + ","
            + JAN_CD + ","
            + "'新規商品'" + ","
            + CATEGORY_CD + ","
            + PRICE + ","
            + TAX_DIV + ","
            + "0" + ","
            + TAX_PRICE + ","
            + MEMO1 + ","
            + DELETE_FLAG + ","
            + REGISTER_DAY
            + " FROM "
            + ITEM_LIST_TABLE_BK;

    public static final String SQL_INSERT_FAVORITE_LIST_NEW = "INSERT INTO "
            + FAVORITE_LIST_TABLE + " ("
            + ID + ","
            + FAVORITE_FLAG + ") "
            + " SELECT "
            + ID + ","
            + FAVORITE_FLAG
            + " FROM "
            + FAVORITE_LIST_TABLE_BK;

}
