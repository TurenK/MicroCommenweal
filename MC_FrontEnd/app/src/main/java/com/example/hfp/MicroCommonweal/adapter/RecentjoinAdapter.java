package com.example.hfp.MicroCommonweal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.object.Recentjoin;

import java.util.List;

public class RecentjoinAdapter extends RecyclerView.Adapter<RecentjoinAdapter.ViewHolder>{
    private List<Recentjoin> mRecnetList;

    static  class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView avatarIamge;


        public ViewHolder(View view){
            super(view);
            avatarIamge = (ImageView) view.findViewById(R.id.imageView6);
        }
    }

    public RecentjoinAdapter(List<Recentjoin> recentjoinList){
        mRecnetList = recentjoinList;
    }

    @Override
    public RecentjoinAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recently_join_item,parent,false);
        RecentjoinAdapter.ViewHolder holder = new RecentjoinAdapter.ViewHolder(view);
        return  holder;

    }

    @Override
    public void onBindViewHolder(RecentjoinAdapter.ViewHolder holder, int position){
        Recentjoin recentjoin = mRecnetList.get(position);
        holder.avatarIamge.setImageResource(recentjoin.getImageId());

    }
    @Override
    public  int getItemCount(){
        return mRecnetList.size();
    }

}
