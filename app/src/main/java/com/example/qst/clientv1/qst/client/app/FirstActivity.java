package com.example.qst.clientv1.qst.client.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.qst.clientv1.R;
import com.example.qst.clientv1.qst.client.data.AppValues;
import com.example.qst.clientv1.qst.client.operator.FileTransferBeginHandler;
import com.example.qst.clientv1.qst.client.socket.CmdClientSocket;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

/**
 * author: 钱苏涛
 * created on: 2019/3/23 15:29
 * description:
 */
public class FirstActivity extends AppCompatActivity implements View.OnClickListener{
    SlidingMenu slidingMenu;
    ArrayList<Fragment>fragmentList=new ArrayList<Fragment>();
    MousePadFragment mousepad;
    RemoteFragment remote;
    IpsettingFragment ipsettingFragment;
    CmdFragment cmdFragment;
    FileUploadFragment fileUploadFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fragment);

        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        ipsettingFragment=new IpsettingFragment();
        fragmentList.add(ipsettingFragment);
        transaction.add(R.id.fragment,ipsettingFragment);
        transaction.show(ipsettingFragment);
        transaction.commit();
        initSlidingMenus();
    }


    @Override
    public void onClick(View v) {
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        switch (v.getId())
        {
            case R.id.bt_mouse_pad:

                if(mousepad==null) {//remoteSys为全局变量，Fragment remoteSys;定义
                    mousepad = new MousePadFragment();
                    fragmentList.add(mousepad);//fragmentList为全局变量，通过ArrayList<Fragment> fragmentList;
                    transaction.add(R.id.fragment,mousepad);
                }
                hideAllFragments(transaction);
                transaction.show(mousepad);
                transaction.commit();
                slidingMenu.toggle();
                break;
            case R.id.bt_remote_computer:
                if(remote==null) {//remoteSys为全局变量，Fragment remoteSys;定义
                    remote = new RemoteFragment();
                    fragmentList.add(remote);//fragmentList为全局变量，通过ArrayList<Fragment> fragmentList;
                    transaction.add(R.id.fragment,remote);
                }
                hideAllFragments(transaction);
                transaction.show(remote);
                transaction.commit();
                slidingMenu.toggle();
                break;
            case R.id.bt_ip_setting:
                if(ipsettingFragment==null) {//remoteSys为全局变量，Fragment remoteSys;定义
                    ipsettingFragment = new IpsettingFragment();
                    fragmentList.add(ipsettingFragment);//fragmentList为全局变量，通过ArrayList<Fragment> fragmentList;
                    transaction.add(R.id.fragment,ipsettingFragment);
                }
                hideAllFragments(transaction);
                transaction.show(ipsettingFragment);
                transaction.commit();
                slidingMenu.toggle();
                break;
            case R.id.bt_remote_cmd:
                if(cmdFragment==null) {//remoteSys为全局变量，Fragment remoteSys;定义
                    cmdFragment = new CmdFragment();
                    fragmentList.add(cmdFragment);//fragmentList为全局变量，通过ArrayList<Fragment> fragmentList;
                    transaction.add(R.id.fragment,cmdFragment);
                }
                hideAllFragments(transaction);
                transaction.show(cmdFragment);
                transaction.commit();
                slidingMenu.toggle();
                break;
            case R.id.bt_file_upload:
                    if(fileUploadFragment==null) {//remoteSys为全局变量，Fragment remoteSys;定义
                        fileUploadFragment = new FileUploadFragment();
                        fragmentList.add(fileUploadFragment);//fragmentList为全局变量，通过ArrayList<Fragment> fragmentList;
                        transaction.add(R.id.fragment,fileUploadFragment);
                    }
                    hideAllFragments(transaction);
                    transaction.show(fileUploadFragment);
                    transaction.commit();
                    slidingMenu.toggle();
        }
    }

    private void hideAllFragments(FragmentTransaction transaction){
        // 不隐藏其他fragment的话，帧上有很多fragment重叠在一起
        for (int i = 0; i <fragmentList.size(); i++) {
            Fragment fragment = fragmentList.get(i);
            if(fragment!=null){
                transaction.hide(fragment);
            }
        }
    }

    private void  initSlidingMenus()
    {
        slidingMenu = new SlidingMenu(FirstActivity.this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingMenu.setShadowDrawable(R.color.colorAccent);

        // 设置滑动菜单视图的宽度
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        slidingMenu.setFadeDegree(0.35f);

        slidingMenu.attachToActivity(FirstActivity.this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        slidingMenu.setMenu(R.layout.menu_layout);

        slidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
            public void onOpened() {

            }
        });
        //各种点击事件
        ViewListener();
    }

    private void ViewListener() {
        slidingMenu.findViewById(R.id.bt_mouse_pad).setOnClickListener(this);
        slidingMenu.findViewById(R.id.bt_remote_computer).setOnClickListener(this);
        slidingMenu.findViewById(R.id.bt_ip_setting).setOnClickListener(this);
        slidingMenu.findViewById(R.id.bt_remote_cmd).setOnClickListener(this);
        slidingMenu.findViewById(R.id.bt_file_upload).setOnClickListener(this);
    }
}
