package com.example.hfp.MicroCommonweal.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.AsyncHttpUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    //声明控件
    private ImageButton avatar;
    private EditText name;
    private EditText phone;
    private EditText password_reg;
    private EditText password_reg_again;
    private TextView login_text;
    private Button register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //初始化控件
        avatar = (ImageButton) findViewById(R.id.avatar);
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.email);
        password_reg = (EditText) findViewById(R.id.password_reg);
        password_reg_again = (EditText) findViewById(R.id.password_reg_again);
        login_text = (TextView) findViewById(R.id.login_text);
        register_btn = (Button) findViewById(R.id.register_btn);
        //控件事件监听器
        avatar.setOnClickListener(this);
        register_btn.setOnClickListener(this);
        login_text.setOnClickListener(this);
        //设置不能输入特殊字符和空格
        setEditTextInhibitInputSpeChat(name);
        setEditTextInhibitInputSpace(name);
    }
    /**
     * 禁止EditText输入空格
     * @param editText
     */
    public static void setEditTextInhibitInputSpace(EditText editText){
        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(source.equals(" ")){
                    return "";
                }else{
                    return null;
                }

            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 禁止EditText输入特殊字符
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText){

        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if(matcher.find())return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avatar: // 头像
                Toast.makeText(RegisterActivity.this, "点击头像", Toast.LENGTH_SHORT).show();
                break;
            case R.id.register_btn: // 注册
                if(password_reg.getText().toString().equals(password_reg_again.getText().toString())){
                    register();
                }else{
                    Toast.makeText(RegisterActivity.this, "密码不匹配！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.login_text: // 登录
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
                break;
            default:
                break;
        }
    }


    private boolean register() {
        String s_username = name.getText().toString().trim();
        String s_pwd = password_reg.getText().toString();
        String s_phone = phone.getText().toString().trim();

        //创建网络访问对象
        JSONObject register_json = new JSONObject();
        register_json.put("username", s_username);
        register_json.put("password", s_pwd);
        register_json.put("phone",s_phone);

        StringEntity stringEntity = null;
        stringEntity = new StringEntity(register_json.toString(),"utf-8");

        Log.d("RegisterActivity", "prepare to send!");

        AsyncHttpUtil.post(this, this.getString(R.string.URL_REG), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                Log.d("RegisterActivity", content);
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d("RegisterActivity", info);
                if (code == 200){
                    Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(MainActivity.this,MainUIActivity.class));
//                    finish();
                }else if(code == 404){
                    Toast.makeText(RegisterActivity.this, "用户名与密码不匹配！", Toast.LENGTH_LONG).show();
                    register_btn.setEnabled(true);
                    register_btn.setText("注册");
                }else if(code == 405){
                    Toast.makeText(RegisterActivity.this, "该用户注册过！", Toast.LENGTH_LONG).show();
                    register_btn.setEnabled(true);
                    register_btn.setText("注册");
                }
//                super.onSuccess(content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d("RegisterActivity", "cannot connect to server!");
                Toast.makeText(RegisterActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
                register_btn.setEnabled(true);
                register_btn.setText("注册");
            }
        });

        return true;


    }

}
