package com.example.hfp.changhaowoer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hfp.changhaowoer.R;
import com.example.hfp.changhaowoer.object.Charity;

import java.util.List;


public class CharityAdapter extends RecyclerView.Adapter<CharityAdapter.ViewHolder>{
    private List<Charity> mCharityList;

    static  class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView charityIamge;
        TextView charityName;
        TextView peoplenum;
        TextView status;

        public ViewHolder(View view){
            super(view);
            charityIamge = (ImageView) view.findViewById(R.id.charity_iamge);
            charityName = (TextView)view.findViewById(R.id.charity_name);
            peoplenum  = (TextView)view.findViewById(R.id.people_num);
            status= (TextView)view.findViewById(R.id.status);
        }
    }

    public CharityAdapter(List<Charity> charityList){
        mCharityList = charityList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.charity_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return  holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Charity charity = mCharityList.get(position);
        holder.charityIamge.setImageResource(charity.getIamgeId());
        holder.charityName.setText(charity.getName());
        holder.peoplenum.setText(charity.getPeoplenum());
        holder.status.setText(charity.getStatus());
    }
    @Override
    public  int getItemCount(){
        return mCharityList.size();
    }

}
