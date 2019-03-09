package com.example.qst.clientv1.qst.client.data;

import java.util.ArrayList;

/**
 * author: 钱苏涛
 * created on: 2019/3/9 13:21
 * description:
 */
public class PPT_HotKey extends BaseHotKey {
    @Override
    ArrayList<HotKeyData> generateData() {
        ArrayList<HotKeyData> hotKeyList=new ArrayList<HotKeyData>();
        hotKeyList.add(new HotKeyData("切换程序","key:vk_alt+vk_shift+vk_tab"));
        hotKeyList.add(new HotKeyData("ESC","key:vk_escape"));
        hotKeyList.add(new HotKeyData("上一页","key:vk_page_up"));
        hotKeyList.add(new HotKeyData("下一页","key:vk_page_down"));
        hotKeyList.add(new HotKeyData("从头放映","key:vk_f5"));
        hotKeyList.add(new HotKeyData("当前页放映","key:vk_shift+vk_f5"));
        hotKeyList.add(new HotKeyData("退出程序","key:vk_alt+vk_f4"));
        hotKeyList.add(new HotKeyData("黑屏/正常","key:vk_b"));
        return hotKeyList;
    }
}
