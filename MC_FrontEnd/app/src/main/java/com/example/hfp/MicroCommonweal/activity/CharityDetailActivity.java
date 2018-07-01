package com.example.hfp.MicroCommonweal.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
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
import com.mob.wrappers.UMSSDKWrapper;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CharityDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private List<Recentjoin> recentjoinList = new ArrayList<>();
    private String JOINING = "报名中";
    private String JOINED = "已报名";
    private String OPENING = "报名中";
    private String CLOSED = "已结束";
    private String DUE = "已截止";

    RecyclerView recyclerView;

    //声明控件
    private Button button_back;
    private Button btn_join;
    //private ImageButton btn_share;
    //private ImageButton btn_chat;
    private ImageButton btn_collect;
    private ImageView VBackground;
    private TextView tv_title;
    private TextView tv_main_title;
    private TextView tv_joinNum;
    private TextView tv_targetNum;
    private TextView tv_dayLeft;
    private TextView tv_detailInfo;
    private TextView str_originator;
    private TextView tv_charityType;
    private TextView tv_startTime;
    private TextView tv_endTime;
    private TextView tv_phone;
    private TextView tv_addr;
    private ImageUpAndDownUtil imageUpAndDownUtil;

    private String aID;
    private String gID;
    private int uStatus;
    private String aStatus;
    private int favStatus;
    private String gPhone;
    private String gAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_detail);

        Intent intent = getIntent();
        aID = intent.getStringExtra("activityID");

        //初始化控件
        VBackground = (ImageView) findViewById(R.id.VBackground);
        button_back = (Button) findViewById(R.id.button_back);
        btn_join = (Button) findViewById(R.id.btn_join);
        //btn_share = (ImageButton) findViewById(R.id.btn_share);
        //btn_chat = (ImageButton) findViewById(R.id.btn_chat);
        btn_collect = (ImageButton) findViewById(R.id.btn_collect);
        tv_title = findViewById(R.id.str_title);
        tv_main_title = findViewById(R.id.title);
        tv_joinNum = findViewById(R.id.str_numsign);
        tv_targetNum = findViewById(R.id.str_numaim);
        tv_dayLeft = findViewById(R.id.str_dayleft);
        tv_detailInfo = findViewById(R.id.str_detail);
        str_originator = findViewById(R.id.str_originator);
        tv_charityType = findViewById(R.id.str_ID);
        tv_startTime = findViewById(R.id.str_introduction);
        tv_endTime = findViewById(R.id.str_condition);
        tv_phone = findViewById(R.id.str_tel);
        tv_addr = findViewById(R.id.str_connect_address);
        imageUpAndDownUtil = new ImageUpAndDownUtil(getApplicationContext());

        //设置监听器
        button_back.setOnClickListener(this);
        //btn_share.setOnClickListener(this);
        btn_join.setOnClickListener(this);
        //btn_chat.setOnClickListener(this);
        btn_collect.setOnClickListener(this);
        str_originator.setOnClickListener(this);

        initInfo(aID);
//        initRecentjoin();//初始化消息
        //初始化消息列表的recycle和adapter
        recyclerView = (RecyclerView) findViewById(R.id.rv_recent_join);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back:
//                Intent mIntent = new Intent(this, MainUIActivity.class);
//                mIntent.putExtra("statuCar", "1");
//                startActivity(mIntent);
                finish();
                break;
//            case R.id.btn_share:
//                Toast.makeText(CharityDetailActivity.this, "分享", Toast.LENGTH_SHORT).show();
//                break;
            case R.id.btn_join:
                if (UserInfo.getUserInfo().getuVerify() == UserInfo.VERTIFIED) {
                    if (UserInfo.getUserInfo().getType() == UserInfo.CHARITY_USER && uStatus == 0) {
                        joinActivity();
                    } else if (UserInfo.getUserInfo().getType() == UserInfo.CHARITY_USER && uStatus == 1) {
                        Toast.makeText(CharityDetailActivity.this, "您已报名此活动！", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(CharityDetailActivity.this, "组织用户无法报名！", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(CharityDetailActivity.this, "您未进行验证！", Toast.LENGTH_LONG).show();
                    if (UserInfo.getUserInfo().getType() == UserInfo.CHARITY_USER) {
                        startActivity(new Intent(CharityDetailActivity.this, VertifyPerActivity.class));
                    } else {
                        startActivity(new Intent(CharityDetailActivity.this, IdentifyInfoActivity.class));
                    }
                }
//                Toast.makeText(CharityDetailActivity.this, "报名", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.btn_chat:
//                Toast.makeText(CharityDetailActivity.this, "聊天", Toast.LENGTH_SHORT).show();
//                break;
            case R.id.btn_collect:
                if (favStatus == 0){
                    favourite();
                }else if (favStatus == 1){
                    cancelFavourite();
                }
//                Toast.makeText(CharityDetailActivity.this, "收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.str_originator:
                Intent intent = new Intent();
                intent.putExtra("orgid", gID);
                intent.putExtra("orgAddr", gAddr);
                intent.putExtra("orgPhone", gPhone);
                intent.setClass(CharityDetailActivity.this, OrgInfoActivity.class);
                getApplicationContext().startActivity(intent);
                //startActivity(new Intent(CharityDetailActivity.this,OrgInfoActivity.class));
                // startActivity(new Intent(CharityDetailActivity.this,PersonInfoActivity.class));

                break;

        }
    }

    private void initInfo(String aID) {
        JSONObject detail_info = new JSONObject();
        detail_info.put("activityId", aID);
        if(UserInfo.getUserInfo().getType()==UserInfo.CHARITY_USER)
        detail_info.put("userId", UserInfo.getUserInfo().getuId());
        else
            detail_info.put("groupId", UserInfo.getUserInfo().getuId());
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

                if (code == 200) {
                    //TODO Intent
//                    Toast.makeText(CharityDetailActivity.this, "创建成功！", Toast.LENGTH_LONG).show();
                    JSONObject alldata = jsonObject.getJSONObject("data");
                    JSONObject object = alldata.getJSONObject("value");
                    JSONObject gInfo = alldata.getJSONObject("groupinfo");
                    String activityId = object.getString("activityId");
                    String activitySponsor = object.getString("activitySponsor");
                    String activityType = object.getString("activityType");
                    String activityName = object.getString("activityName");
                    String activityImage = object.getString("activityImage");
                    imageUpAndDownUtil.testDownloadImage(activityImage, VBackground);
                    String activityReleaseTime = object.getString("activityReleaseTime");
                    String activityDeadline = object.getString("activityDeadline");
                    String activityStartTime = object.getString("activityStartTime");
                    String activityEndTime = object.getString("activityEndTime");
                    String activityAddress = object.getString("activityAddress");
                    String activityTel = object.getString("activityTel");
                    String activityIntroduction = object.getString("activityIntroduction");
                    String aNeedNumOfPerson = object.getString("aNeedNumOfPerson");
                    String aSurplusQuota = object.getString("aSurplusQuota");
                    aStatus = object.getString("activityStatus");
                    favStatus = object.getInteger("collectStatus");
                    gPhone = gInfo.getString("groupTelephone");
                    gAddr = gInfo.getString("groupAddress");
                    if (favStatus == 0){
                        btn_collect.setBackgroundResource(R.drawable.btn_star);
                    }else if (favStatus == 1){
                        btn_collect.setBackgroundResource(R.drawable.star_white);
                    }
                    gID = activitySponsor;
                    str_originator.setText(gInfo.getString("groupName"));
                    tv_title.setText(activityName);
                    tv_main_title.setText(activityName);
                    tv_detailInfo.setText(activityIntroduction);
                    tv_joinNum.setText(String.valueOf(Integer.parseInt(aNeedNumOfPerson) - Integer.parseInt(aSurplusQuota)));
                    tv_targetNum.setText(aNeedNumOfPerson);
                    tv_charityType.setText(activityType);
                    tv_startTime.setText(activityStartTime);
                    tv_endTime.setText(activityEndTime);
                    tv_phone.setText(activityTel);
                    tv_addr.setText(activityAddress);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    String nowDate = df.format(new Date());// new Date()为获取当前系统时间
                    int dayLeft = daysBetween(nowDate, activityDeadline);
                    if (dayLeft < 0){
                        dayLeft = 0;
                    }
                    tv_dayLeft.setText(String.valueOf(dayLeft));
                    if (UserInfo.getUserInfo().getType() == UserInfo.CHARITY_USER) {
                        uStatus = object.getInteger("userStatus");
                        if (aStatus.equals("1") && uStatus == 0) {
                            btn_join.setText(JOINING);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                btn_join.setBackgroundColor(getColor(R.color.unparticipate));
                            }
                        } else if (aStatus.equals("1") && uStatus == 1) {
                            btn_join.setText(JOINED);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                btn_join.setBackgroundColor(getColor(R.color.participated));
                            }
                        } else if (aStatus.equals("0")) {
                            btn_join.setText(CLOSED);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                btn_join.setBackgroundColor(getColor(R.color.allfinished));
                            }
                        } else if (aStatus.equals("2")) {
                            btn_join.setText(DUE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                btn_join.setBackgroundColor(getColor(R.color.parfinished));
                            }
                        }
                    } else {
                        switch (aStatus) {
                            case "1":
                                btn_join.setText(OPENING);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    btn_join.setBackgroundColor(getColor(R.color.unparticipate));
                                }
                                break;
                            case "0":
                                btn_join.setText(CLOSED);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    btn_join.setBackgroundColor(getColor(R.color.allfinished));
                                }
                                break;
                            case "2":
                                btn_join.setText(DUE);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    btn_join.setBackgroundColor(getColor(R.color.parfinished));
                                }
                                break;
                        }
                    }

                    JSONObject usersObj = alldata.getJSONObject("join");
                    for (int i = 1; i <= 100; i++) {
                        if (usersObj.containsKey(String.valueOf(i))) {
                            JSONObject userObj = usersObj.getJSONObject(String.valueOf(i));
                            String uid = userObj.getString("userId");
                            String avatar = userObj.getString("userImage");
                            Recentjoin recentjoin = new Recentjoin();
                            recentjoin.setAvatar(avatar);
                            recentjoin.setUid(uid);
                            recentjoinList.add(recentjoin);
                        } else {
                            break;
                        }
                    }

                    RecentjoinAdapter adapter = new RecentjoinAdapter(recentjoinList);
                    recyclerView.setAdapter(adapter);
                    Log.d("CharityDetailActivity", "join number" + recentjoinList.size() + "");

                    Log.d("CharityDetailActivity", activityName + " " + activityDeadline + " " + activityIntroduction + " "
                            + (Integer.parseInt(aNeedNumOfPerson) - Integer.parseInt(aSurplusQuota)) + " " + daysBetween(nowDate, activityDeadline));

                } else if (code == 400) {
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

    private void joinActivity() {
        JSONObject join_info = new JSONObject();
        join_info.put("userId", UserInfo.getUserInfo().getuId());
        join_info.put("activityId", aID);

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

                if (code == 200) {
                    //TODO Intent
                    Toast.makeText(CharityDetailActivity.this, "参与成功！", Toast.LENGTH_LONG).show();
                    btn_join.setText(JOINED);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        btn_join.setBackgroundColor(getColor(R.color.participated));
                    }
                } else if (code == 403) {
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

    public static int daysBetween(String smdate, String bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        long time1 = 0;
        long time2 = 0;

        try {
            cal.setTime(sdf.parse(smdate));
            time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            time2 = cal.getTimeInMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    public void favourite(){
        JSONObject join_info = new JSONObject();
        if (UserInfo.getUserInfo().getType() == UserInfo.CHARITY_USER){
            join_info.put("userId", UserInfo.getUserInfo().getuId());
        }else if (UserInfo.getUserInfo().getType() == UserInfo.CHARITY_ORG){
            join_info.put("groupId", UserInfo.getUserInfo().getuId());
        }
        join_info.put("activityId",aID);

        Log.d("CharityDetailActivity", join_info.toString());

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(join_info.toString());
//            stringEntity.setContentEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        AsyncHttpUtil.post(this, this.getString(R.string.URL_FAVOURITE), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d("CharityDetailActivity", jsonObject.toString());

                if (code == 200){
                    //TODO Intent
                    Toast.makeText(CharityDetailActivity.this, "收藏成功！", Toast.LENGTH_LONG).show();
                    btn_collect.setBackgroundResource(R.drawable.star_white);
                    favStatus = 1;
                }else if(code == 400){
                    Toast.makeText(CharityDetailActivity.this, "收藏失败，请稍后再试！", Toast.LENGTH_LONG).show();
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

    public void cancelFavourite(){
        JSONObject join_info = new JSONObject();
        if (UserInfo.getUserInfo().getType() == UserInfo.CHARITY_USER){
            join_info.put("userId", UserInfo.getUserInfo().getuId());
        }else if (UserInfo.getUserInfo().getType() == UserInfo.CHARITY_ORG){
            join_info.put("groupId", UserInfo.getUserInfo().getuId());
        }
        join_info.put("activityId",aID);

        Log.d("CharityDetailActivity", join_info.toString());

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(join_info.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        AsyncHttpUtil.post(this, this.getString(R.string.URL_FAVOURITE_CANCEL), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d("CharityDetailActivity", jsonObject.toString());

                if (code == 200){
                    //TODO Intent
                    Toast.makeText(CharityDetailActivity.this, "取消收藏成功！", Toast.LENGTH_LONG).show();
                    btn_collect.setBackgroundResource(R.drawable.btn_star);
                    favStatus = 0;
                }else if(code == 400){
                    Toast.makeText(CharityDetailActivity.this, "取消收藏失败，请稍后再试！", Toast.LENGTH_LONG).show();
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
}
