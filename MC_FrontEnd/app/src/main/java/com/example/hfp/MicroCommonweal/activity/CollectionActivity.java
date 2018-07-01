package com.example.hfp.MicroCommonweal.activity;

import android.content.Intent;
import android.os.Build;
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
import com.example.hfp.MicroCommonweal.object.Charity;
import com.example.hfp.MicroCommonweal.object.UserInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener {
    private String OPENING = "报名中";
    private String CLOSED = "已结束";
    private String DUE = "已截止";
    //recyclerview控件
    private RecyclerView recyclerView;
    private Button button_back;
    private CharityAdapter listadapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //义工列表
    private List<Charity> charityList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        //mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(this);

        initData();//初始化义工

        initView();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        initData();
    }

    /**
     * 刷新listView
     */
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }
    private void initView() {
        recyclerView = (RecyclerView)findViewById(R.id.rv_collection);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        initAdapter();
        // addHeadView();
        recyclerView.setAdapter(listadapter);
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        listadapter = new CharityAdapter(R.layout.charity_item,charityList,getApplicationContext());
        listadapter.openLoadAnimation();
        recyclerView.setAdapter(listadapter);
        listadapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String actId = charityList.get(position).getaID();
                //Toast.makeText(v.getContext(), "点击了", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.setClass(CollectionActivity.this, CharityDetailActivity.class);
                intent.putExtra("activityID", actId);
                startActivity(intent);
            }
        });
        //  addHeadView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_back:
                finish();
                break;
        }

    }
    private  void initData(){
        JSONObject join_info = new JSONObject();
        if (UserInfo.getUserInfo().getType() == UserInfo.CHARITY_USER){
            join_info.put("userId", UserInfo.getUserInfo().getuId());
        }else if (UserInfo.getUserInfo().getType() == UserInfo.CHARITY_ORG){
            join_info.put("groupId", UserInfo.getUserInfo().getuId());
        }

        Log.d("CollectionActivity", join_info.toString());

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(join_info.toString(),"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        AsyncHttpUtil.post(this, this.getString(R.string.URL_GET_FAVOURITE), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d("CollectionActivity", jsonObject.toString());

                if (code == 200){
                    JSONObject objectdata =jsonObject.getJSONObject("data");
                    List<Charity> charities = new ArrayList<>();
                    int i = 1;
                    while(true){
                        if (objectdata.containsKey(String.valueOf(i))) {
                            JSONObject object = objectdata.getJSONObject(String.valueOf(i));
                            String actID = object.getString("activityId");
                            String actName = object.getString("activityName");
                            String actImage = object.getString("activityImage");
                            String aSQ = object.getString("aSurplusQuota");
//                            String aNN = object.getString("aNeedNumOfPerson");
                            String actStatus = object.getString("activityStatus");
//                            int userStatus = object.getInteger("userStatus");
                            //TODO create a Charity object
                            Charity charity = new Charity();
                            charity.setaID(actID);
                            charity.setName(actName);
                            charity.setImagepath(actImage);
                            charity.setPeoplenum("剩余"+aSQ+"人");
                            switch (actStatus) {
                                case "0":
                                    charity.setStatus(CLOSED);
                                    break;
                                case "1":
                                    charity.setStatus(OPENING);
                                    break;
                                case "2":
                                    charity.setStatus(DUE);
                                    break;
                            }
                            charities.add(charity);
                            i++;
                        }else{
                            break;
                        }
                    }
                    listadapter.removeAllData();
                    listadapter.addData(charities);
                }else if(code == 403){
                    Toast.makeText(CollectionActivity.this, "您已收藏此活动！", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d("CollectionActivity", "cannot connect to server!");
                Toast.makeText(CollectionActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
            }
        });
//        Charity charity = new Charity();
//        charity.setName("支教活动");
//        charity.setIamgeId(R.drawable.thumbnail1);
//        charity.setPeoplenum("10人报名");
//        charity.setStatus("报名中");
//        charityList.add(charity);
    }
}
