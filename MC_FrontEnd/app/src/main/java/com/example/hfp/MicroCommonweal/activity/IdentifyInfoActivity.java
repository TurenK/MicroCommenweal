package com.example.hfp.MicroCommonweal.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hfp.MicroCommonweal.R;

public class IdentifyInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_info);
        button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_back:
                //todo...提交数据


                finish();
                break;
        }

    }
}
