package com.example.hfp.changhaowoer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.hfp.changhaowoer.activity.MessageActivity;
import com.example.hfp.changhaowoer.adapter.CategoryAdapter;
import com.example.hfp.changhaowoer.adapter.CharityAdapter;
import com.example.hfp.changhaowoer.object.Category;
import com.example.hfp.changhaowoer.object.Charity;

import java.util.ArrayList;
import java.util.List;


public class CharityFragment extends Fragment {
    //义工列表
    private List<Charity> charityList = new ArrayList<>();
    //分類列表
    private List<Category> categoryList = new ArrayList<>();
    private Button btnMessage;
    //recyclerview控件
    RecyclerView recyclerView;
    RecyclerView recyclerView_category;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_charity, container, false);
        View charityLayout  = inflater.inflate(R.layout.fragment_charity, container, false);
        btnMessage = (Button)charityLayout.findViewById(R.id.button_message);
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View charityLayout) {
                Intent intent = new Intent(getActivity(),MessageActivity.class);
                startActivity(intent);
            }
        });


        initCategories();//初始化分類
        initCharities();//初始化义工


        //初始化义工列表的recycle和adapter
        recyclerView = (RecyclerView) charityLayout.findViewById(R.id.rv_charity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        CharityAdapter adapter = new CharityAdapter(charityList);
        recyclerView.setAdapter(adapter);


        //初始化分類列表recycle和adpater
        recyclerView_category = (RecyclerView)charityLayout.findViewById(R.id.rv_charity_category);
        LinearLayoutManager layoutManager_category = new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager_category.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_category.setLayoutManager(layoutManager_category);
        CategoryAdapter adapter_category = new CategoryAdapter(categoryList);
        recyclerView_category.setAdapter(adapter_category);



        return  charityLayout;
    }

    private  void initCharities(){
        for(int i =0;i<7;i++){
            Charity charity = new Charity("同饮一湖清水，共享生态文明，保护水库环境做文明市民签名活动",R.drawable.thumbnail1,"10人报名","报名中");
            charityList.add(charity);
        }
    }

    private  void initCategories(){
        for(int i =0;i<5;i++){
            Category category = new Category(R.drawable.thumbnail21,"青少年服务");
            categoryList.add(category);

        }
    }

}
