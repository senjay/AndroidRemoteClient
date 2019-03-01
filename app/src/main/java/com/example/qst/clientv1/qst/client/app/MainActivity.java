package com.example.qst.clientv1.qst.client.app;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qst.clientv1.R;
import com.example.qst.clientv1.SocketClient;
import com.example.qst.clientv1.qst.client.operator.ShowRemoteFileHandler;
import com.example.qst.clientv1.qst.client.socket.CmdClientSocket;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    TextView tv;
    Handler handler;
    ShowRemoteFileHandler srfhandler;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
       // tv=findViewById(R.id.textview);
        listView=findViewById(R.id.filelist);
        srfhandler=new ShowRemoteFileHandler(MainActivity.this,listView);

//        handler=new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//               Bundle bundle =msg.getData();
//                ArrayList<String> list =bundle.getStringArrayList(SocketClient.KEY_SERVER_ACK_MSG);
//                StringBuilder sb=new StringBuilder();
//                for(int i=0;i<list.size();i++)
//                {
//                    sb.append(list.get(i)+"\n");
//                }
//                tv.setText(sb.toString());
//                return true;
//            }
//        });
        Button bt=findViewById(R.id.submit);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_ip=findViewById(R.id.edit_ip);
                EditText et_port=findViewById(R.id.edit_port);
                EditText et_cmd=findViewById(R.id.edit_cmd);
                String ip=et_ip.getText().toString().trim();
                int port=Integer.parseInt(et_port.getText().toString());
                String cmd=et_cmd.getText().toString().trim();
                CmdClientSocket socketClient=new CmdClientSocket(ip,port,srfhandler);
                socketClient.work(cmd);
            }
        });
    }
}
