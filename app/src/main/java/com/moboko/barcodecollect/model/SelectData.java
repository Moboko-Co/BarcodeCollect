package com.moboko.barcodecollect.model;

import android.content.ClipData;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.moboko.barcodecollect.db.DbOpenHelper;
import com.moboko.barcodecollect.entity.ItemList;

import java.util.ArrayList;
import java.util.List;

import static com.moboko.barcodecollect.util.Consts.*;

public class SelectData {

    String sql;
    String sqlPrm;
    Cursor cursor;
    List<ItemList> itemList = new ArrayList<>();
    private DbOpenHelper helper;
    private SQLiteDatabase db;

    public void setSql(String sql){
        this.sql = sql;
    }
    public void setSqlPrm(String sqlPrm){
        this.sqlPrm = sqlPrm;
    }

    public void selectSql(String prm[]){
        cursor = db.rawQuery(sql, prm);
        cursor.moveToNext();
    }
    public List<ItemList> getData(int mode) {
        for(int i = 0; i<cursor.getCount();i++){
            if (i == 30) break;
            ItemList sqlRes = new ItemList();
            sqlRes.set_id(cursor.getInt(0));
            sqlRes.setJanCd(cursor.getString(1));
            sqlRes.setCategoryCd(cursor.getString(2));
            sqlRes.setPrice(cursor.getInt(3));
            sqlRes.setTaxDiv(cursor.getString(4));
            sqlRes.setTaxPrice(cursor.getInt(5));
            sqlRes.setMemo1(cursor.getString(6));
            sqlRes.setMemo2(cursor.getString(7));
            sqlRes.setRegisterDay(cursor.getString(8));
            sqlRes.setFavoriteFlag(cursor.getInt(9));

            if(mode == OPTION_ALL_SELECT) {
                sqlRes.setCbSelected(true);
            } else if(mode == OPTION_ZERO_SELECT){
                sqlRes.setCbSelected(false);
            } else if(mode == OPTION_ALL_FAVORITE && sqlRes.getFavoriteFlag() == 1){
                sqlRes.setCbSelected(true);
            }else if(mode == OPTION_ZERO_FAVORITE){
                sqlRes.setCbSelected(false);
            }

            itemList.add(sqlRes);
            cursor.moveToNext();
        }
        cursor.close();
        return itemList;
    }

    public SelectData(DbOpenHelper helper, SQLiteDatabase db) {
        this.helper = helper;
        this.db = db;
    }


    public int getCount() {
        return cursor.getInt(0);
    }
}

