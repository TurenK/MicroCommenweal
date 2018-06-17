package com.example.hfp.MicroCommonweal.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.object.Message;
import com.example.hfp.MicroCommonweal.object.Organization;

import java.util.List;

public class OrganizationAdapter extends BaseQuickAdapter<Organization> {
    public OrganizationAdapter(@LayoutRes int layoutResId, @Nullable List<Organization> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Organization organization) {
        //可链式调用赋值
        helper.setText(R.id.comment_org_name, organization.getOrgname())
                .setImageResource(R.id.comment_iamge, R.drawable.avartar_org);

        //获取当前条目position/
        //int position = helper.getLayoutPosition();

    }
}
