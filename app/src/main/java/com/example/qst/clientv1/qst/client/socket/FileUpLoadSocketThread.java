package com.example.qst.clientv1.qst.client.socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import static com.example.qst.clientv1.qst.client.socket.FileDownLoadSocketThread.BYTES_PER_MIB;
import static com.example.qst.clientv1.qst.client.socket.FileDownLoadSocketThread.NANOS_PER_SECOND;
import static com.example.qst.clientv1.qst.client.socket.FileDownLoadSocketThread.PROCESS_HANDLER_NET_SPEED;
import static com.example.qst.clientv1.qst.client.socket.FileDownLoadSocketThread.PROCESS_HANDLER_RATE;

/**
 * author: 钱苏涛
 * created on: 2019/3/27 14:06
 * description:
 */
public class FileUpLoadSocketThread extends Thread {

    private  String ip;
    private  int port;
    private  Handler handler;
    private  File file;
    private  long filepos;
    private Socket socket;
    private int time_out=10000;
    private boolean isPause=false;//是否暂停

    public FileUpLoadSocketThread(String ip, int port, Handler handler, File file, long filepos) {
        this.ip = ip;
        this.port = port;
        this.handler = handler;
        this.file = file;
        this.filepos = filepos;
    }
    private void connect() throws IOException {
        InetSocketAddress address=new InetSocketAddress(ip,port);
        socket=new Socket();
        socket.connect(address,time_out);
    }
    private void uploadFileSocket() throws IOException {
        byte[] sendByte = new byte[1024];
        DataOutputStream dout=new DataOutputStream(socket.getOutputStream());
        FileInputStream fis = new FileInputStream(file);
        fis.skip(filepos);//跳过已经下载的长度

        long  abspgrate=filepos;
        long last_abspgrate=filepos;
        long last_precent=(long)((abspgrate/(double)file.length())*100);
        long start = System.nanoTime();   //开始时间
        if(file.length()==0||filepos==file.length())
        {
            Message message = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putInt(PROCESS_HANDLER_RATE,100);
            bundle.putDouble(PROCESS_HANDLER_NET_SPEED,-1);//-1即不更新speed
            message.setData(bundle);
            handler.sendMessage(message);
            isPause=true;
        }

        int length;
        while((length = fis.read(sendByte, 0, sendByte.length))>0){
            if(isPause)
                break;
            dout.write(sendByte,0,length);
            dout.flush();
            abspgrate+=length;
            double prcent=(abspgrate/(double)file.length())*100;//long 相除 记得先把分母转成double
            int precent_rate= (int)prcent;

            //如果进度不一样,则更新进度条
            if(precent_rate!=last_precent)
            {
                Message message = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putInt(PROCESS_HANDLER_RATE,precent_rate);
                bundle.putDouble(PROCESS_HANDLER_NET_SPEED,-1);//-1即不更新speed
                Log.d("uprate",precent_rate+"");
                message.setData(bundle);
                handler.sendMessage(message);
                last_precent=precent_rate;
            }
            //发送下载速度给pghandler,0.5s更新
            double second=((double)System.nanoTime()-start)/NANOS_PER_SECOND;
            if(second>0.5)
            {
                start=System.nanoTime();
                double speed = (abspgrate-last_abspgrate)/BYTES_PER_MIB/second;
                last_abspgrate=abspgrate;
                Message message = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putDouble(PROCESS_HANDLER_NET_SPEED,speed);
                bundle.putInt(PROCESS_HANDLER_RATE,-1);//-1即不更新rate
                message.setData(bundle);
                handler.sendMessage(message);
            }

        }
        dout.close();

    }
    @Override
    public void run() {
        super.run();
        try {
            connect();
            uploadFileSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }
}
