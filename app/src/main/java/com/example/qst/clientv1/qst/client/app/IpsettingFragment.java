package com.example.qst.clientv1.qst.client.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qst.clientv1.R;
import com.example.qst.clientv1.qst.client.data.AppValues;

/**
 * author: 钱苏涛
 * created on: 2019/3/23 17:55
 * description:
 */
public class IpsettingFragment extends Fragment {
    AppValues appValues;
    String ip;
    int port;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.layout_ipsetting, container, false);
       appValues=(AppValues)getContext().getApplicationContext();
       appValues.loadData(getContext());
       ip=appValues.getIp();
       port=appValues.getPort();
       final EditText et_ip=view.findViewById(R.id.ip_ip);
       final EditText et_port=view.findViewById(R.id.ip_port);
       Button  btconnect=view.findViewById(R.id.ip_connect);
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
               Toast.makeText(getContext(),"设置成功",Toast.LENGTH_SHORT).show();
           }
       });
       return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        appValues.saveData(getContext());
    }
}
