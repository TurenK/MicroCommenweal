package com.example.hfp.changhaowoer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hfp.changhaowoer.object.Charity;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.example.hfp.changhaowoer.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView reg_text;
    private Button login;
    private EditText username;
    private  EditText pwd;
    private static String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reg_text = (TextView)findViewById(R.id.reg_text);
        login = (Button)findViewById(R.id.loginbtn);
        username = findViewById(R.id.username);
        pwd = findViewById(R.id.password);
        reg_text.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reg_text:
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                //切换界面效果
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                finish();
                break;

            case R.id.loginbtn:
                //login();
                Log.d(TAG, "trying login!");
                startActivity(new Intent(MainActivity.this,MainUIActivity.class));
                //finish();
                break;
        }

    }

    private boolean login(){
        String s_username = username.getText().toString().trim();
        String s_pwd = pwd.getText().toString();
        String url = "http://172.31.34.141:8080/hobby_group_djbgxz";


        //创建网络访问对象
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userName", s_username);
        params.put("Password", s_pwd);
        Log.d(TAG, "im here!");


        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String content) {
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d(TAG, info);
                if (code == 200){
                    JSONObject object = jsonObject.getJSONObject("data");
                    String uId = object.getString("userId");
                    String uName = object.getString("userName");
                    String uPhone = object.getString("userTelephone");
                    String uAge = object.getString("userAge");
                    String uAvatar = object.getString("userImage");
                    String uLable = object.getString("userLabel");
                    String uAttention = object.getString("userAttention");
                }else if(code == 400){

                }
//                super.onSuccess(content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d(TAG, "cannot connect to server!");
//                super.onFailure(error, content);
            }
        });

        return true;
    }


}
