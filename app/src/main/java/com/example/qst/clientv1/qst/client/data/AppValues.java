package com.example.qst.clientv1.qst.client.data;

import android.app.Application;

/**
 * author: 钱苏涛
 * created on: 2019/3/9 19:45
 * description:
 */
public class AppValues extends Application {
    private  String ip;
    private  int port;

    @Override
    public void onCreate() {
        super.onCreate();
        ip="192.168.137.1";
        port=8019;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
