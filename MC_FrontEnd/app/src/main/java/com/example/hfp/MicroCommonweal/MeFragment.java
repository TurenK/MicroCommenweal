package com.example.hfp.MicroCommonweal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.hfp.MicroCommonweal.activity.CollectionActivity;
import com.example.hfp.MicroCommonweal.activity.IdentifyInfoActivity;
import com.example.hfp.MicroCommonweal.activity.JoinedCharityActivity;
import com.example.hfp.MicroCommonweal.activity.MainActivity;
import com.example.hfp.MicroCommonweal.activity.MainUIActivity;
import com.example.hfp.MicroCommonweal.activity.MessageActivity;
import com.example.hfp.MicroCommonweal.activity.PersonalInfoActivity;
import com.example.hfp.MicroCommonweal.activity.PublishedCharityActivity;
import com.example.hfp.MicroCommonweal.activity.RankActivity;
import com.example.hfp.MicroCommonweal.activity.RegisterActivity;


public class MeFragment extends Fragment implements View.OnClickListener  {

    private View meLayout;
    private Button btn_collection;
    private Button btn_initiate_project;
    private Button btn_join_project;
    private Button btn_evaluate;
    private Button btn_message;
    private Button btn_rank;
    private Button btn_set_account;
    private Button btn_approve_info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_me, container, false);
        meLayout = inflater.inflate(R.layout.fragment_me, container, false);

        //获取控件id
        btn_collection = (Button)meLayout.findViewById(R.id.button_like);
        btn_initiate_project = (Button)meLayout.findViewById(R.id.button_useless);
        btn_join_project= (Button)meLayout.findViewById(R.id.button_join);
        btn_evaluate= (Button)meLayout.findViewById(R.id.button_orgContent);
        btn_message= (Button)meLayout.findViewById(R.id.button_orgMessage);
        btn_rank= (Button)meLayout.findViewById(R.id.button_orgRank);
        btn_set_account= (Button)meLayout.findViewById(R.id.button_orgSetting);
        btn_approve_info= (Button)meLayout.findViewById(R.id.button_orgAuthen);

        //设置控件监听器
        btn_collection.setOnClickListener(this);
        btn_initiate_project.setOnClickListener(this);
        btn_join_project.setOnClickListener(this);
        btn_evaluate.setOnClickListener(this);
        btn_message.setOnClickListener(this);
        btn_rank.setOnClickListener(this);
        btn_set_account.setOnClickListener(this);
        btn_approve_info.setOnClickListener(this);
        return meLayout;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_like:
                startActivity(new Intent(getContext(),CollectionActivity.class));
                break;
            case R.id.button_useless:
                startActivity(new Intent(getContext(),PublishedCharityActivity.class));
                break;
            case R.id.button_join:
                startActivity(new Intent(getContext(),JoinedCharityActivity.class));
                break;
            case R.id.button_orgContent:
                Toast.makeText(getContext(), "评价处理", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_orgMessage:
                startActivity(new Intent(getContext(),MessageActivity.class));
                break;
            case R.id.button_orgRank:
                startActivity(new Intent(getContext(),RankActivity.class));
                break;
            case R.id.button_orgSetting:
                startActivity(new Intent(getContext(),PersonalInfoActivity.class));
                break;
            case R.id.button_orgAuthen:
                startActivity(new Intent(getContext(),IdentifyInfoActivity.class));
                break;
        }

    }

}
