package com.example.qst.clientv1.qst.client.operator;

import com.example.qst.clientv1.qst.client.data.Default_HotKey;
import com.example.qst.clientv1.qst.client.data.HotKeyData;
import com.example.qst.clientv1.qst.client.data.Movie_HotKey;
import com.example.qst.clientv1.qst.client.data.NetFileData;
import com.example.qst.clientv1.qst.client.data.PPT_HotKey;

import java.util.ArrayList;

/**
 * author: 钱苏涛
 * created on: 2019/3/9 13:37
 * description:
 */
public class HotKeyGenerator {

    public static ArrayList<HotKeyData> getHotkeyList(NetFileData fileData){
        String type=fileData.getFileName().substring(fileData.getFileName().indexOf('.')+1);
        switch (type)
        {
            case "ppt":
                return new PPT_HotKey().getHotkeyList();

            case "mp4":
                return  new Movie_HotKey().getHotkeyList();
            default:
                return new Default_HotKey().getHotkeyList();
        }

    }
}
