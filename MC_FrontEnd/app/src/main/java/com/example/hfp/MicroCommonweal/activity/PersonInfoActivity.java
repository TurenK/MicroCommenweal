package com.example.hfp.MicroCommonweal.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hfp.MicroCommonweal.R;

public class PersonInfoActivity extends AppCompatActivity {


    private ImageView background;
    private TextView  str_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

        background = findViewById(R.id.background);
        str_name = findViewById(R.id.str_name);

    }
}
