package com.moboko.barcodecollect.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.moboko.barcodecollect.db.DbOpenHelper;
import com.moboko.barcodecollect.entity.DbFavoriteList;
import com.moboko.barcodecollect.entity.DbItemList;

import static com.moboko.barcodecollect.util.Consts.*;

public class UpdateData {
    private DbOpenHelper helper;
    private SQLiteDatabase db;

    DbItemList dbItemList;
    DbFavoriteList dbFavoriteList;

    public UpdateData(DbOpenHelper helper, SQLiteDatabase db) {
        this.helper = helper;
        this.db = db;
    }

    public void setDbItemList(DbItemList dbItemList) {
        this.dbItemList = dbItemList;
    }

    public void setDbFavoriteList(DbFavoriteList dbFavoriteList) {
        this.dbFavoriteList = dbFavoriteList;
    }

    public boolean updateDbItemList(String prm[]) {
        String sql = "update itemList set "
                + ITEM_NM  + " = '" + dbItemList.getItemNm() + "',"
                + CATEGORY_CD  + " = '" + dbItemList.getCategoryCd() + "',"
                + PRICE  + " = " + dbItemList.getPrice() + ","
                + TAX_DIV  + " = '" + dbItemList.getTaxDiv() + "',"
                + SALE_PER + " = " + dbItemList.getSalePer() + ","
                + TAX_PRICE + " = " + dbItemList.getTaxPrice() + ","
                + MEMO1  + " = '" + dbItemList.getMemo1() + "',"
                + REGISTER_DAY  + " = '" + dbItemList.getRegisterDay() + "' "
                + "where " + ID + " = ?";
        Cursor cursor = db.rawQuery(sql, prm);
        cursor.moveToFirst();
        cursor.close();
        return true;
    }

    public boolean updateOldDbItemList(String prm[]) {
        String sql = "update itemList set "
                + DELETE_FLAG  + " = '1' "
                + "where " + ID + " = ?";
        Cursor cursor = db.rawQuery(sql, prm);
        cursor.moveToFirst();
        cursor.close();
        return true;
    }

    public boolean updateDbFavoriteList(String prm[]) {
        String sql = "update favoriteList set "
                + FAVORITE_FLAG  + " = " + dbFavoriteList.getFavoriteFlag()
                + " where " + ID + " = ?";
        Cursor cursor = db.rawQuery(sql, prm);
        cursor.moveToFirst();
        cursor.close();
        return true;
    }

}
