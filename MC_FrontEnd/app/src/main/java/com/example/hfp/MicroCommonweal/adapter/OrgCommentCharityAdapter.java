package com.example.hfp.MicroCommonweal.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.object.Charity;
import com.example.hfp.MicroCommonweal.object.Message;

import java.util.List;

public class OrgCommentCharityAdapter extends BaseQuickAdapter<Charity> {
    public OrgCommentCharityAdapter(@LayoutRes int layoutResId, @Nullable List<Charity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Charity charity) {
        //可链式调用赋值
        helper.setText(R.id.charity_name, charity.getName());
               // .setImageResource(R.id.charity_iamge, R.drawable.background1);

        //获取当前条目position/
        //int position = helper.getLayoutPosition();

    }
}
