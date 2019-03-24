package com.example.qst.clientv1.qst.client.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.qst.clientv1.R;
import com.example.qst.clientv1.qst.client.data.HotKeyData;
import com.example.qst.clientv1.qst.client.socket.CmdClientSocket;

import java.util.List;

/**
 * author: 钱苏涛
 * created on: 2019/3/9 13:46
 * description:
 */
public class HotKeyGridAdapter extends ArrayAdapter {
    Context context;
    List<HotKeyData> list;
    CmdClientSocket cmdClientSocket;

    public HotKeyGridAdapter(Context context, List<HotKeyData> list, CmdClientSocket cmdClientSocket)
    {
        super(context,android.R.layout.simple_list_item_1,list);
        this.context=context;
        this.list=list;
        this.cmdClientSocket=cmdClientSocket;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_hotkey_item,null,false);
        }
        final TextView keyname=convertView.findViewById(R.id.hotkeyname);
        keyname.setText(list.get(position).getHotkeyName());
        keyname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送热键命

                cmdClientSocket.work(list.get(position).getHotkeyCmd());

            }
        });
        return  convertView;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
