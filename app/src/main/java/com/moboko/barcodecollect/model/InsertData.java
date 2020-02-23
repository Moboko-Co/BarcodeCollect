package com.moboko.barcodecollect.model;

import android.database.sqlite.SQLiteDatabase;
import android.media.MediaMetadata;

import com.moboko.barcodecollect.db.DbOpenHelper;
import com.moboko.barcodecollect.entity.DbFavoriteList;
import com.moboko.barcodecollect.entity.DbItemList;

import static com.moboko.barcodecollect.util.Consts.*;

public class InsertData {
    private DbOpenHelper helper;
    private SQLiteDatabase db;

    DbItemList dbItemList;
    DbFavoriteList dbFavoriteList;

    public InsertData(DbOpenHelper helper, SQLiteDatabase db) {
        this.helper = helper;
        this.db = db;
    }

    public void setDbItemList(DbItemList dbItemList) {
        this.dbItemList = dbItemList;
    }

    public void setDbFavoriteList(DbFavoriteList dbFavoriteList) {
        this.dbFavoriteList = dbFavoriteList;
    }

    public boolean insertDbItemList() {
        String sql = "insert into itemList ("
                + JAN_CD + ","
                + CATEGORY_CD + ","
                + PRICE + ","
                + TAX_DIV + ","
                + MEMO1 + ","
                + MEMO2 + ","
                + DELETE_FLAG + ","
                + REGISTER_DAY
                + ") values ('"
                + dbItemList.getJanCd() + "', '"
                + dbItemList.getCategoryCd() + "', "
                + dbItemList.getPrice() + ", '"
                + dbItemList.getTaxDiv() + "', '"
                + dbItemList.getMemo1() + "', '"
                + dbItemList.getMemo2() + "', '"
                + dbItemList.getDeleteFlag() + "', '"
                + dbItemList.getRegisterDay() + "');";
        db.execSQL(sql);
        return true;
    }

    public boolean insertDbFavoriteList() {
        //String sql = "insert into favoriteList (pKey,favoriteFlag) values('2020/2/1 12:00','0');";
        String sql = "insert into favoriteList ("
                + FAVORITE_FLAG
                + ") values ('"
                + dbFavoriteList.getFavoriteFlag() + "');";
        db.execSQL(sql);
        return true;
    }


}
