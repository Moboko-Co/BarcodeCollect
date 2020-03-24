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

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setSqlPrm(String sqlPrm) {
        this.sqlPrm = sqlPrm;
    }

    public void selectSql(String prm[]) {
        cursor = db.rawQuery(sql, prm);
        cursor.moveToNext();
    }

    public List<ItemList> getData(int mode) {
        int count = 0;
        for (int i = 0; i < cursor.getCount(); i++) {
            if (count == MAX_ITEM_COUNT) break;
            if(cursor.getInt(10) == 0 && mode == FAVORITE_SHOW_MODE) {
            } else {
                ItemList sqlRes = new ItemList();
                sqlRes.set_id(cursor.getInt(0));
                sqlRes.setJanCd(cursor.getString(1));
                sqlRes.setItemNm(cursor.getString(2));
                sqlRes.setCategoryCd(cursor.getString(3));
                sqlRes.setPrice(cursor.getInt(4));
                sqlRes.setTaxDiv(cursor.getString(5));
                sqlRes.setSalePer(cursor.getInt(6));
                sqlRes.setTaxPrice(cursor.getInt(7));
                sqlRes.setMemo1(cursor.getString(8));
                sqlRes.setRegisterDay(cursor.getString(9));
                sqlRes.setFavoriteFlag(cursor.getInt(10));
                sqlRes.setSeq(i + 1);

                if (mode == OPTION_ALL_SELECT) {
                    sqlRes.setCbSelected(true);
                } else if (mode == OPTION_ZERO_SELECT) {
                    sqlRes.setCbSelected(false);
                } else if (mode == OPTION_ALL_FAVORITE && sqlRes.getFavoriteFlag() == 1) {
                    sqlRes.setCbSelected(true);
                } else if (mode == OPTION_ZERO_FAVORITE) {
                    sqlRes.setCbSelected(false);
                }

                itemList.add(sqlRes);
            }
            count ++;
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
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int get_Id() {
        int _id = cursor.getInt(0);
        cursor.close();
        return _id;
    }
}

