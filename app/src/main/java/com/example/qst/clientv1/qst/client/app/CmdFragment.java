package com.example.qst.clientv1.qst.client.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.qst.clientv1.R;
import com.example.qst.clientv1.qst.client.data.AppValues;
import com.example.qst.clientv1.qst.client.operator.ShowNonUiUpdateCmdHandler;
import com.example.qst.clientv1.qst.client.socket.CmdClientSocket;

/**
 * author: 钱苏涛
 * created on: 2019/3/23 18:42
 * description:
 */
public class CmdFragment extends Fragment {
    EditText et_cmd;
    Button submit;
    String ip;
    int port;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.layout_cmd, container, false);
        et_cmd=view.findViewById(R.id.edit_cmd);
        submit=view.findViewById(R.id.submit_cmd);
        AppValues appValues=(AppValues)getContext().getApplicationContext();
        ip=appValues.getIp();
        port=appValues.getPort();
        ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler = new ShowNonUiUpdateCmdHandler(getContext());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler = new ShowNonUiUpdateCmdHandler(getContext());
                CmdClientSocket cmdClient = new CmdClientSocket(ip, port,showNonUiUpdateCmdHandler);
                String cmd=et_cmd.getText().toString().trim();
                String []cmdlist=cmd.split("\n");
                cmdClient.work(cmdlist);
            }
        });
        return view;
    }
}
