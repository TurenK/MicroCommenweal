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

public class CharityCommentListActivity extends AppCompatActivity implements View.OnClickListener{
    private Button button_back;

    private List<Personal> personalList = new ArrayList<>();

    RecyclerView recyclerView;
    private PersonalAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_comment_list);

        button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(this);

        initView();
        initPersonal();//初始化消息
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
        recyclerView = (RecyclerView)findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        // addHeadView();
        //recyclerView.setAdapter(adapter);
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        adapter = new PersonalAdapter(R.layout.charity_comment_personlist_item, personalList,this);
        adapter.openLoadAnimation();
        recyclerView.setAdapter(adapter);
    }

    private  void initPersonal(){
        for(int i = 0;i<10;i++){
            Personal personal = new Personal();
            personalList.add(personal);
        }
    }
}
