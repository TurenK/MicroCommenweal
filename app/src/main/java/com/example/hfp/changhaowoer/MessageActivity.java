package com.example.hfp.changhaowoer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hfp.changhaowoer.adapter.CharityAdapter;
import com.example.hfp.changhaowoer.adapter.MessageAdapter;
import com.example.hfp.changhaowoer.object.Charity;
import com.example.hfp.changhaowoer.object.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    private List<Message> messageList = new ArrayList<>();

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initCharities();//初始化义工
           /*
        ****************************
        初始化义工列表的recycle和adapter
        *****************************
         */
        recyclerView = (RecyclerView)findViewById(R.id.rv_message);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MessageAdapter adapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(adapter);
    }

    private  void initCharities(){
        for(int i =0;i<3;i++){
            Message message = new Message("联合国儿童基金委",R.drawable.avatar1,"您好！您报名的联合国儿童基金委志愿者…",R.drawable.point_red);
            messageList.add(message);
        }
    }
}
