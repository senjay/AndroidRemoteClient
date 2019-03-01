package com.example.qst.clientv1.qst.client.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qst.clientv1.R;
import com.example.qst.clientv1.qst.client.data.NetFileData;

import java.util.ArrayList;

public class NetFileListAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<NetFileData> netFileList;
    public NetFileListAdapter(Context context, ArrayList<NetFileData> netFileList)
    {
        super(context,android.R.layout.simple_list_item_1,netFileList);
        this.context=context;
        this.netFileList=netFileList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_listview_item,null,false);
        }
        ImageView fiv=convertView.findViewById(R.id.fileimage);
        TextView fname=convertView.findViewById(R.id.filename);
        TextView fdate=convertView.findViewById(R.id.filedate);
        TextView fsize=convertView.findViewById(R.id.filesize);
        if(netFileList.get(position).isDirectory())
            fiv.setImageResource(R.drawable.folder);
        else
            fiv.setImageResource(R.drawable.doc);
        fname.setText(netFileList.get(position).getFileName());
        fdate.setText(netFileList.get(position).getFileModifiedDate());
        fsize.setText(netFileList.get(position).getFileSizeStr());
        return convertView;
    }
}
