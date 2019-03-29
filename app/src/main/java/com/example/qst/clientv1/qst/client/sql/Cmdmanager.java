package com.example.qst.clientv1.qst.client.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * author: 钱苏涛
 * created on: 2019/3/29 13:10
 * description:
 */
public class Cmdmanager {
    SQLiteDatabase db;
    public Cmdmanager(Context context) {
        db=(new CmddataBase(context)).getWritableDatabase();
    }

    public ArrayList<HashMap<String,String>> findAll(ArrayList<HashMap<String,String>> list)
    {
        list.clear();
        //ArrayList<HashMap<String,String>>list=new ArrayList<HashMap<String, String>>();
        Cursor cursor= db.rawQuery("select *from cmdtable",null);
        cursor.moveToFirst();
        while(cursor.moveToNext()) {
            HashMap<String,String> map=new HashMap<String, String>();
            map.put(CmddataBase.KEY_ID,cursor.getString(cursor.getColumnIndex(CmddataBase.KEY_ID)));
            map.put(CmddataBase.KEY_NAME,cursor.getString(cursor.getColumnIndex(CmddataBase.KEY_NAME)));
            map.put(CmddataBase.KEY_CMD,cursor.getString(cursor.getColumnIndex(CmddataBase.KEY_CMD)));
            list.add(map);
        }
        return list;
    }
    public long insert(String name,String cmd)
    {
        ContentValues cv = new ContentValues();
        cv.put(CmddataBase.KEY_NAME,name);
        cv.put(CmddataBase.KEY_CMD,cmd);
        long ans=db.insert(CmddataBase.TABLENAME_CMD,null,cv);
        return  ans;
    }
    public int update(String id,String name,String cmd)
    {
        ContentValues cv = new ContentValues();
        cv.put(CmddataBase.KEY_NAME,name);
        cv.put(CmddataBase.KEY_CMD,cmd);
        int ans=db.update(CmddataBase.TABLENAME_CMD,cv,"_id=?",new String []{id});
        return ans;
    }
    public  int delete(String id)
    {
        int ans=db.delete(CmddataBase.TABLENAME_CMD,"_id=?",new String[]{id});
        return ans;
    }
}
