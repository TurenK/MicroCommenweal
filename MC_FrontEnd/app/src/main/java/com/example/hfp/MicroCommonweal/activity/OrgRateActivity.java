package com.example.hfp.MicroCommonweal.activity;
/*
* Desc:这个界面是组织评价，活动列表
* */
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.example.hfp.MicroCommonweal.adapter.PerCommitAdapter;
import com.example.hfp.MicroCommonweal.object.Charity;
import com.example.hfp.MicroCommonweal.object.UserInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class OrgRateActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener{

    private Button button_back;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //义工列表
    private List<Charity> charityList = new ArrayList<>();
    //recyclerview控件
    RecyclerView recyclerView;

    private PerCommitAdapter adapter;

    private String TAG = "OrgRateActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_rate);

        button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(this);

        initView();

        initCharities();//初始化义工
    }

    @Override
    public void onRestart() {
        super.onRestart();
        initCharities();
    }

    /**
     * 刷新listView
     */
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initCharities();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
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
        recyclerView = (RecyclerView)findViewById(R.id.rv_org_rate);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        // addHeadView();
        recyclerView.setAdapter(adapter);
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        adapter = new PerCommitAdapter(R.layout.charity_item, charityList,this);
        adapter.openLoadAnimation();
        recyclerView.setAdapter(adapter);
        //  addHeadView();
        //item添加监听
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(OrgRateActivity.this, Integer.toString(position), Toast.LENGTH_LONG).show();
                Intent intent=new Intent();
                intent.setClass(OrgRateActivity.this, CommitPerActivity.class);
                intent.putExtra("activityID", charityList.get(position).getaID());
                startActivity(intent);
            }
        });
    }
    private  void initCharities(){
        requireCharity();
    }


    private void requireCharity(){

        String uid = UserInfo.getUserInfo().getuId();

        //创建网络访问对象
        JSONObject main_json = new JSONObject();
        main_json.put("gId", uid);

        Log.d(TAG, main_json.toString());

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(main_json.toJSONString(),"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        AsyncHttpUtil.post(this, this.getString(R.string.URL_UNCOMMENT_ACT), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d(TAG, jsonObject.toString());
                if (code == 200){
                    //TODO get more JSON objects!
                    JSONObject objectdata =jsonObject.getJSONObject("data");
                    List<Charity> charities = new ArrayList<>();
                    for (int i = 1; i <= 10; i++){
                        if (objectdata.containsKey(String.valueOf(i))) {
                            JSONObject object = objectdata.getJSONObject(String.valueOf(i));
                            String actID = object.getString("activityId");
                            String actName = object.getString("activityName");
                            String actImage = object.getString("activityImage");
                            String aSQ = object.getString("aSurplusQuota");
                            String aNN = object.getString("aNeedNumOfPerson");
//                            String actStatus = object.getString("activityStatus");
//                            int userStatus = object.getInteger("userStatus");
                            //TODO create a Charity object
                           //if(actStatus.equals("1")){
                            Charity charity = new Charity();
                            charity.setaID(actID);
                            charity.setName(actName);
                            charity.setImagepath(actImage);
                            charity.setPeoplenum("剩余"+aSQ+"人");
                            charity.setStatus("评论");
                            charities.add(charity);
                          //  }

                        }
                        adapter.removeAllData();
                        adapter.addData(charities);
                    }
                }else if(code == 400){
                    Toast.makeText(OrgRateActivity.this, "获取活动失败！请稍后再试", Toast.LENGTH_LONG).show();
                }else if(code == 203){
                    Toast.makeText(OrgRateActivity.this, "暂时没有待评价的用户！", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d(TAG, "cannot connect to server!");
                Toast.makeText(OrgRateActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
            }
        });
    }
}
