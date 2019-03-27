package com.example.qst.clientv1.qst.client.operator;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * author: 钱苏涛
 * created on: 2019/3/25 21:46
 * description:
 */
public class CheckLocalDownloadFolder {

    public static String check(){
        String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android_Remote_downlod";
        File file=new File(path);
        if (!file.exists())
            file.mkdir();
        return path;
    }
    public static long isFileExists(String filename)//客户端文件是否存在若存在返回长度
    {
        File clientfile=new File(check()+"/"+filename);
        if (clientfile.exists())
            return  clientfile.length();
        else
        {
            return 0;
        }
    }
}
