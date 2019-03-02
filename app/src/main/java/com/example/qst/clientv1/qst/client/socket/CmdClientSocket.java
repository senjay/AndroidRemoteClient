package com.example.qst.clientv1.qst.client.socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class CmdClientSocket {
    private String ip;
    private int port;
    private ArrayList<String> cmd;
    private int time_out=10000;
    private Handler handler;
    private Socket socket;
    public static final String KEY_SERVER_ACK_MSG = "KEY_SERVER_ACK_MSG";
    public static int SERVER_MSG_OK=0;//用于发送给句柄的消息类型,放在消息的arg2中，表示服务端正常
    public static int SERVER_MSG_ERROR=1;//表示服务端出错
    private OutputStreamWriter writer;
    private BufferedReader bufferedReader;
    private int msgType;
    public CmdClientSocket(String ip, int port, Handler handler)
    {
        this.port = port;
        this.ip = ip;
        this.handler = handler;
    }
    private void connect() throws IOException {
        InetSocketAddress address=new InetSocketAddress(ip,port);
        socket=new Socket();
        socket.connect(address,time_out);

    }
    private void writeCmd(String cmd) throws IOException {
        BufferedOutputStream os=new BufferedOutputStream(socket.getOutputStream());
        writer=new OutputStreamWriter(os,"UTF-8");
        writer.write("1\n");
        writer.write(cmd+"\n");
        writer.flush();
    }
    private ArrayList<String> readSocketMsg() throws IOException {
        ArrayList<String> msgList=new ArrayList<>();
        InputStreamReader isr=new InputStreamReader(socket.getInputStream(),"UTF-8");
        bufferedReader=new BufferedReader(isr);
        String numStr = bufferedReader.readLine();
        int linNum = Integer.parseInt(numStr);
        for (int i = 0; i <linNum ; i++) {
            String s = bufferedReader.readLine();
            msgList.add(s);
        }
        return msgList;
    }
    private void close() throws IOException {
        bufferedReader.close();
        writer.close();
        socket.close();
    }
    private void doCmdTask(String cmd){
        ArrayList<String> msgList=new ArrayList<>();
        try {
            connect();
            writeCmd(cmd);
            msgList = readSocketMsg();
            if(msgList.get(0).equalsIgnoreCase("ok"))
                msgType=SERVER_MSG_OK;
            else
                msgType=SERVER_MSG_ERROR;
            close();
        } catch (IOException e) {
            msgType=SERVER_MSG_ERROR;
            msgList.add(e.toString());
            e.printStackTrace();
        }
        Message message = handler.obtainMessage();
        Bundle bundle = new Bundle();
        message.arg2=msgType;
        bundle.putStringArrayList(KEY_SERVER_ACK_MSG,msgList);
        message.setData(bundle);
        handler.sendMessage(message);

    }
    public void work(final String cmd){
        new Thread(new Runnable() {
            @Override
            public void run() {
                doCmdTask(cmd);
            }
        }).start();
    }
}
