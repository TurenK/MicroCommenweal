package com.example.hfp.MicroCommonweal.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.ImageUpAndDownUtil;
import com.example.hfp.MicroCommonweal.activity.CharityDetailActivity;
import com.example.hfp.MicroCommonweal.activity.CommitOrgActivity;
import com.example.hfp.MicroCommonweal.object.Charity;

import java.util.List;


public class CharityAdapter_ForComment extends RecyclerView.Adapter<CharityAdapter_ForComment.ViewHolder>{
    private List<Charity> mCharityList;
    private Context context;


    static  class ViewHolder extends  RecyclerView.ViewHolder{
        View Charityview;
        ImageView charityIamge;
        TextView charityName;
        TextView peoplenum;
        TextView status;

        ViewHolder(View view){
            super(view);
            Charityview = view;
            charityIamge = (ImageView) view.findViewById(R.id.charity_iamge);
            charityName = (TextView)view.findViewById(R.id.charity_name);
            peoplenum  = (TextView)view.findViewById(R.id.people_num);
            status= (TextView)view.findViewById(R.id.status);
        }
    }

    public CharityAdapter_ForComment(List<Charity> charityList, Context context){
        this.context = context;
        mCharityList = charityList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.charity_item_for_comment,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.Charityview.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                int position = holder.getAdapterPosition();
                Charity charity = mCharityList.get(position);
                if(charity.getStatus().equals("评价")){
                //Toast.makeText(v.getContext(), "点击了", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("actId",charity.getaID());
                intent.setClass(v.getContext(), CommitOrgActivity.class);
                intent.putExtra("bundle",bundle);
                v.getContext().startActivity(intent);
                }else {
                    Toast.makeText(context, "您已评价该活动", Toast.LENGTH_LONG).show();
                }
            }
        });
        return  holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Charity charity = mCharityList.get(position);
        holder.charityName.setText(charity.getName());
        holder.peoplenum.setText(charity.getPeoplenum());
        if(charity.getStatus().equals("已评价")){
            holder.status.setBackgroundColor(Color.rgb(	112,128,144));
        }
        holder.status.setText(charity.getStatus());
        try {
            getImages(charity,holder.charityIamge);
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public  int getItemCount(){
        return mCharityList.size();
    }

    private void getImages(Charity charity, ImageView imageView){
        new ImageUpAndDownUtil(context).testDownloadImage(charity.getImagepath(),imageView);
    }
}
