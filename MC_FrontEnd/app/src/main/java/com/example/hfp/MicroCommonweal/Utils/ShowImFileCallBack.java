package com.example.hfp.MicroCommonweal.Utils;

import android.content.Context;
import android.widget.ImageView;

import com.example.hfp.MicroCommonweal.R;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;

public class ShowImFileCallBack extends FileCallBack {

    private Context context;
    private ImageView imageView;
    public ShowImFileCallBack(String destFileDir, String destFileName, Context context, ImageView imageView) {
        super(destFileDir, destFileName);
        this.context = context;
        this.imageView = imageView;
    }

    @Override
    public void inProgress(float progress, long total , int id){

    }

    @Override
    public void onError(Call call, Exception e, int id) {
        Picasso.with(context)
                .load(R.drawable.loadfail)
                .into(imageView);
        e.printStackTrace();
    }

    @Override
    public void onResponse(File response, int id) {
        Picasso.with(context)
                .load(response)
                .into(imageView);
    }
}
