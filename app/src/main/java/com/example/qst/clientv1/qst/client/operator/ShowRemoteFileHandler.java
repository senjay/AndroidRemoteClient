package com.example.qst.clientv1.qst.client.operator;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.qst.clientv1.SocketClient;
import com.example.qst.clientv1.qst.client.data.NetFileData;
import com.example.qst.clientv1.qst.client.socket.CmdClientSocket;
import com.example.qst.clientv1.qst.client.view.NetFileListAdapter;

import java.util.ArrayList;

public class ShowRemoteFileHandler extends Handler {
    private Context context;
    private ListView listView;
    public ShowRemoteFileHandler(Context context, ListView listView) {
        super();
        this.context = context;
        this.listView = listView;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Bundle bundle =msg.getData();
        ArrayList<String> list =bundle.getStringArrayList(CmdClientSocket.KEY_SERVER_ACK_MSG);
        ArrayList<NetFileData> filelist=new ArrayList<NetFileData>();
        String path=list.get(0);//文件路径
        for(int i=1;i<list.size();i++)
        {
            NetFileData fileData=new NetFileData(list.get(i),path);
            filelist.add(fileData);
        }
        NetFileListAdapter netFileListAdapter=new NetFileListAdapter(context,filelist);
        //更新listview
       // ArrayAdapter<String> adapter=new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,list);
        listView.setAdapter(netFileListAdapter);

    }
}
