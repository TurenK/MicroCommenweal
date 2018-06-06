package com.example.hfp.MicroCommonweal.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.object.Message;

import java.util.List;


public class MessageAdapter extends BaseQuickAdapter<Message> {
    public MessageAdapter(@LayoutRes int layoutResId, @Nullable List<Message> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Message message) {
        //可链式调用赋值
        helper.setText(R.id.str_addresser, message.getAddresser())
                .setText(R.id.str_message, message.getMessage())
                .setImageResource(R.id.image_avatar, R.drawable.avatar1);

        //获取当前条目position/
        //int position = helper.getLayoutPosition();

    }
}
