package com.example.qst.clientv1.qst.client.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qst.clientv1.R;
import com.example.qst.clientv1.qst.client.data.AppValues;
import com.example.qst.clientv1.qst.client.data.NetFileData;
import com.example.qst.clientv1.qst.client.operator.CheckLocalDownloadFolder;
import com.example.qst.clientv1.qst.client.operator.FileTransferBeginHandler;
import com.example.qst.clientv1.qst.client.operator.HotKeyGenerator;
import com.example.qst.clientv1.qst.client.operator.ShowNonUiUpdateCmdHandler;
import com.example.qst.clientv1.qst.client.operator.ShowRemoteFileHandler;
import com.example.qst.clientv1.qst.client.socket.CmdClientSocket;
import com.example.qst.clientv1.qst.client.view.HotKeyDialog;

import java.io.File;

/**
 * author: 钱苏涛
 * created on: 2019/3/23 13:09
 * description:
 */
public class MainFragment extends Fragment {
    Handler handler;
    String ip;
    int port;
    ShowRemoteFileHandler srfhandler;
    ListView listView;
    View hidearea;
    RecyclerView recyclerView;
    AppValues  appValues;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.main_layout, container, false);
        //读取存储的

        appValues=(AppValues)getContext().getApplicationContext();
        ip=appValues.getIp();
        port=appValues.getPort();
        listView=view.findViewById(R.id.filelist);
        registerForContextMenu(listView);//注册上下文菜单
        //Button hidebt=view.findViewById(R.id.hide);
        //hidearea=view.findViewById(R.id.hidearea);
      // Button submitbt=view.findViewById(R.id.submit);
        recyclerView=view.findViewById(R.id.recyclerview);
        srfhandler=new ShowRemoteFileHandler(getContext(),listView,recyclerView);
        CmdClientSocket cmdClient=new CmdClientSocket(ip,port,srfhandler);
        cmdClient.work("dir:...");
        //隐藏输入区域

//        hidebt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                hidearea.setVisibility(View.GONE);
//            }
//        });

        //点击提交命令
//        submitbt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditText et_ip=view.findViewById(R.id.edit_ip);
//                EditText et_port=view.findViewById(R.id.edit_port);
//                EditText et_cmd=view.findViewById(R.id.edit_cmd);
//                ip=et_ip.getText().toString().trim();
//                port=Integer.parseInt(et_port.getText().toString());
//                //存入application类
//                appValues.setIp(ip);
//                appValues.setPort(port);
//
//                String cmd=et_cmd.getText().toString().trim();
//                CmdClientSocket cmdClient=new CmdClientSocket(ip,port,srfhandler);
//                cmdClient.work("dir:...");
//            }
//        });

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
                    ShowRemoteFileHandler showRemoteFileHandler = new ShowRemoteFileHandler(getContext(), listView,recyclerView);//会更新ListView的句柄
                    CmdClientSocket cmdClientSocket = new CmdClientSocket(ip, port,showRemoteFileHandler);
                    cmdClientSocket.work("dir:"+filePath);

                }else{
                    ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler = new ShowNonUiUpdateCmdHandler(getContext());//直接Toast显示的句柄，不更新ListView
                    CmdClientSocket cmdClientSocket = new CmdClientSocket(ip, port,showNonUiUpdateCmdHandler);
                    cmdClientSocket.work("opn:"+filePath);

                }

            }
        });
        return  view;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        new MenuInflater(getContext()).inflate(R.menu.file_list_context_menu,menu);
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
            case R.id.downloadfile:
                //发送文件下载命令
                /**
                 * 1.检验本地是否存在该文件
                 * 2.若存在通过命令socket发送文件路径+本地该文件长度，否则发送发送文件路径+长度0
                 * 3.服务器接受命令，跳过skip客户端发送来的长度,发送文件byte，文件下载socket端口给客户端（服务器开辟新线程）
                 * 4.客户端通过FileTransferBeginHandler接受到端口号，若是本地该文件存在则追加写入，否则创建文件写入（客户端开辟新线程FileDownLoadSocketThread）
                 */
                if(netFileData.getFileType()==0)
                {
                    long filepos=CheckLocalDownloadFolder.isFileExists(netFileData.getFileName());
                    FileTransferBeginHandler fileTransferBeginHandler=new FileTransferBeginHandler(getContext(),netFileData.getFileName());
                    new CmdClientSocket(ip,port,fileTransferBeginHandler).work("dlf:"+netFileData.getFilePath()+"\\"+netFileData.getFileName()+"?"+filepos);
                }
                else
                {
                    Toast.makeText(getContext(),"暂未实现整个文件夹下载功能",Toast.LENGTH_SHORT).show();
                }
                break;
            default :break;
        }
        return super.onContextItemSelected(item);
    }

    private void showHotKeyDialog(NetFileData netFileData) {
        ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler = new ShowNonUiUpdateCmdHandler(getContext());
        CmdClientSocket cmdClientSocket = new CmdClientSocket(ip, port,showNonUiUpdateCmdHandler);
        new HotKeyDialog(getContext(), HotKeyGenerator.getHotkeyList(netFileData) , "热键操作表", cmdClientSocket).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        appValues.saveData(getContext());
    }
}
