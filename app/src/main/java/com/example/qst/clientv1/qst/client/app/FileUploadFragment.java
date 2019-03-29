package com.example.qst.clientv1.qst.client.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.qst.clientv1.R;
import com.example.qst.clientv1.qst.client.data.AppValues;
import com.example.qst.clientv1.qst.client.operator.FileTransferBeginHandler;
import com.example.qst.clientv1.qst.client.socket.CmdClientSocket;

import static android.app.Activity.RESULT_OK;

/**
 * author: 钱苏涛
 * created on: 2019/3/28 23:13
 * description:
 */
public class FileUploadFragment extends Fragment {
    Switch uploadmode;
    Button selectfile;
    int mode=1;//默认续传1，否则-1
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.layout_fileupload, container, false);
        uploadmode=view.findViewById(R.id.switch_upload_mode);
        selectfile=view.findViewById(R.id.bt_selectfile_upload);
        uploadmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    mode=1;
                else
                    mode=-1;
            }
        });
        selectfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,0);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            Uri uri=data.getData();
            String path=uri.getPath();
            String filename=path.substring(path.lastIndexOf("/")+1);
            Toast.makeText(getContext(),filename,Toast.LENGTH_SHORT).show();
            AppValues appValues=(AppValues)getContext().getApplicationContext();
            String ip=appValues.getIp();
            int port=appValues.getPort();
            path=path.replace("/external_files", Environment.getExternalStorageDirectory().getAbsolutePath());//变换一下路径
            FileTransferBeginHandler fileTransferBeginHandler=new FileTransferBeginHandler(getContext(),path);
            new CmdClientSocket(ip,port,fileTransferBeginHandler).work("ulf:"+"G:\\android_server_download\\"+filename+"?"+mode);//(-1)为非续传模式
        }
    }
}
