package com.example.hfp.MicroCommonweal.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.adapter.OrganizationAdapter;
import com.example.hfp.MicroCommonweal.object.Organization;

import java.util.ArrayList;
import java.util.List;

public class PreRateActivity extends AppCompatActivity implements View.OnClickListener{
    private List<Organization> organizationList = new ArrayList<>();
    private Button button_back;
    RecyclerView recyclerView;
    private OrganizationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_rate);
        button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(this);

        initOrganizations();//初始化消息
        initView();

    }


    private void initView() {
        recyclerView = (RecyclerView)findViewById(R.id.rv_pre_rate);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        // addHeadView();
        recyclerView.setAdapter(adapter);
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        adapter = new OrganizationAdapter(R.layout.comment_per_item, organizationList);
        adapter.openLoadAnimation();
        recyclerView.setAdapter(adapter);
        //  addHeadView();
        //item添加监听
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(PreRateActivity.this, Integer.toString(position), Toast.LENGTH_LONG).show();
                startActivity(new Intent(PreRateActivity.this,CommitOrgActivity.class));
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

    private  void initOrganizations(){
        for(int i =0;i<3;i++){
            Organization organization = new Organization();
            organization.setOrgname("联合国儿童基金委");
            organizationList.add(organization);
        }
    }
}
