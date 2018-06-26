package com.example.hfp.MicroCommonweal.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.adapter.OrgCommentCharityAdapter;
import com.example.hfp.MicroCommonweal.object.Charity;

import java.util.ArrayList;
import java.util.List;

public class OrgInfoActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView image_avatar;
    private TextView str_name;
    private TextView str_address;
    private TextView str_intro;
    private TextView tv_comment_score;
    private TextView tv_total_time;
    private TextView tv_charity_num;
    private Button button_back;

    RecyclerView recyclerView;
    private OrgCommentCharityAdapter adapter;
    private List<Charity> charityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_info);

        //初始化控件
        image_avatar = findViewById(R.id.image_avatar);
        str_name = findViewById(R.id.str_name);
        str_address = findViewById(R.id.str_address);
        str_intro = findViewById(R.id.str_intro);
        tv_comment_score = findViewById(R.id.tv_comment_score);
        tv_total_time = findViewById(R.id.tv_total_time);
        tv_charity_num = findViewById(R.id.tv_charity_num);

        button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(this);

        initCharities();//初始化消息
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_back:
                finish();
                break;
        }

    }

    private void initView() {
        recyclerView = (RecyclerView)findViewById(R.id.rv_org_detail);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        // addHeadView();
        recyclerView.setAdapter(adapter);
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        adapter = new OrgCommentCharityAdapter(R.layout.charity_comment_item, charityList);
        adapter.openLoadAnimation();
        recyclerView.setAdapter(adapter);
        //  addHeadView();
        //item添加监听
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(OrgInfoActivity.this,CharityCommentListActivity.class));
            }
        });
    }

    private  void initCharities(){
        for(int i =0;i<6;i++){
            Charity charity = new Charity();
            charity.setName("保护水库环境做文明市民签名活动");
//            charity.setCommentscore();
//            charity.setImagepath();
            charityList.add(charity);
        }
    }

}
