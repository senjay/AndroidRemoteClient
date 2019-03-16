package com.example.qst.clientv1.qst.client.app;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qst.clientv1.R;
import com.example.qst.clientv1.SocketClient;
import com.example.qst.clientv1.qst.client.data.AppValues;
import com.example.qst.clientv1.qst.client.data.HotKeyData;
import com.example.qst.clientv1.qst.client.data.NetFileData;
import com.example.qst.clientv1.qst.client.operator.HotKeyGenerator;
import com.example.qst.clientv1.qst.client.operator.ShowNonUiUpdateCmdHandler;
import com.example.qst.clientv1.qst.client.operator.ShowRemoteFileHandler;
import com.example.qst.clientv1.qst.client.socket.CmdClientSocket;
import com.example.qst.clientv1.qst.client.view.HotKeyDialog;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    Handler handler;
    String ip;
    int port;
    ShowRemoteFileHandler srfhandler;
    ListView listView;
    View hidearea;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        listView=findViewById(R.id.filelist);
        registerForContextMenu(listView);//注册上下文菜单
        Button hidebt=findViewById(R.id.hide);
        hidearea=findViewById(R.id.hidearea);
        Button submitbt=findViewById(R.id.submit);
        recyclerView=findViewById(R.id.recyclerview);
        srfhandler=new ShowRemoteFileHandler(MainActivity.this,listView,recyclerView);

        //隐藏输入区域

        hidebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidearea.setVisibility(View.GONE);
            }
        });

        //点击提交命令
        submitbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_ip=findViewById(R.id.edit_ip);
                EditText et_port=findViewById(R.id.edit_port);
                EditText et_cmd=findViewById(R.id.edit_cmd);
                ip=et_ip.getText().toString().trim();
                port=Integer.parseInt(et_port.getText().toString());
                //存入application类
                AppValues  appValues=(AppValues)getApplication();
                appValues.setIp(ip);
                appValues.setPort(port);

                String cmd=et_cmd.getText().toString().trim();
                CmdClientSocket cmdClient=new CmdClientSocket(ip,port,srfhandler);
                cmdClient.work(cmd);
            }
        });

        //点击文件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                NetFileData fileData = (NetFileData) arg0.getItemAtPosition(arg2);
                String pwd=fileData.getFilePath();
                String filePath="";
                if(pwd.endsWith("/")|pwd.endsWith("\\")){
                    //文件路径可能带"/"结尾，例如"c://aaa/b/"也可能是"c://aaa/b"因此需要考虑周全
                    //另外Windows系统和Linux系统文件夹分隔符不同，对有些系统其文件目录的表示是"c:\\\\aaa\\b\\"，注意"\\"转义成"\"
                    filePath=pwd+fileData.getFileName();
                }else{
                    filePath=pwd+ File.separator+fileData.getFileName();
                }

                if(fileData.getFileType()>=1){

                    if(fileData.getFileName().equals("...")){
                        //处理根目录，列出所有盘符
                        filePath="...";
                    }
                    ShowRemoteFileHandler showRemoteFileHandler = new ShowRemoteFileHandler(MainActivity.this, listView,recyclerView);//会更新ListView的句柄
                    CmdClientSocket cmdClientSocket = new CmdClientSocket(ip, port,showRemoteFileHandler);
                    cmdClientSocket.work("dir:"+filePath);

                }else{
                    ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler = new ShowNonUiUpdateCmdHandler(MainActivity.this);//直接Toast显示的句柄，不更新ListView
                    CmdClientSocket cmdClientSocket = new CmdClientSocket(ip, port,showNonUiUpdateCmdHandler);
                    cmdClientSocket.work("opn:"+filePath);

                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.optionmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_showarea:
              hidearea.setVisibility(View.VISIBLE);
            case R.id.menu_mouse_pad:

                Intent intent = new Intent(MainActivity.this,MousePadActivity.class);
                startActivity(intent);
//                getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment,new MousePadFragment(MainActivity.this ))
//                    .addToBackStack(null)
//                    .commit();
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.file_list_context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos=contextMenuInfo.position;
        NetFileData netFileData=(NetFileData) listView.getAdapter().getItem(pos);//其中listView为显示文件列表的视图
        switch(item.getItemId()){
            case R.id.HotKeyDialog:// 弹出热键对话框
                showHotKeyDialog(netFileData);//能根据netFileData类型决定弹出相应的热键对话框
                break;
            default :break;
        }
        return super.onContextItemSelected(item);
    }

    private void showHotKeyDialog(NetFileData netFileData) {
        ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler = new ShowNonUiUpdateCmdHandler(MainActivity.this);
        CmdClientSocket cmdClientSocket = new CmdClientSocket(ip, port,showNonUiUpdateCmdHandler);
        new HotKeyDialog(MainActivity.this,HotKeyGenerator.getHotkeyList(netFileData) , "热键操作表", cmdClientSocket).show();
    }
}
