package com.example.qst.clientv1.qst.client.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * author: 钱苏涛
 * created on: 2019/3/27 19:57
 * description:
 */
public class IpdataBase extends SQLiteOpenHelper {

    public static final String TABLENAME_IP="IPandPort";
    public static final String KEY_ID="_id";
    public static final String KEY_IP="ip";
    public static final String KEY_PORT="port";
    public IpdataBase(Context context) {
        super(context, "IPandPort", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql=String.format("create table if not exists %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT, %s TEXT)",
                TABLENAME_IP,KEY_ID,KEY_IP,KEY_PORT);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
