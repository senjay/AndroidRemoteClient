package com.example.qst.clientv1.qst.client.listen;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.qst.clientv1.qst.client.data.AppValues;
import com.example.qst.clientv1.qst.client.operator.ShowNonUiUpdateCmdHandler;
import com.example.qst.clientv1.qst.client.socket.CmdClientSocket;

/**
 * author: 钱苏涛
 * created on: 2019/3/16 16:28
 * description:
 */
public class MousePadOnGestureListener extends GestureDetector.SimpleOnGestureListener {

    private Context context;
    String ip;
    int port;
    public MousePadOnGestureListener(Context context) {
        super();
        this.context=context;
        AppValues appValues=(AppValues)context.getApplicationContext();
        this.ip=appValues.getIp();
        this.port=appValues.getPort();
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        String cmd="clk:left";
        ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler=new ShowNonUiUpdateCmdHandler(context);
        CmdClientSocket cmdClientSocket = new CmdClientSocket(ip, port, showNonUiUpdateCmdHandler);
        cmdClientSocket.work(cmd);
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d("disx:",""+distanceX);
        Log.d("disy:",""+distanceY);
        String cmd="mov:"+(int)-distanceX+","+(int)-distanceY;//手势方向与鼠标控制方向相反，对值取反
        ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler=new ShowNonUiUpdateCmdHandler(context);
        CmdClientSocket cmdClientSocket = new CmdClientSocket(ip, port, showNonUiUpdateCmdHandler);
        cmdClientSocket.work(cmd);
        return true;
    }
}
