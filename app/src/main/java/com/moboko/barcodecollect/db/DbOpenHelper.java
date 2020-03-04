package com.moboko.barcodecollect.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.moboko.barcodecollect.util.Consts.*;


public class DbOpenHelper extends SQLiteOpenHelper {


    public DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // テーブル作成
        // SQLiteファイルがなければSQLiteファイルが作成される
        db.execSQL(SQL_CREATE_ITEM_LIST);
        db.execSQL(SQL_CREATE_FAVORITE_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE " + ITEM_LIST_TABLE + " RENAME TO " + ITEM_LIST_TABLE_BK);
        db.execSQL("ALTER TABLE " + FAVORITE_LIST_TABLE + " RENAME TO " + FAVORITE_LIST_TABLE_BK);

        onCreate(db);

        db.execSQL(SQL_INSERT_ITEM_LIST_NEW);
        db.execSQL(SQL_INSERT_FAVORITE_LIST_NEW);


        db.execSQL(SQL_DELETE_ITEM_LIST_BK);
        db.execSQL(SQL_DELETE_FAVORITE_LIST_BK);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}