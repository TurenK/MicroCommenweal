package com.example.hfp.changhaowoer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hfp.changhaowoer.adapter.CharityAdapter;
import com.example.hfp.changhaowoer.object.Charity;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;


public class CharityFragment extends Fragment {
    //义工列表
    private List<Charity> charityList = new ArrayList<>();
    //recyclerview控件
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_charity, container, false);
        View charityLayout  = inflater.inflate(R.layout.fragment_charity, container, false);

        initCharities();//初始化义工
        /*
        ****************************
        初始化义工列表的recycle和adapter
        *****************************
         */
        recyclerView = (RecyclerView) charityLayout.findViewById(R.id.rv_charity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        CharityAdapter adapter = new CharityAdapter(charityList);
        recyclerView.setAdapter(adapter);
        return  charityLayout;
    }

    private  void initCharities(){
        for(int i =0;i<3;i++){
            Charity charity = new Charity("同饮一湖清水，共享生态文明，保护水库环境做文明市民签名活动",R.drawable.logo,"10人报名","报名中");
            charityList.add(charity);
        }
    }

}
