package com.example.hfp.MicroCommonweal.activity;
/*
* Desc:组织对个人评价界面
* */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.adapter.PersonalAdapter;
import com.example.hfp.MicroCommonweal.object.Personal;

import java.util.ArrayList;
import java.util.List;

public class CommitPerActivity extends AppCompatActivity implements View.OnClickListener{
    private Button button_back;
    private Button button_submit;

    private List<Personal> personalList = new ArrayList<>();

    RecyclerView recyclerView;
    private PersonalAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit_per);

        button_back = (Button)findViewById(R.id.button_back);
        button_submit = findViewById(R.id.button_submit_comment_to_user);
        button_back.setOnClickListener(this);
        button_submit.setOnClickListener(this);

        initPersonal();//初始化消息
        initView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_back:
                finish();
                break;
            case R.id.button_submit_comment_to_user:
                sendGrade();
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
        for(int i =0;i<10;i++){
           // Personal personal = new Personal("联合国儿童基金委",R.drawable.avatar1,"您好！您报名的联合国儿童基金委志愿者…",R.drawable.point_red);
            Personal personal = new Personal();
//            personal.setAvatorurl();
            personal.setCommittext("亲爱的志愿者: 感谢您对志愿事业的大力支持");
            personal.setGrade(1);
            personalList.add(personal);
        }
    }

    private void sendGrade(){
        Log.d("CommentPersonActivity", String.valueOf(personalList.size()));
        for (int i = 0; i < personalList.size(); i++){
            Log.d("rating ", i + " " + personalList.get(i).getGrade());
        }
    }
}

