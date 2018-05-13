package com.example.hfp.MicroCommonweal.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.adapter.MessageAdapter;
import com.example.hfp.MicroCommonweal.adapter.RecentjoinAdapter;
import com.example.hfp.MicroCommonweal.object.Recentjoin;

import java.util.ArrayList;
import java.util.List;

public class CharityDetailActivity extends AppCompatActivity {
    private List<Recentjoin> recentjoinList = new ArrayList<>();

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_detail);

        initRecentjoin();//初始化消息

        //初始化消息列表的recycle和adapter
        recyclerView = (RecyclerView)findViewById(R.id.rv_recent_join);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        RecentjoinAdapter adapter = new RecentjoinAdapter(recentjoinList);
        recyclerView.setAdapter(adapter);
    }



    private  void initRecentjoin(){
        for(int i =0;i<7;i++){
            Recentjoin recentjoin = new Recentjoin(R.drawable.avatar1);
            recentjoinList.add(recentjoin);
        }
    }
}
