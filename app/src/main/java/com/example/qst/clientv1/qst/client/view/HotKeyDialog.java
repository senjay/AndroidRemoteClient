package com.example.qst.clientv1.qst.client.view;


import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.qst.clientv1.R;
import com.example.qst.clientv1.qst.client.data.HotKeyData;
import com.example.qst.clientv1.qst.client.socket.CmdClientSocket;

import java.util.ArrayList;

/**
 * author: 钱苏涛
 * created on: 2019/3/9 13:45
 * description:
 */
public class HotKeyDialog {
    private Context context;
    private ArrayList<HotKeyData> hotKeyList;//热键列表，用于HotKeyGridAdapter填充数据
    private String title;//对话框的标题
    private CmdClientSocket cmdClientSocket;//用于HotKeyGridAdapter的视图点击触发cmdClientSocket向远程端发送命令

    public HotKeyDialog(Context context,ArrayList<HotKeyData> hotKeyList,String title,CmdClientSocket cmdClientSocket)
    {
        this.context=context;
        this.hotKeyList=hotKeyList;
        this.title=title;
        this.cmdClientSocket=cmdClientSocket;
    }

    public  void show()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LinearLayout diaglogLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_hotkey_dialog, null, false);
        builder.setTitle(title);

        builder.setView(diaglogLayout);
        GridView gridView=diaglogLayout.findViewById(R.id.hotkey_grid);
        HotKeyGridAdapter gridAdapter= new HotKeyGridAdapter(context,hotKeyList,cmdClientSocket);
        gridView.setAdapter(gridAdapter);
        builder.show();

    }
}
