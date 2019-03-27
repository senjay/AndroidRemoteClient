package com.example.qst.clientv1.qst.client.socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * author: 钱苏涛
 * created on: 2019/3/25 22:27
 * description:
 */
public class FileDownLoadSocketThread extends Thread{
    private String ip;
    private int port;
    private int time_out=10000;
    private Handler handler;//此handler处理进度条进程
    private Socket socket;
    private File file;
    public static final String PROCESS_HANDLER_RATE="pgrate";
    public static final String PROCESS_HANDLER_NET_SPEED="netspeed";
    private long filesize;
    public static final double NANOS_PER_SECOND = 1000000000.0;  //1秒=10亿nanoseconds
    public static final double BYTES_PER_MIB = 1024 * 1024;    //1M=1024*1024byte
    private boolean isPause=false;//是否暂停

    public FileDownLoadSocketThread(String ip, int port, Handler handler, File file, long filesize) {
        this.ip = ip;
        this.port = port;
        this.handler = handler;
        this.file = file;
        this.filesize = filesize;
    }
    private void connect() throws IOException {
        InetSocketAddress address=new InetSocketAddress(ip,port);
        socket=new Socket();
        socket.connect(address,time_out);
    }

    private void downloadFileSocket() throws IOException {
        byte[] inputByte =new byte[1024];
        int length = 0;
        DataInputStream din = new DataInputStream(socket.getInputStream());
        FileOutputStream fout=null;
       //String name= din.readUTF();//测试名字

        if(!file.exists())
        {
            file.createNewFile();
            fout =new FileOutputStream(file);
        }
        else {
            //若存在，以追加方式写入
            fout=new FileOutputStream(file,true);
        }
        long  abspgrate=file.length();
        long last_abspgrate=file.length();
        long last_precent=(long)((abspgrate/(double)filesize)*100);
        long start = System.nanoTime();   //开始时间

        if(filesize==0||abspgrate==filesize)//若已经下载完毕或长度为0 直接结束,ispause置为true
        {
            Message message = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putInt(PROCESS_HANDLER_RATE,100);
            bundle.putDouble(PROCESS_HANDLER_NET_SPEED,-1);//-1即不更新speed
            message.setData(bundle);
            handler.sendMessage(message);
            isPause=true;
        }
        while (true) {
            if(isPause)
                break;
            if (din != null) {
                length = din.read(inputByte, 0, inputByte.length);
            }
            if (length == -1) {
                break;
            }
            fout.write(inputByte, 0, length);
            fout.flush();

            abspgrate+=length;
            double prcent=(abspgrate/(double)filesize)*100;//long 相除 记得先把分母转成double
            int precent_rate= (int)prcent;


            //如果进度不一样,则更新进度条
            if(precent_rate!=last_precent)
            {
                Message message = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putInt(PROCESS_HANDLER_RATE,precent_rate);
                bundle.putDouble(PROCESS_HANDLER_NET_SPEED,-1);//-1即不更新speed
                Log.d("downrate",precent_rate+"");
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
        if (fout != null)
            fout.close();
        if (din != null)
            din.close();
        if (socket != null)
            socket.close();
    }

    @Override
    public void run() {
        super.run();
        try {
            connect();
            downloadFileSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }
}
