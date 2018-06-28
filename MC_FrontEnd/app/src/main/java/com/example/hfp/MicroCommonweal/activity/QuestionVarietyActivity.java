package com.example.hfp.MicroCommonweal.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.hfp.MicroCommonweal.R;

public class QuestionVarietyActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_back;
    private ImageButton img_btn_chose;
    private ImageButton img_btn_judge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_variety);

        btn_back = findViewById(R.id.btn_back_info);
        img_btn_chose = findViewById(R.id.img_btn_chose);
        img_btn_judge = findViewById(R.id.img_btn_judge);
        btn_back.setOnClickListener(this);
        img_btn_chose.setOnClickListener(this);
        img_btn_judge.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back_info:
                finish();
                break;
            case R.id.img_btn_chose:
                startActivity(new Intent(QuestionVarietyActivity.this,QuestionChoseActivity.class));
                finish();
                break;
            case R.id.img_btn_judge:
                startActivity(new Intent(QuestionVarietyActivity.this,QuestionJudgeActivity.class));
                finish();
                break;
        }

    }
}
