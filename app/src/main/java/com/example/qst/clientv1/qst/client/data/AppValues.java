package com.example.qst.clientv1.qst.client.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * author: 钱苏涛
 * created on: 2019/3/9 19:45
 * description:
 */
public class AppValues extends Application {
    private  String ip;
    private  int port;
    public static final String KEY_IP="IP";
    public static final String KEY_PORT="port";
    @Override
    public void onCreate() {
        super.onCreate();

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

    public  void saveData(Context context){//通过SharedPreferences存数据
        //需要上下文传递进来，通过上下文来获得SharedPreferences对象
        SharedPreferences sp=context.getSharedPreferences(context.getClass().getCanonicalName(), Context.MODE_PRIVATE) ;
        //sharedPreferences第一个参数是字符串，对应一个xml文件的文件名称，这里干脆用context对应类的名称
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_IP,ip );//KEY_IP,KEY_PORT等为自定义String类型常量
        editor.putInt(KEY_PORT, port);
        editor.commit();

    }
    public  void loadData(Context context){//通过SharedPreferences取数据
        //需要上下文传递进来，通过上下文来获得SharedPreferences对象
        SharedPreferences sp=context.getSharedPreferences(context.getClass().getCanonicalName(), Context.MODE_PRIVATE) ;
        //sharedPreferences第一个参数是字符串，对应一个xml文件的文件名称，这里干脆用context对应类的名称
        ip = sp.getString(KEY_IP, "192.168.137.1");
        port = sp.getInt(KEY_PORT, 8019);

    }

}
