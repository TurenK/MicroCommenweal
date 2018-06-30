package com.example.hfp.MicroCommonweal.activity;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.AsyncHttpUtil;
import com.example.hfp.MicroCommonweal.Utils.ImageUpAndDownUtil;
import com.example.hfp.MicroCommonweal.adapter.OrgCommentCharityAdapter;
import com.example.hfp.MicroCommonweal.object.Charity;
import com.example.hfp.MicroCommonweal.object.Organization;
import com.example.hfp.MicroCommonweal.object.UserInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class OrgInfoActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener{
    private ImageView image_avatar;
    private TextView str_name;
    private TextView str_address;
    private TextView str_intro;
    private TextView tv_comment_score;
    private TextView tv_total_time;
    private TextView tv_charity_num;
    private Button button_back;
    private String TAG = "OrgInfoActivity";
    private String OrgId;
    private String gAddr;
    private String gPhone;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    RecyclerView recyclerView;
    private OrgCommentCharityAdapter adapter;
    private List<Charity> charityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_info);

        //初始化控件
        image_avatar = findViewById(R.id.image_avatar);
        str_name = findViewById(R.id.str_name);
        str_address = findViewById(R.id.str_address);
        str_intro = findViewById(R.id.str_intro);
        tv_comment_score = findViewById(R.id.tv_comment_score);
        tv_total_time = findViewById(R.id.tv_total_time);
        tv_charity_num = findViewById(R.id.tv_charity_num);
        button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(this);
        OrgId = getIntent().getStringExtra("orgid");
        gAddr = getIntent().getStringExtra("orgAddr");
        gPhone = getIntent().getStringExtra("orgPhone");

        initView();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_back:
                finish();
                break;
        }

    }

    private void initView() {
        recyclerView = (RecyclerView)findViewById(R.id.rv_org_detail);
        //mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        //mSwipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        // addHeadView();
        recyclerView.setAdapter(adapter);
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        adapter = new OrgCommentCharityAdapter(R.layout.charity_comment_item, charityList,getApplicationContext());
        adapter.openLoadAnimation();
        recyclerView.setAdapter(adapter);
        //  addHeadView();
        //item添加监听
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Charity charity = charityList.get(position);
                //Toast.makeText(v.getContext(), "点击了", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.setClass(getApplicationContext(), CharityCommentListActivity.class);
                intent.putExtra("actId", charity.getaID());
                getApplicationContext().startActivity(intent);
            }
        });
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
        main_json.put("orgId", OrgId);

        Log.d(TAG, main_json.toString());

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(main_json.toJSONString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        AsyncHttpUtil.post(this, this.getString(R.string.URL_ORG_INFO), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d(TAG, info);
                if (code == 200){
                    //TODO get more JSON objects!
                    JSONObject objectdata =jsonObject.getJSONObject("data");

                    Organization organization = getOrgInfo(objectdata);

                    displayOrgInfo(organization);

                    List<Charity> charities = getCharityInfo(objectdata);

                    adapter.removeAllData();
                    adapter.addData(charities);

                }else{
                    Toast.makeText(OrgInfoActivity.this, "获取活动失败！请稍后再试", Toast.LENGTH_LONG).show();
                }
//                super.onSuccess(content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d(TAG, "cannot connect to server!");
                Toast.makeText(OrgInfoActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
            }
        });
    }

    private Organization getOrgInfo(JSONObject objectdata){
        Organization organization = new Organization();
        if(objectdata.containsKey("orgid")){
            organization.setOrgId(objectdata.getString("orgid"));
        }else {
            Log.d(TAG, "orgid NOT FIND");
            organization.setOrgId("LOAD FAILED");
        }
        if(objectdata.containsKey("orgname")){
            organization.setOrgname(objectdata.getString("orgname"));
        }else {
            Log.d(TAG, "orgname NOT FIND");
            organization.setOrgname("LOAD FAILED");
        }
        if(objectdata.containsKey("orgimage")){
            organization.setOrgimage(objectdata.getString("orgimage"));
        }else {
            Log.d(TAG, "orgimage NOT FIND");
            organization.setOrgimage("LOAD FAILED");
        }
        if(objectdata.containsKey("actnum")){
            organization.setActnum(objectdata.getIntValue("actnum"));
        }else {
            Log.d(TAG, "actnum NOT FIND");
            organization.setActnum(0);
        }
        if(objectdata.containsKey("description")){
            organization.setDescription(objectdata.getString("description"));
        }else {
            Log.d(TAG, "description NOT FIND");
            organization.setDescription("LOAD FAILED");
        }
        if(objectdata.containsKey("comnum")){
            organization.setComnum(objectdata.getIntValue("comnum"));
        }else {
            Log.d(TAG, "comnum NOT FIND");
            organization.setComnum(0);
        }
        if(objectdata.containsKey("grade")){
            organization.setGrade(objectdata.getIntValue("grade"));
        }else {
            Log.d(TAG, "grade NOT FIND");
            organization.setGrade(0);
        }
        if(objectdata.containsKey("totalTime")){
            organization.setTotalTime(objectdata.getIntValue("totalTime"));
        }else {
            Log.d(TAG, "totalTime NOT FIND");
            organization.setTotalTime(0);
        }
        return organization;
    }

    private void displayOrgInfo(Organization organization){
        str_name.setText(organization.getOrgname());
        str_address.setText(gAddr);
        str_intro.setText(organization.getDescription());
        tv_comment_score.setText(""+organization.getGrade()+"分");
        tv_total_time.setText(""+organization.getTotalTime()+"小时");
        tv_charity_num.setText(""+organization.getActnum()+"个");
        getImages(organization.getOrgimage(),image_avatar);
    }

    private void getImages(String url, ImageView imageView){
        new ImageUpAndDownUtil(getApplicationContext()).testDownloadImage(url,imageView);
    }

    private List<Charity> getCharityInfo(JSONObject objectdata){
        List<Charity> charities = new ArrayList<>();
        for (int i = 0; i <= 20; i++){
            if (objectdata.containsKey(String.valueOf(i))) {
                JSONObject object = objectdata.getJSONObject(String.valueOf(i));
                Charity charity = new Charity();
                if(object.containsKey("actid")){
                    charity.setaID(object.getString("actid"));
                }else {
                    Log.d(TAG, "actid NOT FIND");
                    charity.setaID("LOAD FAILED");
                }
                if(object.containsKey("actname")){
                    charity.setName(object.getString("actname"));
                }else {
                    Log.d(TAG, "actname NOT FIND");
                    charity.setName("LOAD FAILED");
                }
                if(object.containsKey("actimage")){
                    charity.setImagepath(object.getString("actimage"));
                }else {
                    Log.d(TAG, "actimage NOT FIND");
                    charity.setImagepath("LOAD FAILED");
                }
                if(object.containsKey("actcom")){
                    charity.setActcom(object.getIntValue("actcom"));
                }else {
                    Log.d(TAG, "actcom NOT FIND");
                    charity.setActcom(0);
                }
                charities.add(charity);
            }
        }
        return charities;
    }
}
