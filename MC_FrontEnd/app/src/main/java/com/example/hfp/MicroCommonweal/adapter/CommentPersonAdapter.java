package com.example.hfp.MicroCommonweal.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.ImageUpAndDownUtil;
import com.example.hfp.MicroCommonweal.object.Personal;

import java.util.List;

public class CommentPersonAdapter extends BaseQuickAdapter<Personal> {
    private Context context;
    public CommentPersonAdapter(@LayoutRes int layoutResId, @Nullable List<Personal> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Personal personal) {
        //可链式调用赋值
        helper.setText(R.id.str_message, personal.getCommittext())
                .setRating(R.id.ratingbar,personal.getGrade());
                //.setImageResource(R.id.image_avatar, R.drawable.avatar1);
        getImages(personal,(ImageView) helper.getView(R.id.image_avatar));
        //获取当前条目position/
        //int position = helper.getLayoutPosition();

    }

    private void getImages(Personal personal, ImageView imageView){
        new ImageUpAndDownUtil(context).testDownloadImage(personal.getAvatorurl(),imageView);
    }
}

