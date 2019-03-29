package com.example.qst.clientv1.qst.client.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * author: 钱苏涛
 * created on: 2019/3/29 13:05
 * description:
 */
public class CmddataBase extends SQLiteOpenHelper {
    public static final String TABLENAME_CMD="cmdtable";
    public static final String KEY_ID="_id";
    public static final String KEY_CMD="cmd";
    public static final String KEY_NAME="name";
    public CmddataBase(Context context) {
        super(context, TABLENAME_CMD, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql=String.format("create table if not exists %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT, %s TEXT)",
                TABLENAME_CMD,KEY_ID,KEY_NAME,KEY_CMD);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
