package com.example.hfp.MicroCommonweal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.Utils.AsyncHttpUtil;
import com.example.hfp.MicroCommonweal.activity.CharityDetailActivity;
import com.example.hfp.MicroCommonweal.activity.MainActivity;
import com.example.hfp.MicroCommonweal.object.Category;
import com.example.hfp.MicroCommonweal.object.Charity;
import com.example.hfp.MicroCommonweal.object.UserInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> mCategoryList;
    private String JOINING = "报名中";
    private String JOINED = "已报名";
    private String OPENING = "报名中";
    private String CLOSED = "已结束";
    private String DUE = "已截止";
    private static String TAG = "CategoryAdapter";
    private Context context;
    private CharityAdapter listadapter;
    //义工列表
    private List<Charity> charityList = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        View Categoryview;
        ImageView categoryImage;
        TextView categoryName;

        public ViewHolder(View view) {
            super(view);
            Categoryview = view;
            categoryImage = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
            categoryName = (TextView) view.findViewById(R.id.id_index_gallery_item_text);
        }

    }

    public CategoryAdapter(List<Category> categoryList, Context context, CharityAdapter listadapter) {
        mCategoryList = categoryList;
        this.context = context;
        this.listadapter = listadapter;
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.charity_category_item, parent, false);
        final CategoryAdapter.ViewHolder holder = new CategoryAdapter.ViewHolder(view);
        holder.Categoryview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                requireCharity(position);
            }
        });
        return holder;

    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, int position) {
        Category category = mCategoryList.get(position);
        holder.categoryName.setText(category.getCotegory_name());
        holder.categoryImage.setImageResource(category.getImage());

    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    private void requireCharity(int category) {

        String uid = UserInfo.getUserInfo().getuId();

//        btn_login.setEnabled(true);
//        btn_login.setText("登录");

        //创建网络访问对象
        JSONObject main_json = new JSONObject();
        main_json.put("userId", uid);
        main_json.put("category", category);

        Log.d(TAG, main_json.toString());

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(main_json.toJSONString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        AsyncHttpUtil.post(context, context.getString(R.string.URL_MAIN_FRAME), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d(TAG, jsonObject.toString());
                if (code == 200) {
                    //TODO get more JSON objects!
                    JSONObject objectdata = jsonObject.getJSONObject("data");
                    List<Charity> charities = new ArrayList<>();
                    for (int i = 0; i <= 30; i++) {
                        if (objectdata.containsKey(String.valueOf(i))) {
                            JSONObject object = objectdata.getJSONObject(String.valueOf(i));
                            String actID = object.getString("activityId");
                            String actName = object.getString("activityName");
                            String actImage = object.getString("activityImage");
                            String aSQ = object.getString("aSurplusQuota");
                            String aNN = object.getString("aNeedNumOfPerson");
                            String actStatus = object.getString("activityStatus");
                            int userStatus = object.getInteger("userStatus");
                            //TODO create a Charity object
                            Charity charity = new Charity();
                            charity.setaID(actID);
                            charity.setName(actName);
                            charity.setImagepath(actImage);
                            charity.setPeoplenum("剩余" + aSQ + "人");
//                            switch (actStatus) {
//                                case "0":
//                                    charity.setStatus(CLOSED);
//                                    break;
//                                case "1":
//                                    charity.setStatus(OPENING);
//                                    break;
//                                case "2":
//                                    charity.setStatus(DUE);
//                                    break;
//                            }
                            if (actStatus.equals("1") && userStatus == 0) {
                                charity.setStatus(JOINING);
                            } else if (actStatus.equals("1") && userStatus == 1) {
                                charity.setStatus(JOINED);
                            } else if (actStatus.equals("0")) {
                                charity.setStatus(CLOSED);
                            } else if (actStatus.equals("2")) {
                                charity.setStatus(DUE);
                            }
                            charities.add(charity);
                        }
                    }
                    listadapter.removeAllData();
                    listadapter.addData(charities);
                } else {
                    Toast.makeText(context, "获取活动失败！请稍后再试", Toast.LENGTH_LONG).show();
                }
//                super.onSuccess(content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d(TAG, "cannot connect to server!");
                Toast.makeText(context, "无法连接到服务器！", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
            }
        });
    }
}
