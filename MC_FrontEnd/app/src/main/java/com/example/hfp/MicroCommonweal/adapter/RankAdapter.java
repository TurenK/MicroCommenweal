package com.example.hfp.MicroCommonweal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.object.Rank;

import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder>{
    private List<Rank> mRankList;
    static  class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView avatarIamge;
        TextView rank;
        TextView donatenumber;

        public ViewHolder(View view){
            super(view);
            avatarIamge = (ImageView) view.findViewById(R.id.image_avatar);
            rank = (TextView)view.findViewById(R.id.str_ranking);
            donatenumber  = (TextView)view.findViewById(R.id.str_num_bean);
        }
    }

    public RankAdapter(List<Rank> RankList){
        mRankList = RankList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return  holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Rank rank = mRankList.get(position);
        holder.rank.setText(rank.getRank());
        holder.avatarIamge.setImageResource(rank.getAvatar());
        holder.donatenumber.setText(rank.getDonatenumber());

    }
    @Override
    public  int getItemCount(){
        return mRankList.size();
    }
}
