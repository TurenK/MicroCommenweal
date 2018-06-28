package com.example.hfp.MicroCommonweal.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.adapter.CharityAdapter;
import com.example.hfp.MicroCommonweal.object.Charity;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener {
    //recyclerview控件
    private RecyclerView recyclerView;
    private Button button_back;
    private CharityAdapter listadapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //义工列表
    private List<Charity> charityList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        //mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(this);

        initData();//初始化义工

        initView();
    }
    /**
     * 刷新listView
     */
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }
    private void initView() {
        recyclerView = (RecyclerView)findViewById(R.id.rv_collection);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        initAdapter();
        // addHeadView();
        recyclerView.setAdapter(listadapter);
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        listadapter = new CharityAdapter(R.layout.charity_item,charityList,getApplicationContext());
        listadapter.openLoadAnimation();
        recyclerView.setAdapter(listadapter);
        //  addHeadView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_back:
                finish();
                break;
        }

    }
    private  void initData(){
//        Charity charity = new Charity();
//        charity.setName("支教活动");
//        charity.setIamgeId(R.drawable.thumbnail1);
//        charity.setPeoplenum("10人报名");
//        charity.setStatus("报名中");
//        charityList.add(charity);
    }
}
