package com.example.hfp.MicroCommonweal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.AsyncHttpUtil;
import com.example.hfp.MicroCommonweal.Utils.ImageUpAndDownUtil;
import com.example.hfp.MicroCommonweal.activity.CharityDetailActivity;
import com.example.hfp.MicroCommonweal.activity.MainActivity;
import com.example.hfp.MicroCommonweal.object.Category;
import com.example.hfp.MicroCommonweal.object.Charity;
import com.example.hfp.MicroCommonweal.object.UserInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class CategoryAdapter extends BaseQuickAdapter<Category> {

    private static String TAG = "CategoryAdapter";
    private Context context;
    private List<Category> charities;

    public CategoryAdapter(@LayoutRes int layoutResId, @Nullable List<Category> data, Context context) {
        super(layoutResId, data);
        this.context = context;
        this.charities = data;
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, Category category) {
        baseViewHolder.setText(R.id.id_index_gallery_item_text, category.getCotegory_name());
        baseViewHolder.setImageResource(R.id.id_index_gallery_item_image,category.getImage());
    }

    public void removeAllData() {
        while (charities != null && !charities.isEmpty()) {
            remove(0);
        }
    }

    private void getImages(String url, ImageView imageView) {
        new ImageUpAndDownUtil(context).testDownloadImage(url, imageView);
    }
}
