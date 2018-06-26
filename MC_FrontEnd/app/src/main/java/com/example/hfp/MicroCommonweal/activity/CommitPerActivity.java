package com.example.hfp.MicroCommonweal.activity;
/*
* Desc:组织对个人评价界面
* */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.AsyncHttpUtil;
import com.example.hfp.MicroCommonweal.adapter.PersonalAdapter;
import com.example.hfp.MicroCommonweal.object.Personal;
import com.example.hfp.MicroCommonweal.object.UserInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;

import java.util.ArrayList;
import java.util.List;

public class CommitPerActivity extends AppCompatActivity implements View.OnClickListener{
    private Button button_back;
    private Button button_submit;
    private String aID;

    private List<Personal> personalList = new ArrayList<>();

    RecyclerView recyclerView;
    private PersonalAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit_per);

        Intent intent = getIntent();
        aID = intent.getStringExtra("activityID");

        button_back = (Button)findViewById(R.id.button_back);
        button_submit = findViewById(R.id.button_submit_comment_to_user);
        button_back.setOnClickListener(this);
        button_submit.setOnClickListener(this);

        initPersonal();//初始化消息
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_back:
                finish();
                break;
            case R.id.button_submit_comment_to_user:
                sendGrade();
        }
    }

    private void initView() {
        recyclerView = (RecyclerView)findViewById(R.id.rv_commit_per);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        // addHeadView();
        recyclerView.setAdapter(adapter);
    }


    /**
     * 初始化adapter
     */
    private void initAdapter() {
        adapter = new PersonalAdapter(R.layout.rate_item, personalList,this);
        adapter.openLoadAnimation();
        recyclerView.setAdapter(adapter);
        //  addHeadView();
        //item添加监听
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(CommitPerActivity.this, Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });
    }


    private  void initPersonal(){
        JSONObject detail_info = new JSONObject();
        detail_info.put("activityId", aID);
        detail_info.put("userId", "1");
        Log.d("CommitPerActivity", detail_info.toString());
        StringEntity stringEntity = new StringEntity(detail_info.toString(), "UTF-8");
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
                    JSONObject usersObj = alldata.getJSONObject("join");

                    String aNeedNumOfPerson = object.getString("aNeedNumOfPerson");
                    for (int i = 1; i <= Integer.valueOf(aNeedNumOfPerson); i++){
                        if (usersObj.containsKey(String.valueOf(i))){
                            JSONObject userObj = usersObj.getJSONObject(String.valueOf(i));
                            Personal personal = new Personal();
                            personal.setUid(userObj.getString("userId"));
                            personal.setAvatorurl(userObj.getString("userImage"));
                            personal.setCommittext("亲爱的志愿者: 感谢您对志愿事业的大力支持");
                            personal.setGrade(1);
                            personalList.add(personal);
                        }
                    }
                    initView();
                }else if(code == 400){
                    Toast.makeText(CommitPerActivity.this, "获取信息失败！请稍后再试", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d("CommitPerActivity", "cannot connect to server!");
                Toast.makeText(CommitPerActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
            }
        });
    }

    private void sendGrade(){
        Log.d("CommentPersonActivity", "user number" + String.valueOf(personalList.size()));
        JSONObject allObject = new JSONObject();
        allObject.put("aId", aID);
        allObject.put("gId", UserInfo.getUserInfo().getuId());
        JSONObject comments = new JSONObject();
        for (int i = 0; i < personalList.size(); i++){
            Personal p = personalList.get(i);
            JSONObject detail_info = new JSONObject();
            detail_info.put("uId", p.getUid());
            detail_info.put("zan", p.getGrade());
            detail_info.put("remarks", p.getCommittext());
            comments.put(String.valueOf(i+1), detail_info);
        }
        allObject.put("rating", comments);
        Log.d("CommitPerActivity", allObject.toString());
//            StringEntity stringEntity = new StringEntity(detail_info.toString(), "UTF-8");

//            AsyncHttpUtil.post(this, this.getString(R.string.URL_ORG_COMMIT_RATING), stringEntity, "application/json", new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(String content) {
//                    JSONObject jsonObject = JSONObject.parseObject(content);
//                    int code = jsonObject.getInteger("code");
//                    String info = jsonObject.getString("message");
//                    Log.d("CharityDetailActivity", jsonObject.toString());
//
//                    if (code == 200){
//                        //TODO Intent
//                        if (i == personalList.size())
//                    }else if(code == 400){
//                        Toast.makeText(CommitPerActivity.this, "获取信息失败！请稍后再试", Toast.LENGTH_LONG).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Throwable error, String content) {
//                    Log.d("CommitPerActivity", "cannot connect to server!");
//                    Toast.makeText(CommitPerActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
////                super.onFailure(error, content);
//                }
//            });
//            Log.d("rating ", personalList.get(i).getUid() + " " + personalList.get(i).getGrade() + " " + personalList.get(i).getCommittext());
    }
}

