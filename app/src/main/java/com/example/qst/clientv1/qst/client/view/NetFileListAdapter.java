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
        if(position==0||position==1)
        {
            fiv.setImageResource(R.drawable.drive);
            fname.setText(netFileList.get(position).getFileName());
            fsize.setText("");
            fdate.setText("");
            return convertView;
        }
        String date=netFileList.get(position).getFileModifiedDate();//为了使盘符不显示日期
        if(netFileList.get(position).getFileType()==1)
            fiv.setImageResource(R.drawable.folder);
        else if(netFileList.get(position).getFileType()==0)
            fiv.setImageResource(R.drawable.doc);
        else if(netFileList.get(position).getFileType()==2)
        {
            fiv.setImageResource(R.drawable.drive);
            date="";
        }
        fname.setText(netFileList.get(position).getFileName());
        fdate.setText(date);
        fsize.setText(netFileList.get(position).getFileSizeStr());
        return convertView;
    }

    @Override
    public int getCount() {
        return netFileList.size();
    }
}
