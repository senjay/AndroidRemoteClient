package com.example.qst.clientv1.qst.client.view;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qst.clientv1.R;


/**
 * author: 钱苏涛
 * created on: 2019/3/2 16:37
 * description:
 */
public class FilePathAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {
    public interface PathOnItemClickLisenter{
        void OnClick(String path);
    }
    Context context;
    public String[] datas = null;
    private int thisPosition;
    private PathOnItemClickLisenter onItemClickLisenter;
    public FilePathAdapter(Context context, String path) {

        this.context = context;
        dealPath(path);
    }

    public void setOnItemClickLisenter(PathOnItemClickLisenter onItemClickLisenter) {
        this.onItemClickLisenter = onItemClickLisenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_filepath_item, viewGroup, false);
        RecViewViewHolder viewViewHolder = new RecViewViewHolder(view);
        return viewViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final int position=i;
        RecViewViewHolder viewViewHolder = (RecViewViewHolder) viewHolder;
        viewViewHolder.mTextView.setText(datas[position]+"\\");

        if (onItemClickLisenter != null) {

            viewViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String fullpath="";
                    for(int i=0;i<=position;i++)
                    {
                        fullpath+=datas[i]+"\\";
                    }
                    onItemClickLisenter.OnClick(fullpath);
                }
            });

        }
    }
    private void dealPath(String path)
    {
        datas=path.split("\\\\");
    }
    @Override
    public int getItemCount() {
        return datas != null ? datas.length : 0;
    }
    public int getthisPosition() {
        return thisPosition;
    }
    public void setThisPosition(int thisPosition) {
        this.thisPosition = thisPosition;
    }

    public static class RecViewViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public RecViewViewHolder(View view) {
            super(view);
            mTextView = view.findViewById(R.id.pathstr);
        }
    }
}
