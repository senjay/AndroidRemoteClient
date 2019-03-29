package com.example.qst.clientv1.qst.client.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.qst.clientv1.R;
import com.example.qst.clientv1.qst.client.data.AppValues;
import com.example.qst.clientv1.qst.client.listen.MousePadOnGestureListener;
import com.example.qst.clientv1.qst.client.operator.ShowNonUiUpdateCmdHandler;
import com.example.qst.clientv1.qst.client.socket.CmdClientSocket;
import com.example.qst.clientv1.qst.client.sql.CmddataBase;

/**
 * author: 钱苏涛
 * created on: 2019/3/16 20:46
 * description:
 */
@SuppressLint("ValidFragment")
public class MousePadFragment extends Fragment {

    TextView mousepad;
    TextView mousewheel;
    Button mouseleft;
    Button mouseright;
    ToggleButton mouselock;
    Button bt_input;
    EditText et_input;
    Button bt_back;

    String ip;
    int port;
    private GestureDetector mGestureDetector;
    GestureDetector rolgestureDetector;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.layout_mouse_pad, container, false);
        AppValues appValues=(AppValues)getContext().getApplicationContext();
                //(AppValues)context.getApplicationContext();
        ip=appValues.getIp();
        port=appValues.getPort();
        mousepad=view.findViewById(R.id.mouse_pad);
        mouseleft=view.findViewById(R.id.mouse_left);
        mouseright=view.findViewById(R.id.mouse_right);
        mouselock=view.findViewById(R.id.mouse_lock);
        mousewheel=view.findViewById(R.id.mouse_wheel);
        bt_input=view.findViewById(R.id.pad_input_submit);
        et_input=view.findViewById(R.id.pad_input);
        bt_back=view.findViewById(R.id.back_space);
        mGestureDetector=new GestureDetector(getContext(), new MousePadOnGestureListener(getContext()));
        //这里rolgestureDetector 不能每次new新的出来调用
        rolgestureDetector=new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener(){
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
                ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler=new ShowNonUiUpdateCmdHandler(getContext());
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
                ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler=new ShowNonUiUpdateCmdHandler(getContext());
                CmdClientSocket cmdClientSocket = new CmdClientSocket(ip, port, showNonUiUpdateCmdHandler);
                cmdClientSocket.work(cmd);
            }
        });

        mouseright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cmd="clk:right";
                ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler=new ShowNonUiUpdateCmdHandler(getContext());
                CmdClientSocket cmdClientSocket = new CmdClientSocket(ip, port, showNonUiUpdateCmdHandler);
                cmdClientSocket.work(cmd);
            }
        });
        mouselock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    String cmd="clk:left_press";
                    ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler=new ShowNonUiUpdateCmdHandler(getContext());
                    CmdClientSocket cmdClientSocket = new CmdClientSocket(ip, port, showNonUiUpdateCmdHandler);
                    cmdClientSocket.work(cmd);
                }
                else {
                    String cmd="clk:left_release";
                    ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler=new ShowNonUiUpdateCmdHandler(getContext());
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
        bt_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputstring=et_input.getText().toString();
                ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler = new ShowNonUiUpdateCmdHandler(getContext());
                CmdClientSocket cmdClient = new CmdClientSocket(ip, port,showNonUiUpdateCmdHandler);
                cmdClient.work("cps:"+inputstring);
            }
        });
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler = new ShowNonUiUpdateCmdHandler(getContext());
                CmdClientSocket cmdClient = new CmdClientSocket(ip, port,showNonUiUpdateCmdHandler);
                cmdClient.work("key:vk_back_space");
            }
        });
        return  view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mouselock.setChecked(false);
    }
}
