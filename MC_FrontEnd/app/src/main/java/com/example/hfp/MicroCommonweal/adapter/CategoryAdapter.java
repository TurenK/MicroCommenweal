package com.example.hfp.MicroCommonweal.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.activity.CharityDetailActivity;
import com.example.hfp.MicroCommonweal.object.Category;
import com.example.hfp.MicroCommonweal.object.Charity;

import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private List<Category> mCategoryList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View Categoryview;
        ImageView categoryImage;
        TextView categoryName;

        public  ViewHolder(View view){
            super(view);
            Categoryview = view;
            categoryImage = (ImageView)view.findViewById(R.id.id_index_gallery_item_image);
            categoryName = (TextView)view.findViewById(R.id.id_index_gallery_item_text);
        }

    }

    public CategoryAdapter(List<Category> categoryList){mCategoryList = categoryList;}

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.charity_category_item,parent,false);
        final CategoryAdapter.ViewHolder holder = new CategoryAdapter.ViewHolder(view);
        holder.Categoryview.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                int position = holder.getAdapterPosition();

            }
        });
        return  holder;

    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, int position){
        Category category = mCategoryList.get(position);
        holder.categoryName.setText(category.getCotegory_name());
        holder.categoryImage.setImageResource(category.getImage());

    }
    @Override
    public  int getItemCount(){
        return mCategoryList.size();
    }

}
