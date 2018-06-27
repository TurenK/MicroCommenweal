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
import com.example.hfp.MicroCommonweal.object.Message;

import java.util.List;

public class OrgCommentCharityAdapter extends BaseQuickAdapter<Charity> {
    private Context context;
    public OrgCommentCharityAdapter(@LayoutRes int layoutResId, @Nullable List<Charity> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Charity charity) {
        //可链式调用赋值
        helper.setText(R.id.charity_name, charity.getName());
        RatingBar ratingBar = (RatingBar) helper.getView(R.id.ratingbar);
        ratingBar.setNumStars(charity.getActcom());
               // .setImageResource(R.id.charity_iamge, R.drawable.background1);

        //获取当前条目position/
        //int position = helper.getLayoutPosition();

        getImages(charity.getImagepath(),(ImageView) helper.getView(R.id.charity_iamge));

    }
    private void getImages(String url, ImageView imageView){
        new ImageUpAndDownUtil(context).testDownloadImage(url,imageView);
    }
}
