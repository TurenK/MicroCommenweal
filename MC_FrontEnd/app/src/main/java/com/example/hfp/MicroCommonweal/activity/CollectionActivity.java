package com.example.hfp.MicroCommonweal.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.adapter.CharityAdapter;
import com.example.hfp.MicroCommonweal.object.Charity;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity {
    //recyclerview控件
    RecyclerView recyclerView;

    //义工列表
    private List<Charity> charityList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        initCharities();//初始化义工

        //初始化义工列表的recycle和adapter
        recyclerView = (RecyclerView)findViewById(R.id.rv_collection);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        CharityAdapter adapter = new CharityAdapter(charityList);
        recyclerView.setAdapter(adapter);
    }

    private  void initCharities(){
        Charity charity = new Charity();
        charity.setName("支教活动");
        charity.setIamgeId(R.drawable.thumbnail1);
        charity.setPeoplenum("10人报名");
        charity.setStatus("报名中");
        charityList.add(charity);
    }
}
