package com.example.qst.clientv1.qst.client.app;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.qst.clientv1.R;
import com.example.qst.clientv1.qst.client.data.AppValues;
import com.example.qst.clientv1.qst.client.operator.ShowNonUiUpdateCmdHandler;
import com.example.qst.clientv1.qst.client.socket.CmdClientSocket;
import com.example.qst.clientv1.qst.client.sql.CmddataBase;
import com.example.qst.clientv1.qst.client.sql.Cmdmanager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * author: 钱苏涛
 * created on: 2019/3/23 18:42
 * description:
 */
public class CmdFragment extends Fragment {
    EditText et_cmd;
    Button submit;
    Button save_bt;
    String ip;
    int port;
    Cmdmanager cmdmanager;
    SwipeMenuListView swipeMenuListView;
    SimpleAdapter adapter;
    ArrayList<HashMap<String,String>>list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.layout_cmd, container, false);
        et_cmd=view.findViewById(R.id.edit_cmd);
        submit=view.findViewById(R.id.submit_cmd);
        AppValues appValues=(AppValues)getContext().getApplicationContext();
        ip=appValues.getIp();
        port=appValues.getPort();
        swipeMenuListView=view.findViewById(R.id.cmd_listview);
        save_bt=view.findViewById(R.id.cmd_save);
        cmdmanager=new Cmdmanager(getContext());
        ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler = new ShowNonUiUpdateCmdHandler(getContext());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler = new ShowNonUiUpdateCmdHandler(getContext());
                CmdClientSocket cmdClient = new CmdClientSocket(ip, port,showNonUiUpdateCmdHandler);
                String cmd=et_cmd.getText().toString().trim();
                String []cmdlist=cmd.split("\n");
                cmdClient.work(cmdlist);

            }
        });
        save_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSaveDialog();
            }
        });
        list=new ArrayList<HashMap<String, String>>();
        list=cmdmanager.findAll(list);
        adapter=new SimpleAdapter(getContext(),list,R.layout.layout_cmdlistview_item,
                new String[]{CmddataBase.KEY_NAME,CmddataBase.KEY_CMD},
                   new int[]{R.id.item_cmd_name,R.id.item_cmd_content});
        swipeMenuListView.setMenuCreator(createSwipeMenulistvieitem());
        swipeMenuListView.setAdapter(adapter);
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index)
                {
                    case 0:
                        showEditDialog(position);
                        break;
                    case 1:
                        cmdmanager.delete(list.get(position).get(CmddataBase.KEY_ID));
                        notifyAdapter();
                        break;
                }
                return false;
            }
        });
        swipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler = new ShowNonUiUpdateCmdHandler(getContext());
                CmdClientSocket cmdClient = new CmdClientSocket(ip, port,showNonUiUpdateCmdHandler);
                String cmd=list.get(position).get(CmddataBase.KEY_CMD);
                String []cmdlist=cmd.split("\n");
                cmdClient.work(cmdlist);
            }
        });
        return view;
    }

    /**
     * 将dp转换成px
     */
    public  int dp2px(float dpValue){
        final float scale = getContext().getResources ().getDisplayMetrics ().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void showSaveDialog() {
        final EditText edit_name = new EditText(getContext());
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(getContext());
        inputDialog.setTitle("保存").setView(edit_name);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String cmd=et_cmd.getText().toString();
                        String name=edit_name.getText().toString();
                        cmdmanager.insert(name,cmd);

                        notifyAdapter();

                        Toast.makeText(getContext(),
                                "保存成功",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        inputDialog.setNegativeButton("取消",null);
        inputDialog.show();
    }
    private void showEditDialog(final int position) {

        View view= LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog_cmd_edit,null,false);

        final EditText edit_name =view.findViewById(R.id.dialog_cmd_name);
        final EditText edit_cmd=view.findViewById(R.id.dialog_cmd_content);
        edit_name.setText(list.get(position).get(CmddataBase.KEY_NAME));
        edit_cmd.setText(list.get(position).get(CmddataBase.KEY_CMD));
        AlertDialog.Builder editDialog = new AlertDialog.Builder(getContext());
        editDialog.setTitle("编辑");
        editDialog.setView(view);
        editDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String cmd=edit_cmd.getText().toString();
                        String name=edit_name.getText().toString();
                        cmdmanager.update(list.get(position).get(CmddataBase.KEY_ID),name,cmd);
                        notifyAdapter();

                        Toast.makeText(getContext(),
                                "编辑成功",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        editDialog.setNegativeButton("取消",null);
        editDialog.show();
    }
    private SwipeMenuCreator createSwipeMenulistvieitem()
    {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("编辑");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setTitle("删除");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        return creator;
    }

    private void notifyAdapter()//notifyDataSetChanged()的list必须是同一个list,所以list是传参更新的,而不是new
    {
        list=cmdmanager.findAll(list);
        adapter.notifyDataSetChanged();

    }
}
