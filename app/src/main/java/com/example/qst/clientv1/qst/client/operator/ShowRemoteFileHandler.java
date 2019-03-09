package com.example.qst.clientv1.qst.client.operator;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;


import com.example.qst.clientv1.qst.client.data.AppValues;
import com.example.qst.clientv1.qst.client.data.NetFileData;
import com.example.qst.clientv1.qst.client.socket.CmdClientSocket;
import com.example.qst.clientv1.qst.client.view.FilePathAdapter;
import com.example.qst.clientv1.qst.client.view.NetFileListAdapter;

import java.util.ArrayList;

public class ShowRemoteFileHandler extends Handler {
    private Context context;
    private ListView listView;
    RecyclerView recyclerView;
    public ShowRemoteFileHandler(Context context, ListView listView,RecyclerView recyclerView) {
        super();
        this.context = context;
        this.listView = listView;
        this.recyclerView=recyclerView;
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
            filelist.add(new NetFileData());
            filelist.add(new NetFileData(path));
            for(int i=2;i<list.size();i++)
            {
                NetFileData fileData=new NetFileData(list.get(i),path);
                filelist.add(fileData);
            }
            //更新recycleview
            FilePathAdapter filePathAdapter=new FilePathAdapter(context,path);
            recyclerView.setAdapter(filePathAdapter);
            filePathAdapter.setOnItemClickLisenter(new FilePathAdapter.PathOnItemClickLisenter() {
                @Override
                public void OnClick(String filePath) {
                    ShowRemoteFileHandler showRemoteFileHandler = new ShowRemoteFileHandler(context, listView,recyclerView);//会更新ListView的句柄
                    AppValues appValues=(AppValues)context.getApplicationContext();
                    CmdClientSocket cmdClientSocket = new CmdClientSocket(appValues.getIp(), appValues.getPort(),showRemoteFileHandler);
                    cmdClientSocket.work("dir:"+filePath);


                }
            });
            //自动滑动到最右边
           // recyclerView.smoothScrollToPosition(filePathAdapter.getItemCount()-1); 此法不起作用
            recyclerView.scrollToPosition(filePathAdapter.getItemCount()-1);
            //更新listview
            NetFileListAdapter netFileListAdapter=new NetFileListAdapter(context,filelist);
            listView.setAdapter(netFileListAdapter);
        }
        else
        {
            //给Noui传递消息
            ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler = new ShowNonUiUpdateCmdHandler(context);
            Message message = showNonUiUpdateCmdHandler.obtainMessage();
            Bundle bundle2 = new Bundle();
            message.arg2=status;
            bundle.putStringArrayList(CmdClientSocket.KEY_SERVER_ACK_MSG,list);
            message.setData(bundle);
            showNonUiUpdateCmdHandler.sendMessage(message);
        }


    }
}
