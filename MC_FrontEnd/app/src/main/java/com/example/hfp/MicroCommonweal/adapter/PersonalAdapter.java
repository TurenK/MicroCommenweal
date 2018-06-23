package com.example.hfp.MicroCommonweal.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.ImageUpAndDownUtil;
import com.example.hfp.MicroCommonweal.object.Personal;

import java.util.List;

public class PersonalAdapter extends BaseQuickAdapter<Personal> {
    private Context context;
    public PersonalAdapter(@LayoutRes int layoutResId, @Nullable List<Personal> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Personal personal) {
        //可链式调用赋值
        getImages(personal,(ImageView) helper.getView(R.id.image_avatar));
        //获取当前条目position/
        //int position = helper.getLayoutPosition();

    }
    private void getImages(Personal personal, ImageView imageView){
        new ImageUpAndDownUtil(context).testDownloadImage(personal.getAvatorurl(),imageView);
    }

    public View getViewByPosition(RecyclerView recyclerView, int position, @IdRes int viewId) {

        if (recyclerView == null) {
            return null;
        }

        BaseViewHolder viewHolder = (BaseViewHolder) recyclerView.findViewHolderForLayoutPosition(position);
        if (viewHolder == null) {
            return null;
        }
        return viewHolder.getView(viewId);
    }
}
