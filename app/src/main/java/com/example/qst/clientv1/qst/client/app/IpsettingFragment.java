package com.example.qst.clientv1.qst.client.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.qst.clientv1.R;
import com.example.qst.clientv1.qst.client.data.AppValues;
import com.example.qst.clientv1.qst.client.sql.Ipmanager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * author: 钱苏涛
 * created on: 2019/3/23 17:55
 * description:
 */
public class IpsettingFragment extends Fragment {
    AppValues appValues;
    String ip;
    int port;
    EditText et_ip;
    Ipmanager ipmanager;
    Handler testconnecthandler;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.layout_ipsetting, container, false);
       appValues=(AppValues)getContext().getApplicationContext();
       appValues.loadData(getContext());
       ip=appValues.getIp();
       port=appValues.getPort();
       et_ip=view.findViewById(R.id.ip_ip);
       final EditText et_port=view.findViewById(R.id.ip_port);
       Button  btconnect=view.findViewById(R.id.ip_connect);

       ipmanager=new Ipmanager(getContext());
       testconnecthandler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int connectflag=msg.arg1;
                if (connectflag==1)
                    Toast.makeText(getContext(),"连接成功",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(),"连接失败",Toast.LENGTH_SHORT).show();

            }
        };

        et_ip.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getX() >= (et_ip.getWidth() - et_ip.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        showListPopulWindow();
                        et_ip.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.pull_up), null);
                        return true;
                    }
                }
                return false;
            }
        });


       et_ip.setText(ip);
       et_port.setText(port+"");
       btconnect.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ip=et_ip.getText().toString().trim();
               port=Integer.parseInt(et_port.getText().toString());
               //存入application类
               appValues.setIp(ip);
               appValues.setPort(port);
               appValues.saveData(getContext());
               ipmanager.saveip(appValues.getIp(),appValues.getPort()+"");
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       Message message = testconnecthandler.obtainMessage();
                       message.arg1=1;
                       try {
                           testIpconnect(ip,port);
                       } catch (IOException e) {
                           e.printStackTrace();
                           message.arg1=0;
                       }
                       testconnecthandler.sendMessage(message);
                   }
               }).start();

           }
       });
       return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        appValues.saveData(getContext());
        ipmanager.saveip(appValues.getIp(),appValues.getPort()+"");
    }

    private void showListPopulWindow() {
        final ListPopupWindow listPopupWindow;
        listPopupWindow = new ListPopupWindow(getContext());

        final ArrayList<String> ip_portlist=ipmanager.findall();

        listPopupWindow.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, ip_portlist));//用android内置布局，或设计自己的样式
        listPopupWindow.setAnchorView(et_ip);//以哪个控件为基准，在该处以mEditText为基准
        listPopupWindow.setModal(true);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {//设置项点击监听
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                et_ip.setText(ip_portlist.get(i));//把选择的选项内容展示在EditText上
                listPopupWindow.dismiss();//如果已经选择了，隐藏起来
            }
        });
        listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                et_ip.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.pull_down), null);
            }
        });

        listPopupWindow.show();//把ListPopWindow展示出来
    }
    private void testIpconnect(String ip,int port) throws IOException {
        InetSocketAddress address=new InetSocketAddress(ip,port);
        Socket socket=new Socket();
        socket.connect(address,1000);
        socket.close();
    }
}
