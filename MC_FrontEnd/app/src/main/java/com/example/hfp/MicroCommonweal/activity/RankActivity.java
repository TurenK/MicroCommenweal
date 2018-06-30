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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.AsyncHttpUtil;
import com.example.hfp.MicroCommonweal.Utils.ImageUpAndDownUtil;
import com.example.hfp.MicroCommonweal.adapter.PerCommitAdapter;
import com.example.hfp.MicroCommonweal.adapter.RankAdapter;
import com.example.hfp.MicroCommonweal.object.Charity;
import com.example.hfp.MicroCommonweal.object.Question_Chose;
import com.example.hfp.MicroCommonweal.object.Rank;
import com.example.hfp.MicroCommonweal.object.UserInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class RankActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener{
    private List<Rank> rankList = new ArrayList<>();
    RecyclerView recyclerView;
    private Button button_back;
    private RankAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView image_avatar;
    private TextView tv_rank;
    private TextView tv_score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        button_back = (Button)findViewById(R.id.button_back);
        image_avatar = findViewById(R.id.image_avatar);
        tv_rank = findViewById(R.id.str_num_ranking);
        tv_score = findViewById(R.id.str_num_bean);
        button_back.setOnClickListener(this);

        initView();
        initRank();
    }

    private void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }
    /**
     * 刷新listView
     */
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rankList.clear();
                initRank();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        recyclerView = (RecyclerView)findViewById(R.id.rv_rank);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RankAdapter(R.layout.rank_item, rankList,this);
        adapter.openLoadAnimation();
        recyclerView.setAdapter(adapter);
        //  addHeadView();
        //item添加监听
//        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Toast.makeText(OrgRateActivity.this, Integer.toString(position), Toast.LENGTH_LONG).show();
//                Intent intent=new Intent();
//                intent.setClass(OrgRateActivity.this, CommitPerActivity.class);
//                intent.putExtra("activityID", charityList.get(position).getaID());
//                startActivity(intent);
//            }
//        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_back:
                finish();
                break;
        }

    }


    private  void initRank(){
        List<Rank> ranks = new ArrayList<>();
        JSONObject join_info = new JSONObject();
        join_info.put("userId", UserInfo.getUserInfo().getuId());
        Log.d("RankActivity", join_info.toString());

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(join_info.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        AsyncHttpUtil.post(this, this.getString(R.string.URL_RANK_LIST), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d("RankActivity", jsonObject.toString());
                if (code == 200) {
                    JSONObject objectdata = jsonObject.getJSONObject("data");
                    if (objectdata.containsKey("me")) {
                        JSONObject myObj = objectdata.getJSONObject("me");
//                        tv_rank.setText(myObj.getString());
                        tv_score.setText(myObj.getString("userScore"));
                        new ImageUpAndDownUtil(RankActivity.this).testDownloadImage(myObj.getString("userImage"),image_avatar);

                    }
                    List<Charity> charities = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        if (objectdata.containsKey(String.valueOf(i))) {
                            JSONObject object = objectdata.getJSONObject(String.valueOf(i));
                            Rank rank = new Rank();
                            rank.setAvatar(object.getString("userImage"));
                            rank.setDonatenumber(object.getString("userScore"));
                            rank.setRank(String.valueOf(i+1));
                            rank.setuName(object.getString("userName"));
                            rankList.add(rank);
                        }else{
                            break;
                        }
                    }
                    initAdapter();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d("RankActivity", "cannot connect to server!");
                Toast.makeText(RankActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
            }
        });
    }
}
