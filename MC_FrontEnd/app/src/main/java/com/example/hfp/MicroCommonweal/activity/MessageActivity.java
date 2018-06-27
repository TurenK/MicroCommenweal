package com.example.hfp.MicroCommonweal.activity;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.adapter.MessageAdapter;
import com.example.hfp.MicroCommonweal.object.Message;

import java.util.ArrayList;
import java.util.List;


public class MessageActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener{
    private List<Message> messageList = new ArrayList<>();

    RecyclerView recyclerView;
    private  MessageAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Button button_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(this);


        initMessages();//初始化消息
        initView();

    }

    private void initView() {
        recyclerView = (RecyclerView)findViewById(R.id.rv_message);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        // addHeadView();
        recyclerView.setAdapter(adapter);
    }
    /**
     * 刷新listView
     */
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initAdapter();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }
    /**
     * 初始化adapter
     */
    private void initAdapter() {
        adapter = new MessageAdapter(R.layout.message_item, messageList);
        adapter.openLoadAnimation();
        recyclerView.setAdapter(adapter);
        //  addHeadView();
        //item添加监听
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MessageActivity.this, Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_back:
                finish();
                break;
        }

    }


    private  void initMessages(){
        for(int i =0;i<6;i++){
            Message message = new Message("联合国儿童基金委",R.drawable.avatar1,"您好！您报名的联合国儿童基金委志愿者…",R.drawable.point_red);
            messageList.add(message);
        }
    }
}
