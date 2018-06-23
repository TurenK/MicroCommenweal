package com.example.hfp.MicroCommonweal.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hfp.MicroCommonweal.R;

public class OrgInfoActivity extends AppCompatActivity {
    private ImageView background;
    private TextView str_name;
    private TextView str_intro;
    private TextView str_totaltime;
    private TextView str_totalnum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_info);

        background = findViewById(R.id.background);
        str_name = findViewById(R.id.str_name);
        str_intro = findViewById(R.id.str_intro);
        str_totaltime = findViewById(R.id.str_totaltime);
        str_totalnum = findViewById(R.id.str_totalnum);
    }
}
