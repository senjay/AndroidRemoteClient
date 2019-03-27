package com.example.qst.clientv1.qst.client.operator;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.qst.clientv1.qst.client.data.AppValues;
import com.example.qst.clientv1.qst.client.listen.FileloadPauseListener;
import com.example.qst.clientv1.qst.client.socket.CmdClientSocket;
import com.example.qst.clientv1.qst.client.socket.FileDownLoadSocketThread;
import com.example.qst.clientv1.qst.client.socket.FileUpLoadSocketThread;
import com.example.qst.clientv1.qst.client.view.FileProgressDialog;

import java.io.File;

import java.util.ArrayList;

/**
 * author: 钱苏涛
 * created on: 2019/3/25 22:26
 * description:
 */
public class FileTransferBeginHandler extends Handler {

    private  Context context;
    private String  filename;
    private String ip;
    public FileTransferBeginHandler(Context context,String filename) {
        super();
        this.context = context;
        this.filename=filename;
        AppValues appValues= (AppValues) context.getApplicationContext();
        ip=appValues.getIp();
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        ArrayList<String> ll=getDownloadFileportandlength(msg);
        if(ll.get(0).equals("dlf"))
        {
            int port=Integer.parseInt(ll.get(1));
            long fileSize=Long.parseLong(ll.get(2));//服务端文件大小,为了做百分比进度条

            String path=CheckLocalDownloadFolder.check();
            File clientfile=new File(path+"/"+filename);

            FileProgressDialog fileProgressDialog=new FileProgressDialog(context,filename,"下载");
            fileProgressDialog.show();//此句必须在下句前面,因为pghanlder在这里new出来
            Handler pghandler=fileProgressDialog.getHandler();//得到一个进度条handler传送下载进度,并更新进度

            final FileDownLoadSocketThread fileDownLoadSocketThread=new FileDownLoadSocketThread(ip,port,pghandler,clientfile,fileSize);
            fileDownLoadSocketThread.start();//开启文件下载线程
            //暂停下载接口,把暂停flag传到pgDialog
            fileProgressDialog.setFiledownloadpauseListener(new FileloadPauseListener() {
                @Override
                public void closeFileloadsocket() {
                    fileDownLoadSocketThread.setPause(true);
                }
            });
        }
        else if(ll.get(0).equals("ulf"))
        {
            int port=Integer.parseInt(ll.get(1));
            long filepos=Long.parseLong(ll.get(2));
            File file=new File(filename);//此为手机本地文件,传进来的自带路径

            FileProgressDialog fileProgressDialog=new FileProgressDialog(context,filename,"上传");
            fileProgressDialog.show();
            Handler pghandler=fileProgressDialog.getHandler();

            final FileUpLoadSocketThread fileUpLoadSocketThread=new FileUpLoadSocketThread(ip,port,pghandler,file,filepos);
            fileUpLoadSocketThread.start();
            fileProgressDialog.setFiledownloadpauseListener(new FileloadPauseListener() {
                @Override
                public void closeFileloadsocket() {
                    fileUpLoadSocketThread.setPause(true);
                }
            });

        }


    }
    private ArrayList<String> getDownloadFileportandlength(Message msg)
    {
        Bundle bundle =msg.getData();
        ArrayList<String> list =bundle.getStringArrayList(CmdClientSocket.KEY_SERVER_ACK_MSG);
        int status=msg.arg2;
        if(status==CmdClientSocket.SERVER_MSG_OK)
        {
            if(list.size()==4 && list.get(1).equals("dlf"))//发送port给文件下载handler进行下载操作
            {
                ArrayList<String> ll=new ArrayList<String>();
                ll.add(list.get(1));//方式
                ll.add(list.get(2));//端口
                ll.add(list.get(3));//文件长度
                return ll;
            }
            else if(list.size()==4 && list.get(1).equals("ulf"))
            {
                ArrayList<String> ll=new ArrayList<String>();
                ll.add(list.get(1));//方式
                ll.add(list.get(2));//端口
                ll.add(list.get(3));//文件pos
                return ll;
            }
        }
        return null;//null即传回端口文件长度失败
    }


}
