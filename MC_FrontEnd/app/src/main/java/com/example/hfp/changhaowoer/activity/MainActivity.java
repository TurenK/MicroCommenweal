package com.example.hfp.changhaowoer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hfp.changhaowoer.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView reg_text;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reg_text = (TextView)findViewById(R.id.reg_text);
        login = (Button)findViewById(R.id.loginbtn);
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
                startActivity(new Intent(MainActivity.this,MainUIActivity.class));
                finish();
                break;
        }


    }
}
