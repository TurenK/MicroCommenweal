package com.example.hfp.MicroCommonweal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.object.Message;

import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
    private List<Message> mMessageList;

    static  class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView avatarIamge;
        TextView str_addresser;
        TextView str_message;
        ImageView pointId;

        public ViewHolder(View view){
            super(view);
            avatarIamge = (ImageView) view.findViewById(R.id.image_avatar);
            str_addresser = (TextView)view.findViewById(R.id.str_addresser);
            str_message  = (TextView)view.findViewById(R.id.str_message);
            pointId= (ImageView)view.findViewById(R.id.status);
        }
    }

    public MessageAdapter(List<Message> MessageList){
        mMessageList = MessageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return  holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Message message = mMessageList.get(position);
        holder.str_message.setText(message.getMessage());
        holder.pointId.setImageResource(message.getPointId());
        holder.avatarIamge.setImageResource(message.getAvatarId());
        holder.str_addresser.setText(message.getAddresser());

    }
    @Override
    public  int getItemCount(){
        return mMessageList.size();
    }

}
