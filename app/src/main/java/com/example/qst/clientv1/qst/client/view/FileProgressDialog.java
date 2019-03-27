package com.example.qst.clientv1.qst.client.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.qst.clientv1.qst.client.listen.FileloadPauseListener;

import static com.example.qst.clientv1.qst.client.socket.FileDownLoadSocketThread.PROCESS_HANDLER_NET_SPEED;
import static com.example.qst.clientv1.qst.client.socket.FileDownLoadSocketThread.PROCESS_HANDLER_RATE;

/**
 * author: 钱苏涛
 * created on: 2019/3/25 22:50
 * description:
 */
public class FileProgressDialog {
    private Handler handler;
    private String title;
    private String filename;
    private ProgressDialog pg;
    private Context context;
    FileloadPauseListener fileloadpauseListener;
    // ProgressDialog的使用 http://www.cnblogs.com/guop/p/5139937.html
    public FileProgressDialog(Context context,String filename,String title) {
        super();
        this.context=context;
        this.filename=filename;
        this.title = title;

    }
    private void init()
    {
        pg = new ProgressDialog(context);
        pg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        pg.setCancelable(true);// 设置是否可以通过点击Back键取消
        pg.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        // dialog.setIcon(R.drawable.ic_launcher);// 设置提示的title的图标，默认是没有的
        pg.setTitle(title);
        pg.setMax(100);
        pg.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });
        pg.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        fileloadpauseListener.closeFileloadsocket();
                    }
                });
        pg.setButton(DialogInterface.BUTTON_NEUTRAL, "暂停",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        fileloadpauseListener.closeFileloadsocket();
                    }
                });
        pg.setMessage("0.00MB/s");
        pg.show();
    }
    public  void show()
    {
        init();
        handler=new Handler(){
            @Override
            public void handleMessage(final Message msg) {
                super.handleMessage(msg);
                Bundle bundle =msg.getData();
                int rate=bundle.getInt(PROCESS_HANDLER_RATE);
                if(rate!=-1)
                    pg.setProgress(rate);//pg.incrementProgressBy(1);
                double speed=bundle.getDouble(PROCESS_HANDLER_NET_SPEED);
                if(speed!=-1)
                    pg.setMessage(String.format("%.2fMB/s",speed));
                if(rate==100)
                {
                    pg.dismiss();
                    Toast.makeText(context,filename+title+"完成",Toast.LENGTH_SHORT).show();
                }
        }
        };

    }

    public void setFiledownloadpauseListener(FileloadPauseListener fileloadpauseListener) {
        this.fileloadpauseListener = fileloadpauseListener;
    }

    public Handler getHandler() {
        return handler;
    }
}
