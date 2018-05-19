package com.example.hfp.MicroCommonweal.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hfp.MicroCommonweal.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    //声明控件
    private ImageButton avatar;
    private EditText name;
    private EditText email;
    private EditText password_reg;
    private EditText password_reg_again;
    private TextView login_text;
    private Button register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //初始化控件
        avatar = (ImageButton)findViewById(R.id.avatar);
        name = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.email);
        password_reg = (EditText)findViewById(R.id.password_reg);
        password_reg_again = (EditText)findViewById(R.id.password_reg_again);
        login_text = (TextView)findViewById(R.id.login_text);
        register_btn = (Button)findViewById(R.id.register_btn);
        //控件事件监听器
        avatar.setOnClickListener(this);
        register_btn.setOnClickListener(this);
        login_text.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avatar: // 头像
                Toast.makeText(RegisterActivity.this, "点击头像", Toast.LENGTH_SHORT).show();
                break;
            case R.id.register_btn: // 注册
                Toast.makeText(RegisterActivity.this, "注册", Toast.LENGTH_SHORT).show();
                break;
            case R.id.login_text: // 登录
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                finish();
                break;
            default:
                break;
        }
    }
}
