package com.example.hfp.MicroCommonweal.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.object.Personal;

import java.util.List;

public class CommentPersonAdapter extends BaseQuickAdapter<Personal> {
    public CommentPersonAdapter(@LayoutRes int layoutResId, @Nullable List<Personal> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Personal personal) {
        //可链式调用赋值
        helper.setText(R.id.str_message, personal.getCommittext())
                .setRating(R.id.ratingbar,personal.getGrade());
                //.setImageResource(R.id.image_avatar, R.drawable.avatar1);

        //获取当前条目position/
        //int position = helper.getLayoutPosition();

    }
}

