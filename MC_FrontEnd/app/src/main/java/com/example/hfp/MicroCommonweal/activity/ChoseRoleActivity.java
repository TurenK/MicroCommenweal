package com.example.hfp.MicroCommonweal.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.hfp.MicroCommonweal.R;

public class ChoseRoleActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton personal_user;
    private ImageButton team_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_role);

        personal_user = (ImageButton)findViewById(R.id.personal_user);
        team_user = (ImageButton)findViewById(R.id.team_user);
        personal_user.setOnClickListener(this);
        team_user.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personal_user: // 个人用户
                startActivity(new Intent(ChoseRoleActivity.this,RegisterActivity.class));
                finish();
                break;
            case R.id.team_user: // 团队用户
                startActivity(new Intent(ChoseRoleActivity.this,RegisterTeamActivity.class));
                finish();
                break;
            default:
                break;
        }
    }
}
