package com.example.qst.clientv1.qst.client.data;

import java.util.ArrayList;

/**
 * author: 钱苏涛
 * created on: 2019/3/9 18:20
 * description:
 */
public class Default_HotKey extends BaseHotKey {
    @Override
    ArrayList<HotKeyData> generateData() {

        ArrayList<HotKeyData> hotKeyList=new ArrayList<HotKeyData>();
        hotKeyList.add(new HotKeyData("最大化","key:vk_windows+vk_up"));
        hotKeyList.add(new HotKeyData("最小化","key:vk_windows+vk_down"));
        hotKeyList.add(new HotKeyData("退出","key:vk_alt+vk_f4"));
        hotKeyList.add(new HotKeyData("切换程序","key:vk_alt+vk_shift+vk_tab"));
        return hotKeyList;
    }
}
