package com.example.hfp.MicroCommonweal.activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.adapter.CharityAdapter;
import com.example.hfp.MicroCommonweal.adapter.MessageAdapter;
import com.example.hfp.MicroCommonweal.object.Charity;
import com.example.hfp.MicroCommonweal.object.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    private List<Message> messageList = new ArrayList<>();

    RecyclerView recyclerView;
    private  MessageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initMessages();//初始化消息
        //初始化消息列表的recycle和adapter
        recyclerView = (RecyclerView)findViewById(R.id.rv_message);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        MessageAdapter adapter = new MessageAdapter(messageList);
//        recyclerView.setAdapter(adapter);
        //创建适配器
        adapter = new MessageAdapter(R.layout.message_item, messageList);

        //给RecyclerView设置适配器
        recyclerView.setAdapter(adapter);
        //条目点击事件
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MessageActivity.this, "点击了第" + (position + 1) + "条条目", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private  void initMessages(){
        for(int i =0;i<7;i++){
            Message message = new Message("联合国儿童基金委",R.drawable.avatar1,"您好！您报名的联合国儿童基金委志愿者…",R.drawable.point_red);
            messageList.add(message);
        }
    }
}
