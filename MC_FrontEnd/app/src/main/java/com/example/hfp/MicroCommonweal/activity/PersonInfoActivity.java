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
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.AsyncHttpUtil;
import com.example.hfp.MicroCommonweal.Utils.ImageUpAndDownUtil;
import com.example.hfp.MicroCommonweal.adapter.CharityAdapter;
import com.example.hfp.MicroCommonweal.adapter.CommentPersonAdapter;
import com.example.hfp.MicroCommonweal.adapter.PersonalAdapter;
import com.example.hfp.MicroCommonweal.object.Charity;
import com.example.hfp.MicroCommonweal.object.Personal;
import com.example.hfp.MicroCommonweal.object.UserInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;

import java.util.ArrayList;
import java.util.List;

public class PersonInfoActivity extends AppCompatActivity implements View.OnClickListener{
    private String JOINING = "报名中";
    private String CLOSED = "已结束";
    private String DUE = "已截止";

    //声明控件
    private Button button_back;
    private ImageView image_avatar;
    private TextView str_name;
    private TextView str_intro;
    private TextView tv_comment_score;
    private TextView tv_total_time;
    private TextView tv_charity_num;
    private CharityAdapter listadapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //recyclerview控件
    RecyclerView recyclerView_charity;
    RecyclerView recyclerView_comment;

    private String uid;

    //义工列表
    private List<Charity> charityList = new ArrayList<>();
    private List<Personal> personalList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

        Intent intent = getIntent();
        uid = intent.getStringExtra("userID");
        Log.d("123", uid);

        //初始化控件
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        image_avatar = findViewById(R.id.image_avatar);
        str_name = findViewById(R.id.str_name);
        str_intro = findViewById(R.id.str_intro);
        tv_comment_score = findViewById(R.id.tv_comment_score);
        tv_total_time = findViewById(R.id.tv_total_time);
        tv_charity_num = findViewById(R.id.tv_charity_num);

        button_back = (Button)findViewById(R.id.btn_back_info);
        button_back.setOnClickListener(this);

        init();
//        initPersonal();//初始化消息
//        initCharities();//初始化义工
    }

//    /**
//     * 刷新listView
//     */
//    @Override
//    public void onRefresh() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                init();
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//        }, 1000);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back_info:
                finish();
                break;
        }

    }

    private  void initPersonal(){
        //初始化评价列表的recycle和adapter
        recyclerView_comment = (RecyclerView)findViewById(R.id.rv_comment);
        recyclerView_comment.setLayoutManager(new LinearLayoutManager(this));
        CommentPersonAdapter adapter_comment = new CommentPersonAdapter(R.layout.person_comment_list_item, personalList, this);
        adapter_comment.openLoadAnimation();
        recyclerView_comment.setAdapter(adapter_comment);
    }

    private void initCharity(){
        //初始化义工列表的recycle和adapter
        recyclerView_charity = (RecyclerView)findViewById(R.id.rv_charity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView_charity.setLayoutManager(layoutManager);
        CharityAdapter adapter_charity = new CharityAdapter(R.layout.charity_item,charityList,getApplicationContext());
        recyclerView_charity.setAdapter(adapter_charity);
    }

    private void init(){
        JSONObject detail_info = new JSONObject();
        detail_info.put("userId", uid);
        Log.d("CommitPerActivity", detail_info.toString());
        StringEntity stringEntity = new StringEntity(detail_info.toString(), "UTF-8");
        AsyncHttpUtil.post(this, this.getString(R.string.URL_USER_INFO), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d("PersonInfoActivity", jsonObject.toString());

                if (code == 200){
                    //TODO Intent
//                    Toast.makeText(CharityDetailActivity.this, "创建成功！", Toast.LENGTH_LONG).show();
                    JSONObject alldata = jsonObject.getJSONObject("data");

                    String uid = alldata.getString("userid");
                    String uname = alldata.getString("username");
                    String avatar = alldata.getString("userimage");
                    String ulabel = alldata.getString("userLabel");
                    int actnum = alldata.getInteger("actnum");
                    int totalTime = alldata.getInteger("totalTime");
                    int comnum = alldata.getInteger("comnum");
                    int grade = alldata.getInteger("grade");
                    str_name.setText(uname);
                    str_intro.setText(UserInfo.getUserInfo().getuIntro());
//                    str_intro.setText(Us);
                    tv_comment_score.setText(String.valueOf(grade));
                    tv_total_time.setText(String.valueOf(totalTime));
                    tv_charity_num.setText(String.valueOf(actnum));
                    new ImageUpAndDownUtil(PersonInfoActivity.this).testDownloadImage(avatar, image_avatar);
                    for (int i = 0; i < comnum; i++){
                        if (alldata.containsKey(String.valueOf(i))){
                            JSONObject commentObj = alldata.getJSONObject(String.valueOf(i));
                            Personal personal = new Personal();
                            personal.setUid(commentObj.getString("orgid"));
                            personal.setAvatorurl(commentObj.getString("orgimage"));
                            personal.setCommittext(commentObj.getString("orgdes"));
                            personal.setGrade(commentObj.getInteger("orgcom"));
                            personalList.add(personal);
                            Log.d("PersonInfoActivity", personal.getCommittext() + personal.getGrade());
                        }
                    }

                    initPersonal();
                    initAct();
                }else if(code == 400){
                    Toast.makeText(PersonInfoActivity.this, "获取信息失败！请稍后再试", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d("CommitPerActivity", "cannot connect to server!");
                Toast.makeText(PersonInfoActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
            }
        });
    }

    private void initAct(){
        JSONObject detail_info = new JSONObject();
        detail_info.put("userId", uid);
        Log.d("CommitPerActivity", detail_info.toString());
        StringEntity stringEntity = new StringEntity(detail_info.toString(), "UTF-8");
        AsyncHttpUtil.post(this, this.getString(R.string.URL_MY_JOIN), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d("PersonInfoActivity", jsonObject.toString());

                if (code == 200){
                    //TODO Intent
//                    Toast.makeText(CharityDetailActivity.this, "创建成功！", Toast.LENGTH_LONG).show();
                    JSONObject alldata = jsonObject.getJSONObject("data");
                    for (int i = 1; i <= 10; i++){
                        if (alldata.containsKey(String.valueOf(i))){
                            JSONObject actObj = alldata.getJSONObject(String.valueOf(i));
                            Charity charity = new Charity();
                            charity.setaID(actObj.getString("activityId"));
                            charity.setName(actObj.getString("activityName"));
                            charity.setImagepath(actObj.getString("activityImage"));
                            String actStatus = actObj.getString("activityStatus");
                            switch (actStatus) {
                                case "1":
                                    charity.setStatus(JOINING);
                                    break;
                                case "0":
                                    charity.setStatus(CLOSED);
                                    break;
                                case "2":
                                    charity.setStatus(DUE);
                                    break;
                            }
                            charityList.add(charity);
                        }
                    }
                    initCharity();
                }else if(code == 400){
                    Toast.makeText(PersonInfoActivity.this, "获取信息失败！请稍后再试", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d("CommitPerActivity", "cannot connect to server!");
                Toast.makeText(PersonInfoActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
            }
        });
    }
}
