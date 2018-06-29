package com.example.hfp.MicroCommonweal.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.AsyncHttpUtil;
import com.example.hfp.MicroCommonweal.object.UserInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;

public class IdentifyInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button_back;
    private Button button_submit;
    private static String TAG = "IdentifyInfoActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_info);
        button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(this);
        button_submit = (Button)findViewById(R.id.button_submit);
        button_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_back:
                //todo...提交数据


                finish();
                break;
            case R.id.button_submit:
                //todo...提交数据
                sendInfo();
                finish();
                break;
        }

    }

    private void sendInfo(){
        //创建网络访问对象
        JSONObject login_json = new JSONObject();
        login_json.put("groupId", UserInfo.getUserInfo().getuId());
        Log.d(TAG, login_json.toString());

        StringEntity stringEntity = null;

        stringEntity = new StringEntity(login_json.toString(), "UTF-8");

        Log.d(TAG, login_json.toJSONString());
        Log.d(TAG, "prepare to send!");


        AsyncHttpUtil.post(this, this.getString(R.string.URL_VERTIFY_ORG), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                Log.d(TAG, content);
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d(TAG, info);
                if (code == 200) {
                    Toast.makeText(IdentifyInfoActivity.this, "验证成功！", Toast.LENGTH_LONG).show();
                    UserInfo.getUserInfo().setuVerify(UserInfo.VERTIFIED);
                    finish();
                } else {
                    Toast.makeText(IdentifyInfoActivity.this, "验证失败！", Toast.LENGTH_LONG).show();
                }
//                super.onSuccess(content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d(TAG, "cannot connect to server!");
                Toast.makeText(IdentifyInfoActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
//
            }
        });
    }
}
