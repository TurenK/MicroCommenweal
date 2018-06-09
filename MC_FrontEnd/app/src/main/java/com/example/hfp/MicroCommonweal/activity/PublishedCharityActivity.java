package com.example.hfp.MicroCommonweal.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.adapter.CharityAdapter;
import com.example.hfp.MicroCommonweal.object.Charity;

import java.util.ArrayList;
import java.util.List;

public class PublishedCharityActivity extends AppCompatActivity implements View.OnClickListener {
    //recyclerview控件
    RecyclerView recyclerView;
    private Button button_back;
    //义工列表
    private List<Charity> charityList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_published_charity);
        button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(this);

        initCharities();//初始化义工

        //初始化义工列表的recycle和adapter
        recyclerView = (RecyclerView)findViewById(R.id.rv_published_charity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        CharityAdapter adapter = new CharityAdapter(charityList);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_back:
                finish();
                break;
        }

    }
    private  void initCharities(){
//        Charity charity = new Charity();
//        charity.setName("支教活动");
//        charity.setIamgeId(R.drawable.thumbnail1);
//        charity.setPeoplenum("10人报名");
//        charity.setStatus("报名中");
//        charityList.add(charity);
    }
}
