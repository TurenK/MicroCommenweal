package com.example.hfp.MicroCommonweal.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.adapter.CharityAdapter;
import com.example.hfp.MicroCommonweal.adapter.PersonalAdapter;
import com.example.hfp.MicroCommonweal.object.Charity;
import com.example.hfp.MicroCommonweal.object.Personal;

import java.util.ArrayList;
import java.util.List;

public class PersonInfoActivity extends AppCompatActivity implements View.OnClickListener{

    //声明控件
    private Button button_back;
    private ImageView image_avatar;
    private TextView str_name;
    private TextView str_intro;
    private TextView tv_comment_score;
    private TextView tv_total_time;
    private TextView tv_charity_num;

    //recyclerview控件
    RecyclerView recyclerView_charity;
    RecyclerView recyclerView_comment;

    //义工列表
    private List<Charity> charityList = new ArrayList<>();
    private List<Personal> personalList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

        //初始化控件
        image_avatar = findViewById(R.id.image_avatar);
        str_name = findViewById(R.id.str_name);
        str_intro = findViewById(R.id.str_intro);
        tv_comment_score = findViewById(R.id.tv_comment_score);
        tv_total_time = findViewById(R.id.tv_total_time);
        tv_charity_num = findViewById(R.id.tv_charity_num);

        button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(this);


        initPersonal();//初始化消息
        initCharities();//初始化义工

        //初始化义工列表的recycle和adapter
        recyclerView_charity = (RecyclerView)findViewById(R.id.rv_charity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView_charity.setLayoutManager(layoutManager);
        CharityAdapter adapter_charity = new CharityAdapter(charityList,getApplicationContext());
        recyclerView_charity.setAdapter(adapter_charity);

        //初始化评价列表的recycle和adapter
        recyclerView_comment = (RecyclerView)findViewById(R.id.rv_comment);
        recyclerView_comment.setLayoutManager(new LinearLayoutManager(this));
        PersonalAdapter adapter_comment = new PersonalAdapter(R.layout.charity_comment_personlist_item, personalList,this);
        adapter_comment.openLoadAnimation();
        recyclerView_comment.setAdapter(adapter_comment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_back:
                finish();
                break;
        }

    }


    private  void initCharities(){
        for (int i = 1; i <= 1; i++){
            Charity charity = new Charity();
            charityList.add(charity);
        }
    }

    private  void initPersonal(){
        for(int i = 0;i<1;i++){

            Personal personal = new Personal();
            personalList.add(personal);
        }
    }
}
