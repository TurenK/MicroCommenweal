package com.example.hfp.changhaowoer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hfp.changhaowoer.R;
import com.example.hfp.changhaowoer.object.Category;

import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private List<Category> mCategoryList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView categoryImage;
        TextView categoryName;

        public  ViewHolder(View view){
            super(view);
            categoryImage = (ImageView)view.findViewById(R.id.id_index_gallery_item_image);
            categoryName = (TextView)view.findViewById(R.id.id_index_gallery_item_text);
        }

    }

    public CategoryAdapter(List<Category> categoryList){mCategoryList = categoryList;}

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.charity_category_item,parent,false);
        CategoryAdapter.ViewHolder holder = new CategoryAdapter.ViewHolder(view);
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
