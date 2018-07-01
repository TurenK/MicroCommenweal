package com.example.hfp.MicroCommonweal.activity;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.AsyncHttpUtil;
import com.example.hfp.MicroCommonweal.Utils.ImageUpAndDownUtil;
import com.example.hfp.MicroCommonweal.adapter.CharityCommentListAdapter;
import com.example.hfp.MicroCommonweal.adapter.PersonalAdapter;
import com.example.hfp.MicroCommonweal.object.Charity;
import com.example.hfp.MicroCommonweal.object.Personal;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CharityCommentListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener{
    private Button button_back;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView charity_iamge;
    private TextView charity_name;
    private TextView charity_description;
    private String actId;
    private String TAG = "CharityCommentListActivity";

    private List<Personal> personalList = new ArrayList<>();

    RecyclerView recyclerView;
    private CharityCommentListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_comment_list);

        button_back = (Button)findViewById(R.id.button_back);
        charity_iamge = (ImageView)findViewById(R.id.charity_iamge);
        charity_name = (TextView) findViewById(R.id.charity_name);
        charity_description = (TextView) findViewById(R.id.charity_description);
        button_back.setOnClickListener(this);
        actId = getIntent().getStringExtra("actId");

        initData();//初始化消息
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
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        // addHeadView();
        //recyclerView.setAdapter(adapter);
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        adapter = new CharityCommentListAdapter(R.layout.charity_comment_list_item, personalList,this);
        adapter.openLoadAnimation();
        recyclerView.setAdapter(adapter);
    }

    private  void initData(){
//        for(int i =0;i<6;i++){
//            Charity charity = new Charity();
//            charity.setName("保护水库环境做文明市民签名活动");
////            charity.setCommentscore();
////            charity.setImagepath();
//            charityList.add(charity);
//        }

        //创建网络访问对象
        JSONObject main_json = new JSONObject();
        main_json.put("actId", actId);

        Log.d(TAG, main_json.toString());

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(main_json.toJSONString(),"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        AsyncHttpUtil.post(this, this.getString(R.string.URL_CHARITY_COMMENT_LIST), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d(TAG, jsonObject.toString());
                if (code == 200){
                    //TODO get more JSON objects!
                    JSONObject objectdata =jsonObject.getJSONObject("data");

                    Charity charity = getActInfo(objectdata);

                    displayActInfo(charity);

                    getUserInfo(objectdata);

                    initView();

                }else{
                    Toast.makeText(CharityCommentListActivity.this, "没有数据", Toast.LENGTH_LONG).show();
                    initView();
                }
//                super.onSuccess(content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d(TAG, "cannot connect to server!");
                Toast.makeText(CharityCommentListActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
            }
        });
    }

    private Charity getActInfo(JSONObject objectdata){
        Charity organization = new Charity();
        if(objectdata.containsKey("actname")){
            organization.setName(objectdata.getString("actname"));
        }else {
            Log.d(TAG, "actname NOT FIND");
            organization.setName("LOAD FAILED");
        }
        if(objectdata.containsKey("actimage")){
            organization.setImagepath(objectdata.getString("actimage"));
        }else {
            Log.d(TAG, "actimage NOT FIND");
            organization.setImagepath("LOAD FAILED");
        }
        if(objectdata.containsKey("actid")){
            organization.setaID(objectdata.getString("actid"));
        }else {
            Log.d(TAG, "actid NOT FIND");
            organization.setaID("LOAD FAILED");
        }
        if(objectdata.containsKey("actcom")){
            organization.setActcom(objectdata.getIntValue("actcom"));
        }else {
            Log.d(TAG, "actcom NOT FIND");
            organization.setActcom(0);
        }
        if(objectdata.containsKey("actdesc")){
            organization.setDescription(objectdata.getString("actdesc"));
        }else {
            Log.d(TAG, "actdesc NOT FIND");
            organization.setDescription("LOAD FAILED");
        }

        return organization;
    }

    private void displayActInfo(Charity organization){
        charity_name.setText(organization.getName());
        charity_description.setText(organization.getDescription());
        getImages(organization.getImagepath(),charity_iamge);
    }

    private void getImages(String url, ImageView imageView){
        new ImageUpAndDownUtil(getApplicationContext()).testDownloadImage(url,imageView);
    }

    private void getUserInfo(JSONObject objectdata){
        for (int i = 1; i <= 20; i++){
            if (objectdata.containsKey(String.valueOf(i))) {
                JSONObject object = objectdata.getJSONObject(String.valueOf(i));
                Personal charity = new Personal();
                if(object.containsKey("userid")){
                    charity.setUid(object.getString("userid"));
                }else {
                    Log.d(TAG, "userid NOT FIND");
                    charity.setUid("LOAD FAILED");
                }
                if(object.containsKey("username")){
                    charity.setUname(object.getString("username"));
                }else {
                    Log.d(TAG, "username NOT FIND");
                    charity.setUname("LOAD FAILED");
                }
                if(object.containsKey("userimage")){
                    charity.setAvatorurl(object.getString("userimage"));
                }else {
                    Log.d(TAG, "userimage NOT FIND");
                    charity.setAvatorurl("LOAD FAILED");
                }
                if(object.containsKey("usercom")){
                    charity.setGrade(object.getIntValue("usercom"));
                }else {
                    Log.d(TAG, "usercom NOT FIND");
                    charity.setGrade(0);
                }
                if(object.containsKey("usercomdesc")){
                    charity.setCommittext(object.getString("usercomdesc"));
                }else {
                    Log.d(TAG, "usercomdesc NOT FIND");
                    charity.setCommittext("LOAD FAILED");
                }
                personalList.add(charity);
            }
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                personalList.clear();
                initData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }
}
