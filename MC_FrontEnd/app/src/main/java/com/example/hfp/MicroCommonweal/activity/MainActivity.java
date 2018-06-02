package com.example.hfp.MicroCommonweal.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONPathException;
import com.example.hfp.MicroCommonweal.Utils.AsyncHttpUtil;
import com.example.hfp.MicroCommonweal.object.Charity;
import com.alibaba.fastjson.JSONObject;
import com.example.hfp.MicroCommonweal.object.UserInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.example.hfp.MicroCommonweal.R;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

import okhttp3.internal.http2.Header;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView reg_text;
    private Button btn_login;
    private EditText username;
    private  EditText pwd;
    private static String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reg_text = (TextView)findViewById(R.id.reg_text);
        btn_login = (Button)findViewById(R.id.loginbtn);
        username = findViewById(R.id.username);
        pwd = findViewById(R.id.password);
        reg_text.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        // API 23: we have to check if ACCESS_FINE_LOCATION and/or ACCESS_COARSE_LOCATION permission are granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
            // The ACCESS_COARSE_LOCATION is denied, then I request it and manage the result in
            // onRequestPermissionsResult() using the constant MY_PERMISSION_ACCESS_FINE_LOCATION
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        0);
            }
            // The ACCESS_FINE_LOCATION is denied, then I request it and manage the result in
            // onRequestPermissionsResult() using the constant MY_PERMISSION_ACCESS_FINE_LOCATION
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this,
                        new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                        0);
            }
        }

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
                //TODO: 判断输入格式
//                if (username.getText().toString().length() < 5){
//                    Toast.makeText(MainActivity.this, "用户名至少需要5位！", Toast.LENGTH_LONG).show();
//                }else if (pwd.getText().toString().length() < 6){
//                    Toast.makeText(MainActivity.this, "密码至少需要6位！", Toast.LENGTH_LONG).show();
//                }else{
//                    Log.d(TAG, "trying login!");
//                    btn_login.setEnabled(false);
//                    btn_login.setText("登录中...");
//                    login();
//                }
                startActivity(new Intent(MainActivity.this,MainUIActivity.class));
                btn_login.setEnabled(false);
                btn_login.setText("登录中...");
                finish();
                break;
        }

    }

    private boolean login(){
        String s_username = username.getText().toString().trim();
        String s_pwd = pwd.getText().toString();

//        btn_login.setEnabled(true);
//        btn_login.setText("登录");

        //创建网络访问对象
        JSONObject login_json = new JSONObject();
        login_json.put("userName", s_username);
        login_json.put("password", s_pwd);

        Log.d(TAG, login_json.toString());

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(login_json.toJSONString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.d(TAG, login_json.toJSONString());
        Log.d(TAG, "prepare to send!");


        AsyncHttpUtil.post(this, this.getString(R.string.URL_LOGIN), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                Log.d(TAG, content);
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
                    String uLabel = object.getString("userLabel");
                    String uAttention = object.getString("userAttention");
                    UserInfo userInfo = UserInfo.getUserInfo();
                    userInfo.setuId(uId);
                    userInfo.setuName(uName);
                    userInfo.setuPhone(uPhone);
                    userInfo.setuAge(uAge);
                    userInfo.setuAvatar(uAvatar);
                    userInfo.setuLable(uLabel);
                    userInfo.setuAttention(uAttention);
//                    Toast.makeText(MainActivity.this, "成功了！", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this,MainUIActivity.class));
                    finish();
                }else if(code == 400){
                    Toast.makeText(MainActivity.this, "用户名与密码不匹配！", Toast.LENGTH_LONG).show();
                    btn_login.setEnabled(true);
                    btn_login.setText("登录");
                }
//                super.onSuccess(content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d(TAG, "cannot connect to server!");
                Toast.makeText(MainActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
                btn_login.setEnabled(true);
                btn_login.setText("登录");
            }
        });

        return true;
    }


}
