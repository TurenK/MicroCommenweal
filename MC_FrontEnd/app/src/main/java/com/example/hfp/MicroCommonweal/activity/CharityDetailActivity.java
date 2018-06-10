package com.example.hfp.MicroCommonweal.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.hfp.MicroCommonweal.CharityFragment;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.AsyncHttpUtil;
import com.example.hfp.MicroCommonweal.Utils.ImageUpAndDownUtil;
import com.example.hfp.MicroCommonweal.adapter.MessageAdapter;
import com.example.hfp.MicroCommonweal.adapter.RecentjoinAdapter;
import com.example.hfp.MicroCommonweal.object.Recentjoin;
import com.example.hfp.MicroCommonweal.object.UserInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CharityDetailActivity extends AppCompatActivity  implements View.OnClickListener{
    private List<Recentjoin> recentjoinList = new ArrayList<>();

    RecyclerView recyclerView;

    //声明控件
    private Button button_back;
    private Button btn_join;
    private ImageButton btn_share;
    private ImageButton btn_chat;
    private ImageButton btn_collect;
    private ImageView VBackground;
    private TextView tv_title;
    private TextView tv_joinNum;
    private TextView tv_targetNum;
    private TextView tv_dayLeft;
    private TextView tv_detailInfo;
    private ImageUpAndDownUtil imageUpAndDownUtil;

    private String aID;
    private int uStatus;
    private String aStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_detail);

        Intent intent = getIntent();
        aID = intent.getStringExtra("activityID");

        //初始化控件
        VBackground = (ImageView) findViewById(R.id.VBackground);
        button_back = (Button)findViewById(R.id.button_back);
        btn_join = (Button)findViewById(R.id.btn_join);
        btn_share = (ImageButton)findViewById(R.id.btn_share);
        btn_chat = (ImageButton)findViewById(R.id.btn_chat);
        btn_collect = (ImageButton)findViewById(R.id.btn_collect);
        tv_title = findViewById(R.id.str_title);
        tv_joinNum = findViewById(R.id.str_numsign);
        tv_targetNum = findViewById(R.id.str_numaim);
        tv_dayLeft = findViewById(R.id.str_dayleft);
        tv_detailInfo = findViewById(R.id.str_detail);
        imageUpAndDownUtil = new ImageUpAndDownUtil(getApplicationContext());

        //设置监听器
        button_back.setOnClickListener(this);
        btn_share.setOnClickListener(this);
        btn_join.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        btn_collect.setOnClickListener(this);

        initInfo(aID);
        initRecentjoin();//初始化消息

        //初始化消息列表的recycle和adapter
        recyclerView = (RecyclerView)findViewById(R.id.rv_recent_join);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        RecentjoinAdapter adapter = new RecentjoinAdapter(recentjoinList);
        recyclerView.setAdapter(adapter);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back:
                Intent mIntent = new Intent(this, MainUIActivity.class);
                mIntent.putExtra("statuCar", "1");
                startActivity(mIntent);
                finish();
                break;
            case R.id.btn_share:
                Toast.makeText(CharityDetailActivity.this, "分享", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_join:
                if (UserInfo.getUserInfo().getType() == UserInfo.CHARITY_USER && uStatus == 0){
                    joinActivity();
                }else if (UserInfo.getUserInfo().getType() == UserInfo.CHARITY_USER && uStatus == 1){
                    Toast.makeText(CharityDetailActivity.this, "您已报名此活动！", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(CharityDetailActivity.this, "组织用户无法报名！", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(CharityDetailActivity.this, "报名", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_chat:
                Toast.makeText(CharityDetailActivity.this, "聊天", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_collect:
                Toast.makeText(CharityDetailActivity.this, "收藏", Toast.LENGTH_SHORT).show();
                break;


        }
    }

    private  void initRecentjoin(){
        for(int i =0;i<7;i++){
            Recentjoin recentjoin = new Recentjoin(R.drawable.avatar1);
            recentjoinList.add(recentjoin);
        }
    }

    private void initInfo(String aID){
        JSONObject detail_info = new JSONObject();
        detail_info.put("activityId", aID);
        detail_info.put("userId", UserInfo.getUserInfo().getuId());
        Log.d("CharityDetailActivity", detail_info.toString());
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(detail_info.toString(), "UTF-8");
//            stringEntity.setContentEncoding("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        AsyncHttpUtil.post(this, this.getString(R.string.URL_DETAIL_ACTIVITY), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d("CharityDetailActivity", jsonObject.toString());

                if (code == 200){
                    //TODO Intent
//                    Toast.makeText(CharityDetailActivity.this, "创建成功！", Toast.LENGTH_LONG).show();
                    JSONObject alldata = jsonObject.getJSONObject("data");
                    JSONObject object = alldata.getJSONObject("value");
                    String activityId = object.getString("activityId");
                    String activitySponsor = object.getString("activitySponsor");
                    String activityType = object.getString("activityType");
                    String activityName = object.getString("activityName");
                    String activityImage = object.getString("activityImage");
                    imageUpAndDownUtil.testDownloadImage(activityImage,VBackground);
                    String activityReleaseTime = object.getString("activityReleaseTime");
                    String activityDeadline = object.getString("activityDeadline");
                    String activityStartTime = object.getString("activityStartTime");
                    String activityEndTime = object.getString("activityEndTime");
                    String activityAddress = object.getString("activityAddress");
                    String activityTel = object.getString("activityTel");
                    String activityIntroduction = object.getString("activityIntroduction");
                    String aNeedNumOfPerson = object.getString("aNeedNumOfPerson");
                    String aSurplusQuota = object.getString("aSurplusQuota");
                    uStatus = object.getInteger("userStatus");
                    aStatus = object.getString("activityStatus");
                    tv_title.setText(activityName);
                    tv_detailInfo.setText(activityIntroduction);
                    tv_joinNum.setText(String.valueOf(Integer.parseInt(aNeedNumOfPerson)-Integer.parseInt(aSurplusQuota)));
                    tv_targetNum.setText(aNeedNumOfPerson);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    String nowDate = df.format(new Date());// new Date()为获取当前系统时间
                    tv_dayLeft.setText(String.valueOf(daysBetween(nowDate, activityDeadline)));
                    if (uStatus == 1){
                        btn_join.setText("已报名");
                    }
                    Log.d("CharityDetailActivity", activityName + " " + activityDeadline + " " + activityIntroduction + " "
                            + (Integer.parseInt(aNeedNumOfPerson)-Integer.parseInt(aSurplusQuota)) + " " + daysBetween(nowDate, activityDeadline));

                }else if(code == 400){
                    Toast.makeText(CharityDetailActivity.this, "获取信息失败！请稍后再试", Toast.LENGTH_LONG).show();
                }
//                super.onSuccess(content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d("PublishActivity", "cannot connect to server!");
                Toast.makeText(CharityDetailActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
            }
        });
    }

    private void joinActivity(){
        JSONObject join_info = new JSONObject();
        join_info.put("userId", UserInfo.getUserInfo().getuId());
        join_info.put("activityId",aID);

        Log.d("CharityDetailActivity", join_info.toString());

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(join_info.toString());
//            stringEntity.setContentEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        AsyncHttpUtil.post(this, this.getString(R.string.URL_JOIN), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d("CharityDetailActivity", jsonObject.toString());

                if (code == 200){
                    //TODO Intent
                    Toast.makeText(CharityDetailActivity.this, "参与成功！", Toast.LENGTH_LONG).show();
                }else if(code == 403){
                    Toast.makeText(CharityDetailActivity.this, "您已报名此活动！", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d("PublishActivity", "cannot connect to server!");
                Toast.makeText(CharityDetailActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
            }
        });
    }

    public static int daysBetween(String smdate,String bdate){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        long time1 = 0;
        long time2 = 0;

        try{
            cal.setTime(sdf.parse(smdate));
            time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            time2 = cal.getTimeInMillis();
        }catch(Exception e){
            e.printStackTrace();
        }
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }

}
