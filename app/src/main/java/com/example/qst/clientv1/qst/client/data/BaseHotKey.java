package com.example.qst.clientv1.qst.client.data;

import java.util.ArrayList;

/**
 * author: 钱苏涛
 * created on: 2019/3/9 13:13
 * description:
 */
public abstract class BaseHotKey {
    abstract ArrayList<HotKeyData> generateData();

    public BaseHotKey() {

    }
    public  ArrayList<HotKeyData> getHotkeyList()
    {
        return  generateData();
    }
}
