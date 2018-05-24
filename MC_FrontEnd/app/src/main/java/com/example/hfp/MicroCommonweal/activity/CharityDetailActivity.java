package com.example.hfp.MicroCommonweal.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.hfp.MicroCommonweal.CharityFragment;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.adapter.MessageAdapter;
import com.example.hfp.MicroCommonweal.adapter.RecentjoinAdapter;
import com.example.hfp.MicroCommonweal.object.Recentjoin;

import java.util.ArrayList;
import java.util.List;

public class CharityDetailActivity extends AppCompatActivity  implements View.OnClickListener{
    private List<Recentjoin> recentjoinList = new ArrayList<>();

    RecyclerView recyclerView;

    //声明控件
    private Button button_back;
    private Button btn_join;
    private ImageButton btn_share;
    private ImageButton btn_chat;
    private ImageButton btn_collect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_detail);

        //初始化控件
        button_back = (Button)findViewById(R.id.button_back);
        btn_join = (Button)findViewById(R.id.btn_join);
        btn_share = (ImageButton)findViewById(R.id.btn_share);
        btn_chat = (ImageButton)findViewById(R.id.btn_chat);
        btn_collect = (ImageButton)findViewById(R.id.btn_collect);
        //设置监听器
        button_back.setOnClickListener(this);
        btn_share.setOnClickListener(this);
        btn_join.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        btn_collect.setOnClickListener(this);

        initRecentjoin();//初始化消息

        //初始化消息列表的recycle和adapter
        recyclerView = (RecyclerView)findViewById(R.id.rv_recent_join);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        RecentjoinAdapter adapter = new RecentjoinAdapter(recentjoinList);
        recyclerView.setAdapter(adapter);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back:
                Intent mIntent = new Intent(this, MainUIActivity.class);
                mIntent.putExtra("statuCar", "1");
                startActivity(mIntent);
                finish();
                break;
            case R.id.btn_share:
                Toast.makeText(CharityDetailActivity.this, "分享", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_join:
                Toast.makeText(CharityDetailActivity.this, "报名", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_chat:
                Toast.makeText(CharityDetailActivity.this, "聊天", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_collect:
                Toast.makeText(CharityDetailActivity.this, "收藏", Toast.LENGTH_SHORT).show();
                break;


        }
    }

    private  void initRecentjoin(){
        for(int i =0;i<7;i++){
            Recentjoin recentjoin = new Recentjoin(R.drawable.avatar1);
            recentjoinList.add(recentjoin);
        }
    }
}
