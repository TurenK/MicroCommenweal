package com.example.hfp.MicroCommonweal.activity;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.adapter.PerCommitAdapter;
import com.example.hfp.MicroCommonweal.adapter.RankAdapter;
import com.example.hfp.MicroCommonweal.object.Rank;

import java.util.ArrayList;
import java.util.List;

public class RankActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener{
    private List<Rank> rankList = new ArrayList<>();
    RecyclerView recyclerView;
    private Button button_back;
    private RankAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(this);

        initView();

        initRank();//初始化消息
        //初始化消息列表的recycle和adapter
//        recyclerView = (RecyclerView)findViewById(R.id.rv_rank);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        RankAdapter adapter = new RankAdapter(rankList);
//        recyclerView.setAdapter(adapter);
    }

    private void initView() {
        recyclerView = (RecyclerView)findViewById(R.id.rv_rank);
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
                initRank();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        adapter = new RankAdapter(R.layout.rank_item, rankList,this);
        adapter.openLoadAnimation();
        recyclerView.setAdapter(adapter);
        //  addHeadView();
        //item添加监听
//        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Toast.makeText(OrgRateActivity.this, Integer.toString(position), Toast.LENGTH_LONG).show();
//                Intent intent=new Intent();
//                intent.setClass(OrgRateActivity.this, CommitPerActivity.class);
//                intent.putExtra("activityID", charityList.get(position).getaID());
//                startActivity(intent);
//            }
//        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_back:
                finish();
                break;
        }

    }


    private  void initRank(){
        List<Rank> ranks = new ArrayList<>();
        for(int i =0;i<10;i++){
            Rank rank = new Rank();
            rank.setAvatar(R.drawable.avatar1);
            rank.setDonatenumber("1000");
            rank.setRank(String.valueOf(i+1));
            ranks.add(rank);
        }
        adapter.removeAllData();
        adapter.addData(ranks);
    }
}
