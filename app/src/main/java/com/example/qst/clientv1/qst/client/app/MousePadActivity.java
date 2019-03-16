package com.example.qst.clientv1.qst.client.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.qst.clientv1.R;
import com.example.qst.clientv1.qst.client.data.AppValues;
import com.example.qst.clientv1.qst.client.listen.MousePadOnGestureListener;
import com.example.qst.clientv1.qst.client.operator.ShowNonUiUpdateCmdHandler;
import com.example.qst.clientv1.qst.client.socket.CmdClientSocket;

/**
 * author: 钱苏涛
 * created on: 2019/3/16 15:58
 * description:
 */
public class MousePadActivity extends AppCompatActivity {
    TextView mousepad;
    TextView mousewheel;
    Button mouseleft;
    Button mouseright;
    ToggleButton mouselock;
    String ip;
    int port;
    private GestureDetector mGestureDetector;
    GestureDetector rolgestureDetector;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppValues appValues=(AppValues)getApplication();
        ip=appValues.getIp();
        port=appValues.getPort();
        setContentView(R.layout.layout_mouse_pad);
        mousepad=findViewById(R.id.mouse_pad);
        mouseleft=findViewById(R.id.mouse_left);
        mouseright=findViewById(R.id.mouse_right);
        mouselock=findViewById(R.id.mouse_lock);
        mousewheel=findViewById(R.id.mouse_wheel);
        mGestureDetector=new GestureDetector(this, new MousePadOnGestureListener(MousePadActivity.this));
        //这里rolgestureDetector 不能每次new新的出来调用
        rolgestureDetector=new GestureDetector(MousePadActivity.this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                String cmd="rol:";
                Log.d("disyyy:",""+distanceY);
                if (distanceY<0)
                {
                    cmd+="-1";
                }
                else {
                    cmd+="1";
                }
                ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler=new ShowNonUiUpdateCmdHandler(MousePadActivity.this);
                CmdClientSocket cmdClientSocket = new CmdClientSocket(ip, port, showNonUiUpdateCmdHandler);
                cmdClientSocket.work(cmd);
                return true;
            }
        });

        mousepad.setOnTouchListener(new TextView.OnTouchListener(){
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               // TODO Auto-generated method stub
               mGestureDetector.onTouchEvent(event);
               return true;
           }
       });

        mouseleft.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String cmd="clk:left";
               ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler=new ShowNonUiUpdateCmdHandler(MousePadActivity.this);
               CmdClientSocket cmdClientSocket = new CmdClientSocket(ip, port, showNonUiUpdateCmdHandler);
               cmdClientSocket.work(cmd);
           }
        });

        mouseright.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String cmd="clk:right";
               ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler=new ShowNonUiUpdateCmdHandler(MousePadActivity.this);
               CmdClientSocket cmdClientSocket = new CmdClientSocket(ip, port, showNonUiUpdateCmdHandler);
               cmdClientSocket.work(cmd);
           }
         });
        mouselock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (isChecked){
                   String cmd="clk:left_press";
                   ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler=new ShowNonUiUpdateCmdHandler(MousePadActivity.this);
                   CmdClientSocket cmdClientSocket = new CmdClientSocket(ip, port, showNonUiUpdateCmdHandler);
                   cmdClientSocket.work(cmd);
               }
               else {
                   String cmd="clk:left_release";
                   ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler=new ShowNonUiUpdateCmdHandler(MousePadActivity.this);
                   CmdClientSocket cmdClientSocket = new CmdClientSocket(ip, port, showNonUiUpdateCmdHandler);
                   cmdClientSocket.work(cmd);
               }
           }
        });

        mousewheel.setOnTouchListener(new TextView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rolgestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }
}
