package com.example.hfp.MicroCommonweal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.hfp.MicroCommonweal.Utils.AsyncHttpUtil;
import com.example.hfp.MicroCommonweal.activity.MessageActivity;
import com.example.hfp.MicroCommonweal.adapter.CategoryAdapter;
import com.example.hfp.MicroCommonweal.adapter.CharityAdapter;
import com.example.hfp.MicroCommonweal.object.Category;
import com.example.hfp.MicroCommonweal.object.Charity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CharityFragment extends Fragment {
    private static String TAG = "CharityFragment";

    private View charityLayout;
    private ViewPager mViewPaper;
    private List<ImageView> images;
    private List<View> dots;
    private int currentItem;
    //记录上一次点的位置
    private int oldPosition = 0;
    //存放图片的id
    private int[] imageIds = new int[]{
            R.drawable.img4,
            R.drawable.img3,
            R.drawable.img2,
            R.drawable.img1,
            R.drawable.img
    };
    //存放图片的标题
    private String[] titles = new String[]{
            "轮播1",
            "轮播2",
            "轮播3",
            "轮播4",
            "轮播5"
    };
    private TextView title;
    private ViewPagerAdapter adapter;
    private ScheduledExecutorService scheduledExecutorService;
    //义工列表
    private List<Charity> charityList = new ArrayList<>();
    //分類列表
    private List<Category> categoryList = new ArrayList<>();
    private Button btnMessage;
    //recyclerview控件
    RecyclerView recyclerView;
    RecyclerView recyclerView_category;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_charity, container, false);
         charityLayout  = inflater.inflate(R.layout.fragment_charity, container, false);


        btnMessage = (Button)charityLayout.findViewById(R.id.button_message);
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View charityLayout) {
                Intent intent = new Intent(getActivity(),MessageActivity.class);
                startActivity(intent);
            }
        });

        setView();

        initCategories();//初始化分類
        initCharities();//初始化义工


        //初始化义工列表的recycle和adapter
        recyclerView = (RecyclerView) charityLayout.findViewById(R.id.rv_charity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        CharityAdapter adapter = new CharityAdapter(charityList);
        recyclerView.setAdapter(adapter);


        //初始化分類列表recycle和adpater
        recyclerView_category = (RecyclerView)charityLayout.findViewById(R.id.rv_charity_category);
        LinearLayoutManager layoutManager_category = new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager_category.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_category.setLayoutManager(layoutManager_category);
        CategoryAdapter adapter_category = new CategoryAdapter(categoryList);
        recyclerView_category.setAdapter(adapter_category);



        return  charityLayout;
    }

    private void setView(){
        mViewPaper = (ViewPager)charityLayout.findViewById(R.id.vp);

        //显示的图片
        images = new ArrayList<ImageView>();
        for(int i = 0; i < imageIds.length; i++){
            ImageView imageView = new ImageView(getActivity());
            imageView.setBackgroundResource(imageIds[i]);
            images.add(imageView);
        }
        //显示的小点
        dots = new ArrayList<View>();
        dots.add(charityLayout.findViewById(R.id.dot_0));
        dots.add(charityLayout.findViewById(R.id.dot_1));
        dots.add(charityLayout.findViewById(R.id.dot_2));
        dots.add(charityLayout.findViewById(R.id.dot_3));
        dots.add(charityLayout.findViewById(R.id.dot_4));

        title = (TextView) charityLayout.findViewById(R.id.title);
        title.setText(titles[0]);

        adapter = new ViewPagerAdapter();
        mViewPaper.setAdapter(adapter);

        mViewPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                title.setText(titles[position]);
                dots.get(position).setBackgroundResource(R.drawable.dot_yes);
                dots.get(oldPosition).setBackgroundResource(R.drawable.dot_no);

                oldPosition = position;
                currentItem = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    /*定义的适配器*/
    public class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            // TODO Auto-generated method stub
//          super.destroyItem(container, position, object);
//          view.removeView(view.getChildAt(position));
//          view.removeViewAt(position);
            view.removeView(images.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            // TODO Auto-generated method stub
            view.addView(images.get(position));
            return images.get(position);
        }

    }

    /**
     * 利用线程池定时执行动画轮播
     */
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(
                new ViewPageTask(),
                2,
                2,
                TimeUnit.SECONDS);
    }


    /**
     * 图片轮播任务
     * @author liuyazhuang
     *
     */
    private class ViewPageTask implements Runnable{

        @Override
        public void run() {
            currentItem = (currentItem + 1) % imageIds.length;
            mHandler.sendEmptyMessage(0);
        }
    }

    /**
     * 接收子线程传递过来的数据
     */
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            mViewPaper.setCurrentItem(currentItem);
        };
    };
    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if(scheduledExecutorService != null){
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
    }



    private  void initCharities(){
        for(int i =0;i<7;i++){
            Charity charity = new Charity("同饮一湖清水，共享生态文明，保护水库环境做文明市民签名活动",R.drawable.thumbnail1,"10人报名","报名中");
            charityList.add(charity);
        }
    }

    private  void initCategories(){
        for(int i =0;i<5;i++){
            Category category = new Category(R.drawable.thumbnail21,"青少年服务");
            categoryList.add(category);
        }
    }

    private boolean requireCharity(){
        AsyncHttpUtil.post(getContext(), this.getString(R.string.URL_MAIN_FRAME), null, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d(TAG, info);
                if (code == 200){
                    //TODO get more JSON objects!
                    JSONObject object = jsonObject.getJSONObject("data");
                    String actName = object.getString("activityName");
                    String actImage = object.getString("activityImage");
                    String aSQ = object.getString("aSurplusQuota");
                    String actStatus = object.getString("activityStatus");
                    //TODO create a Charity object

                }else if(code == 400){
                    Toast.makeText(getContext(), "wrong account or password!", Toast.LENGTH_LONG).show();
                }
//                super.onSuccess(content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d(TAG, "cannot connect to server!");
                Toast.makeText(getContext(), "cannot connect to server!", Toast.LENGTH_LONG).show();
//                super.onFailure(error, content);
            }
        });
        return true;
    }

}
