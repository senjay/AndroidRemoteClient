package com.example.qst.clientv1.qst.client.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * author: 钱苏涛
 * created on: 2019/3/27 20:23
 * description:
 */
public class Ipmanager {
    SQLiteDatabase db;
    public Ipmanager(Context context) {
        db=(new IpdataBase(context)).getWritableDatabase();
    }
    public  ArrayList<String> findall()
    {
        ArrayList<String> list =new ArrayList<String>();
        String sql=String.format("select * from %s",IpdataBase.TABLENAME_IP);
        Cursor cursor= db.rawQuery(sql,null);
        cursor.moveToFirst();
        while(cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex(IpdataBase.KEY_IP)));
        }
        return list;
    }

    public void insert(String ip,String port)
    {
        ContentValues cv = new ContentValues();
        cv.put(IpdataBase.KEY_IP,ip);
        cv.put(IpdataBase.KEY_PORT,port);
        db.insert(IpdataBase.TABLENAME_IP,null,cv);
    }
    public int finacount()
    {
        String sql=String.format("select count(*) as count from %s",IpdataBase.TABLENAME_IP);
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        return  cursor.getInt(cursor.getColumnIndex("count"));
    }
    public  void delete()
    {
        String sql=String.format("delete from %s where _id=(select min(_id) from %s)",IpdataBase.TABLENAME_IP,IpdataBase.TABLENAME_IP);
        db.execSQL(sql);
    }
    public boolean findIpandPort(String ip,String port)
    {
        String sql=String.format("select count(*) as count from %s where ip= ? and port = ?",IpdataBase.TABLENAME_IP);
        String [] args=new String[] {ip,port};
        Cursor cursor = db.rawQuery(sql,args);
        cursor.moveToFirst();
        int count= cursor.getInt(cursor.getColumnIndex("count"));
        if(count>=1)
            return false;//已存在
        else
            return true;//不存在
    }
    public  void saveip(String ip,String port)
    {
        int count=finacount();
        boolean flag=findIpandPort(ip,port);
        if(flag)
        {
            if(count>=5)
            {
                delete();
                insert(ip,port);
            }
            else
                insert(ip, port);
        }
    }

}
