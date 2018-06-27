package com.example.hfp.MicroCommonweal.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.ImageUpAndDownUtil;
import com.example.hfp.MicroCommonweal.activity.CharityDetailActivity;
import com.example.hfp.MicroCommonweal.activity.PersonInfoActivity;
import com.example.hfp.MicroCommonweal.object.Charity;
import com.example.hfp.MicroCommonweal.object.Recentjoin;

import java.util.List;

public class RecentjoinAdapter extends RecyclerView.Adapter<RecentjoinAdapter.ViewHolder>{
    private List<Recentjoin> mRecnetList;

    static  class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView avatarIamge;
        View recentJoinView;


        ViewHolder(View view){
            super(view);
            recentJoinView = view;
            avatarIamge = (ImageView) view.findViewById(R.id.imageView6);
        }

    }

    public RecentjoinAdapter(List<Recentjoin> recentjoinList){
        mRecnetList = recentjoinList;
    }

    @Override
    public RecentjoinAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recently_join_item,parent,false);
        final RecentjoinAdapter.ViewHolder holder = new RecentjoinAdapter.ViewHolder(view);
        holder.recentJoinView.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                int position = holder.getAdapterPosition();
                Recentjoin recentjoin = mRecnetList.get(position);
//                Toast.makeText(v.getContext(), "点击了" + recentjoin.getUid(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.setClass(v.getContext(), PersonInfoActivity.class);
                intent.putExtra("userID", recentjoin.getUid());
                v.getContext().startActivity(intent);
            }
        });
        return  holder;

    }

    @Override
    public void onBindViewHolder(RecentjoinAdapter.ViewHolder holder, int position){
        Recentjoin recentjoin = mRecnetList.get(position);
        String avatar = recentjoin.getAvatar();
        new ImageUpAndDownUtil(holder.avatarIamge.getContext()).testDownloadImage(avatar, holder.avatarIamge);
    }
    @Override
    public  int getItemCount(){
        return mRecnetList.size();
    }

}
