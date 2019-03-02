package com.example.qst.clientv1.qst.client.operator;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.Toast;

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
        int status=msg.arg2;
        if(status==CmdClientSocket.SERVER_MSG_OK)
        {
            String path=list.get(1);//文件路径
            for(int i=2;i<list.size();i++)
            {
                NetFileData fileData=new NetFileData(list.get(i),path);
                filelist.add(fileData);
            }
            NetFileListAdapter netFileListAdapter=new NetFileListAdapter(context,filelist);
            //更新listview
            listView.setAdapter(netFileListAdapter);
        }
        else
        {
            Toast.makeText(context,list.toString(),Toast.LENGTH_SHORT).show();
        }


    }
}
