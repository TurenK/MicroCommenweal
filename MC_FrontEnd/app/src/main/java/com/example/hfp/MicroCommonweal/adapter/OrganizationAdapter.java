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
import com.example.hfp.MicroCommonweal.object.Message;
import com.example.hfp.MicroCommonweal.object.Organization;

import java.util.List;

public class OrganizationAdapter extends BaseQuickAdapter<Organization> {
    private Context context;
    public OrganizationAdapter(@LayoutRes int layoutResId, @Nullable List<Organization> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    private void getImages(Organization organization, ImageView imageView){
        new ImageUpAndDownUtil(context).testDownloadImage(organization.getAvatorurl(),imageView);
    }

    @Override
    protected void convert(BaseViewHolder helper, Organization organization) {
        //可链式调用赋值
        helper.setText(R.id.comment_org_name, organization.getOrgname());
        getImages(organization,(ImageView) helper.getView(R.id.comment_iamge));

        //获取当前条目position/
        //int position = helper.getLayoutPosition();
    }
}
