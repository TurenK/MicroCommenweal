package com.example.hfp.MicroCommonweal.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.ImageUpAndDownUtil;
import com.example.hfp.MicroCommonweal.object.Charity;

import java.util.List;


public class PerCommitAdapter extends BaseQuickAdapter<Charity> {
    private Context context;
    public PerCommitAdapter(@LayoutRes int layoutResId, @Nullable List<Charity> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Charity charity) {
        //可链式调用赋值
        helper.setText(R.id.charity_name, charity.getName())
              .setText(R.id.people_num,charity.getPeoplenum())
                .setText(R.id.status,charity.getStatus());
         getImages(charity,(ImageView) helper.getView(R.id.charity_iamge));
        //获取当前条目position/
        //int position = helper.getLayoutPosition();

    }
    private void getImages(Charity charity, ImageView imageView){
        new ImageUpAndDownUtil(context).testDownloadImage(charity.getImagepath(),imageView);
    }
}
