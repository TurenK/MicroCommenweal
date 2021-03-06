package com.example.hfp.MicroCommonweal.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.ImageUpAndDownUtil;
import com.example.hfp.MicroCommonweal.activity.CharityDetailActivity;
import com.example.hfp.MicroCommonweal.activity.MainActivity;
import com.example.hfp.MicroCommonweal.activity.MainUIActivity;
import com.example.hfp.MicroCommonweal.object.Charity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;


public class CharityAdapter extends BaseQuickAdapter<Charity> {

    private Context context;
    private List<Charity> charities;
    private String JOINING = "报名中";
    private String JOINED = "已报名";
    private String OPENING = "报名中";
    private String CLOSED = "已结束";
    private String DUE = "已截止";

    public CharityAdapter(@LayoutRes int layoutResId, @Nullable List<Charity> data, Context context) {
        super(layoutResId, data);
        this.context = context;
        charities = data;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final Charity charity) {
//        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = baseViewHolder.getLayoutPosition();
//                //Toast.makeText(v.getContext(), "点击了", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                intent.setClass(v.getContext(), CharityDetailActivity.class);
//                intent.putExtra("activityID", charity.getaID());
//                v.getContext().startActivity(intent);
//            }
//        });

        baseViewHolder.setText(R.id.charity_name, charity.getName());
        baseViewHolder.setText(R.id.people_num, charity.getPeoplenum());
        baseViewHolder.setText(R.id.status, charity.getStatus());
        if(charity.getStatus().equals("已评价")){
            baseViewHolder.setBackgroundColor(R.id.status, Color.GRAY);
        }else if(charity.getStatus().equals(JOINING)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                baseViewHolder.setBackgroundColor(R.id.status, context.getColor(R.color.unparticipate));
            }
        }else if(charity.getStatus().equals(JOINED)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                baseViewHolder.setBackgroundColor(R.id.status, context.getColor(R.color.participated));
            }
        }else if(charity.getStatus().equals(CLOSED)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                baseViewHolder.setBackgroundColor(R.id.status, context.getColor(R.color.allfinished));
            }
        }else if(charity.getStatus().equals(DUE)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                baseViewHolder.setBackgroundColor(R.id.status, context.getColor(R.color.parfinished));
            }
        }
        try {
            getImages(charity, (ImageView) baseViewHolder.getView(R.id.charity_iamge));
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void removeAllData() {
        while (charities != null && !charities.isEmpty()) {
                remove(0);
        }
    }

    private void getImages(Charity charity, ImageView imageView) {
        new ImageUpAndDownUtil(context).testDownloadImage(charity.getImagepath(), imageView);
    }
}
