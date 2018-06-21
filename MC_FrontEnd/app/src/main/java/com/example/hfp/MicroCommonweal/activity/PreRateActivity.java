package com.example.hfp.MicroCommonweal.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.AsyncHttpUtil;
import com.example.hfp.MicroCommonweal.adapter.CharityAdapter;
import com.example.hfp.MicroCommonweal.adapter.OrganizationAdapter;
import com.example.hfp.MicroCommonweal.object.Charity;
import com.example.hfp.MicroCommonweal.object.Organization;
import com.example.hfp.MicroCommonweal.object.UserInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class PreRateActivity extends AppCompatActivity implements View.OnClickListener{
    private List<Organization> organizationList = new ArrayList<>();
    private Button button_back;
    RecyclerView recyclerView;
    private OrganizationAdapter adapter;
    public final String TAG = "pengfeiwuer";

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
        adapter = new OrganizationAdapter(R.layout.comment_per_item, organizationList,getApplicationContext());
        adapter.openLoadAnimation();
        recyclerView.setAdapter(adapter);
        //  addHeadView();
        //item添加监听
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(PreRateActivity.this, Integer.toString(position), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PreRateActivity.this,CommitOrgActivity.class);
                intent.putExtra("orgId",organizationList.get(position).getOrgId());
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

    private void initOrganizations(){
//        for(int i =0;i<3;i++){
//            Organization organization = new Organization();
//            organization.setOrgname("联合国儿童基金委");
//            organizationList.add(organization);
//        }

        requireOrganization();
    }

    private void requireOrganization(){
        String uid = UserInfo.getUserInfo().getuId();

//        btn_login.setEnabled(true);
//        btn_login.setText("登录");

        //创建网络访问对象
        JSONObject main_json = new JSONObject();
        main_json.put("userId", uid);

        Log.d(TAG, main_json.toString());

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(main_json.toJSONString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        AsyncHttpUtil.post(getApplicationContext(), this.getString(R.string.URL_PRE_RATE), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d(TAG, jsonObject.toString());
                if (code == 200){
                    //TODO get more JSON objects!
                    JSONObject objectdata =jsonObject.getJSONObject("data");
                    for (int i = 1; i <= 10; i++) {
                        if (objectdata.containsKey(String.valueOf(i))) {
                            JSONObject object = objectdata.getJSONObject(String.valueOf(i));
                            int orgID = object.getInteger("organizationId");
                            String orgName = object.getString("organizationName");
                            String orgImage = object.getString("organizationImage");
                            Organization organization = new Organization();
                            organization.setOrgId(orgID);
                            organization.setOrgname(orgName);
                            organization.setAvatorurl(orgImage);

                            organizationList.add(organization);
                        }
                    }
                }else if(code == 400){
                    Toast.makeText(getApplicationContext(), "获取活动失败！请稍后再试", Toast.LENGTH_LONG).show();
                }
//                super.onSuccess(content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d(TAG, "cannot connect to server!");
                Toast.makeText(getApplicationContext(), "无法连接到服务器！", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
            }
        });
    }
}
