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

/**
 * author: 钱苏涛
 * created on: 2019/3/2 15:01
 * description:
 */
public class ShowNonUiUpdateCmdHandler extends Handler {
    private Context context;
    public ShowNonUiUpdateCmdHandler(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Bundle bundle =msg.getData();
        ArrayList<String> list =bundle.getStringArrayList(CmdClientSocket.KEY_SERVER_ACK_MSG);
        //ArrayList<NetFileData> filelist=new ArrayList<NetFileData>();
        int status=msg.arg2;
        if(status==CmdClientSocket.SERVER_MSG_OK)
        {

        }
        else
        {
            Toast.makeText(context,list.toString(),Toast.LENGTH_SHORT).show();
        }

    }
}
