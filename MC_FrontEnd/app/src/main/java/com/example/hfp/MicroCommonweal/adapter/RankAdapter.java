package com.example.hfp.MicroCommonweal.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.object.Charity;
import com.example.hfp.MicroCommonweal.object.Rank;

import java.util.List;

public class RankAdapter extends BaseQuickAdapter<Rank> {
    private List<Rank> mRankList;
    private Context context;

    public RankAdapter(@LayoutRes int layoutResId, @Nullable List<Rank> data, Context context){
        super(layoutResId, data);
        mRankList = data;
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final Rank rank) {
        baseViewHolder.setText(R.id.str_ranking, rank.getRank());
        baseViewHolder.setText(R.id.str_num_bean, rank.getDonatenumber());
        baseViewHolder.setImageResource(R.id.image_touxiangkuang, rank.getAvatar());

    }

    public void removeAllData() {
        while (mRankList != null && !mRankList.isEmpty()) {
            remove(0);
        }
    }

    @Override
    public  int getItemCount(){
        return mRankList.size();
    }
}
