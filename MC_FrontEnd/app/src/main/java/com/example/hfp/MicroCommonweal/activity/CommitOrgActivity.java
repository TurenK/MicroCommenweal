package com.example.hfp.MicroCommonweal.activity;

import android.content.Intent;
import android.media.Image;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.AsyncHttpUtil;
import com.example.hfp.MicroCommonweal.Utils.ImageUpAndDownUtil;
import com.example.hfp.MicroCommonweal.object.Organization;
import com.example.hfp.MicroCommonweal.object.UserInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;

public class CommitOrgActivity extends AppCompatActivity implements View.OnClickListener{
    private Button button_back;
    private Button button_submit;
    private ImageView charity_iamge;
    private TextView charity_name;
    private TextView activity_num;
    private TextView charity_description;
    private TextInputEditText P2O_Commit;
    private RatingBar star_rating;
    private int actId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit_org);

        P2O_Commit = (TextInputEditText)findViewById(R.id.P2O_Commit);
        charity_iamge = (ImageView)findViewById(R.id.charity_iamge);
        charity_name = (TextView)findViewById(R.id.charity_name);
        activity_num = (TextView)findViewById(R.id.activity_num);
        charity_description = (TextView)findViewById(R.id.charity_description);
        star_rating = (RatingBar)findViewById(R.id.star_rating) ;
        button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(this);
        button_submit = (Button)findViewById(R.id.button_submit);
        button_submit.setOnClickListener(this);
        Intent intent = this.getIntent();
        String actSId =  intent.getStringExtra("actId");
        actId = Integer.valueOf(actSId);
        initialize();
    }

    private void getImages(String url, ImageView imageView){
        new ImageUpAndDownUtil(getApplicationContext()).testDownloadImage(url,imageView);
    }

    private void initialize(){
        JSONObject join_info = new JSONObject();
        join_info.put("actId", actId);

        Log.d("CommitOrgActivity", join_info.toString());

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(join_info.toString());
//            stringEntity.setContentEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        AsyncHttpUtil.post(this, this.getString(R.string.URL_GET_ORGINFO), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d("CharityDetailActivity", jsonObject.toString());

                if (code == 200){
                    //TODO Intent
                    charity_name.setText(jsonObject.getString("orgname"));
                    activity_num.setText("发起过: "+jsonObject.getString("actnum")+"项活动");
                    charity_description.setText(jsonObject.getString("orgdes"));
                    getImages(jsonObject.getString("orgimg"),charity_iamge);

                }else if(code == 403){
                    Toast.makeText(CommitOrgActivity.this, info, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d("PublishActivity", "cannot connect to server!");
                Toast.makeText(CommitOrgActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_back:
                finish();
                break;
            case R.id.button_submit:
                submit();
                break;
        }
    }

    private void submit(){
        JSONObject join_info = new JSONObject();
        join_info.put("actId", actId);
        join_info.put("userId", UserInfo.getUserInfo().getuId());
        join_info.put("ratnum", star_rating.getNumStars());
        String desc = P2O_Commit.getText().toString();
        if(desc!=null&&!desc.isEmpty()){
            join_info.put("cominfo", desc);
        }else {
            join_info.put("cominfo", "");
        }

        Log.d("pengfeiwuer", join_info.toString());

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(join_info.toString());
//            stringEntity.setContentEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        AsyncHttpUtil.post(this, this.getString(R.string.URL_SEND_P2OCOMMIT), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d("CharityDetailActivity", jsonObject.toString());

                if (code == 200){
                    //TODO Intent
                    Toast.makeText(CommitOrgActivity.this, "评价成功！", Toast.LENGTH_LONG).show();
                    finish();
                }else if(code == 403){
                    Toast.makeText(CommitOrgActivity.this, "评价失败！"+info, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d("PublishActivity", "cannot connect to server!");
                Toast.makeText(CommitOrgActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
            }
        });
    }

}
