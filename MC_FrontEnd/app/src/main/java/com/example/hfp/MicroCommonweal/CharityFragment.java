package com.example.hfp.MicroCommonweal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.example.hfp.MicroCommonweal.object.UserInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CharityFragment extends Fragment {
    private static String TAG = "CharityFragment";
    private String JOINING = "报名中";
    private String JOINED = "已报名";


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

         getPermission();

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
//        for(int i = 0;i<10;i++){
//            Charity charity = new Charity();
//            charity.setaID(""+i);
//            charity.setName("小桔灯");
//            charity.setIamgeId(R.drawable.thumbnail1);
//            charity.setPeoplenum("剩余10人");
//                charity.setStatus(JOINING);
//            charityList.add(charity);
//        }

        requireCharity();
    }

    private  void initCategories(){
        for(int i =0;i<5;i++){
            Category category = new Category(R.drawable.thumbnail21,"青少年服务");
            categoryList.add(category);
        }
    }

    private void requireCharity(){

        String uid = UserInfo.getUserInfo().getuId();

//        btn_login.setEnabled(true);
//        btn_login.setText("登录");

        //创建网络访问对象
        JSONObject main_json = new JSONObject();
        main_json.put("userId", uid);

        Log.d(TAG, main_json.toString());

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(main_json.toJSONString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        AsyncHttpUtil.post(getContext(), this.getString(R.string.URL_MAIN_FRAME), stringEntity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                JSONObject jsonObject = JSONObject.parseObject(content);
                int code = jsonObject.getInteger("code");
                String info = jsonObject.getString("message");
                Log.d(TAG, jsonObject.toString());
                if (code == 200){
                    //TODO get more JSON objects!
                    JSONObject objectdata =jsonObject.getJSONObject("data");
                    for (int i = 1; i <= 10; i++){
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
                            charity.setPeoplenum("剩余"+aSQ+"人");
                            if(actStatus.equals("1") && userStatus==0){
                                charity.setStatus(JOINING);
                            }else if(actStatus.equals("1") && userStatus==1){
                                charity.setStatus(JOINED);
                            }
                            charityList.add(charity);
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(layoutManager);
                        CharityAdapter adapter = new CharityAdapter(charityList,getContext());
                        recyclerView.setAdapter(adapter);
                    }

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
    }

    private void getPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
    }

}
