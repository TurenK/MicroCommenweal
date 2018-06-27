package com.example.hfp.MicroCommonweal.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.ImageUpAndDownUtil;
import com.example.hfp.MicroCommonweal.object.Charity;
import com.example.hfp.MicroCommonweal.object.Personal;

import java.util.List;

public class CharityCommentListAdapter extends BaseQuickAdapter<Personal> {
    private Context context;
    public CharityCommentListAdapter(@LayoutRes int layoutResId, @Nullable List<Personal> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Personal charity) {
        //可链式调用赋值
        helper.setText(R.id.username, charity.getUname());
        helper.setText(R.id.str_message, charity.getCommittext());
        RatingBar ratingBar = (RatingBar) helper.getView(R.id.ratingbar);
        ratingBar.setNumStars(charity.getGrade());
               // .setImageResource(R.id.charity_iamge, R.drawable.background1);

        //获取当前条目position/
        //int position = helper.getLayoutPosition();

        getImages(charity.getAvatorurl(),(ImageView) helper.getView(R.id.image_avatar));

    }
    private void getImages(String url, ImageView imageView){
        new ImageUpAndDownUtil(context).testDownloadImage(url,imageView);
    }
}
