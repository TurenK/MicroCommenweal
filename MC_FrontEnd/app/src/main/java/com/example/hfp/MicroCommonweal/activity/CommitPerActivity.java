package com.example.hfp.MicroCommonweal.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.adapter.PersonalAdapter;
import com.example.hfp.MicroCommonweal.object.Personal;

import java.util.ArrayList;
import java.util.List;

public class CommitPerActivity extends AppCompatActivity implements View.OnClickListener{
    private Button button_back;

    private List<Personal> personalList = new ArrayList<>();

    RecyclerView recyclerView;
    private PersonalAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit_per);

        button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(this);

        initPersonal();//初始化消息
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
        recyclerView = (RecyclerView)findViewById(R.id.rv_commit_per);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        // addHeadView();
        recyclerView.setAdapter(adapter);
    }


    /**
     * 初始化adapter
     */
    private void initAdapter() {
        adapter = new PersonalAdapter(R.layout.rate_item, personalList,this);
        adapter.openLoadAnimation();
        recyclerView.setAdapter(adapter);
        //  addHeadView();
        //item添加监听
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(CommitPerActivity.this, Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });
    }


    private  void initPersonal(){
        for(int i =0;i<6;i++){
           // Personal personal = new Personal("联合国儿童基金委",R.drawable.avatar1,"您好！您报名的联合国儿童基金委志愿者…",R.drawable.point_red);
            Personal personal = new Personal();
//            personal.setAvatorurl();
//            personal.setCommittext();
            personalList.add(personal);
        }
    }
}

