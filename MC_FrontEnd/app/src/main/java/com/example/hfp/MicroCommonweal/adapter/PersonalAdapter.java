package com.example.hfp.MicroCommonweal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.ImageUpAndDownUtil;
import com.example.hfp.MicroCommonweal.activity.PersonalInfoActivity;
import com.example.hfp.MicroCommonweal.object.Personal;

import java.util.List;

public class PersonalAdapter extends BaseQuickAdapter<Personal> {
    private Context context;

    public PersonalAdapter(@LayoutRes int layoutResId, @Nullable List<Personal> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(BaseViewHolder helper, Personal personal) {
        //可链式调用赋值
        getImages(personal,(ImageView) helper.getView(R.id.image_avatar));
        //获取当前条目position/
        final int position = helper.getLayoutPosition();
        RatingBar ratingBar = helper.getView(R.id.ratingbar);
        final TextView textView = helper.getView(R.id.str_message);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.d("rating test", rating+"");
                getData().get(position).setGrade((int)rating);
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inputComment= new EditText(context);
                inputComment.setText(textView.getText().toString());
                AlertDialog.Builder builder_userid = new AlertDialog.Builder(context);
                builder_userid.setTitle("评价").setView(inputComment)
                        .setNegativeButton("取消", null);
                builder_userid.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        textView.setText( inputComment.getText().toString());
                        getData().get(position).setCommittext(inputComment.getText().toString());
                    }
                });
                builder_userid.show();
            }
        });

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
