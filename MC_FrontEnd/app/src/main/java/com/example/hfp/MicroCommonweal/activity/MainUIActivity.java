package com.example.hfp.MicroCommonweal.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hfp.MicroCommonweal.CharityFragment;
import com.example.hfp.MicroCommonweal.MeFragment;
import com.example.hfp.MicroCommonweal.R;
import com.example.hfp.MicroCommonweal.object.Message;
import com.example.hfp.MicroCommonweal.object.UserInfo;

public class MainUIActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = "MainUIActivity";

    // 三个tab布局
    private RelativeLayout charityLayout, publishLayout, meLayout;
    // 底部标签切换的Fragment
    private Fragment charityFragment,meFragment,
            currentFragment;
    // 底部标签图片
    private ImageView charityImg, publishImg, meImg;
    // 底部标签的文本
    private TextView charityTv, meTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ui);
        getPermission();
        initUI();
        initTab();
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "I'm back!");
        if (currentFragment.equals(meFragment)) {
            addOrShowFragment(getSupportFragmentManager().beginTransaction(), currentFragment);
            charityImg.setImageResource(R.drawable.charity_gray);
            charityTv.setTextColor(getResources().getColor(R.color.bottomtab_normal));
            meImg.setImageResource(R.drawable.me);
            meTv.setTextColor(getResources().getColor(R.color.bottomtab_press));
        }else if(currentFragment.equals(charityFragment)){
            addOrShowFragment(getSupportFragmentManager().beginTransaction(), currentFragment);
            charityImg.setImageResource(R.drawable.charity);
            charityTv.setTextColor(getResources().getColor(R.color.bottomtab_press));
            meImg.setImageResource(R.drawable.me_gray);
            meTv.setTextColor(getResources().getColor(R.color.bottomtab_normal));
        }
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        charityLayout = (RelativeLayout) findViewById(R.id.rl_charity);
        publishLayout = (RelativeLayout) findViewById(R.id.rl_publish);
        meLayout = (RelativeLayout) findViewById(R.id.rl_me);
        charityLayout.setOnClickListener(this);
        publishLayout.setOnClickListener(this);
        meLayout.setOnClickListener(this);

        charityImg = (ImageView) findViewById(R.id.iv_charity);
        publishImg = (ImageView) findViewById(R.id.iv_publish);
        meImg = (ImageView) findViewById(R.id.iv_me);
        charityTv = (TextView) findViewById(R.id.tv_charity);
        meTv = (TextView) findViewById(R.id.tv_me);

        }


    /**
     * 初始化底部标签
     */
    private void initTab() {
        if (charityFragment == null) {
            charityFragment = new CharityFragment();
        }

        if (!charityFragment.isAdded()) {
            // 提交事务
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_layout, charityFragment).commit();
            // 记录当前Fragment
            currentFragment = charityFragment;
            // 设置图片文本的变化
            charityImg.setImageResource(R.drawable.charity);
            charityTv.setTextColor(getResources().getColor(R.color.bottomtab_press));
            publishImg.setImageResource(R.drawable.publish);
            meImg.setImageResource(R.drawable.me_gray);
            meTv.setTextColor(getResources().getColor(R.color.bottomtab_normal));

        }

    }



//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        String statuCar = intent.getStringExtra("statuCar");
//        if (!TextUtils.isEmpty(statuCar)) {
//            addOrShowFragment(getSupportFragmentManager().beginTransaction(), charityFragment);
//        }
//    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_charity: // 义工
                clickTab1Layout();
                break;
            case R.id.rl_publish: // 发布
                if (UserInfo.getUserInfo().getType() == UserInfo.CHARITY_ORG){
                    startActivity(new Intent(MainUIActivity.this,PublishActivity.class));
                }else{
                    Toast.makeText(MainUIActivity.this, "个人用户无法创建活动！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.rl_me: // 我的
                clickTab3Layout();
                break;

            default:
                break;
        }
    }

    /**
     * 点击第一个tab
     */
    private void clickTab1Layout() {
        if (charityFragment == null) {
            charityFragment = new CharityFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), charityFragment);

        // 设置底部tab变化
        currentFragment = charityFragment;
        // 设置图片文本的变化
        charityImg.setImageResource(R.drawable.charity);
        charityTv.setTextColor(getResources().getColor(R.color.bottomtab_press));
        meImg.setImageResource(R.drawable.me_gray);
        meTv.setTextColor(getResources().getColor(R.color.bottomtab_normal));
    }

    /**
     * 点击第三个tab
     */
    private void clickTab3Layout() {
        if (meFragment == null) {
            meFragment = new MeFragment();
        }

        // 设置底部tab变化
//        currentFragment = meFragment;
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), meFragment);
        charityImg.setImageResource(R.drawable.charity_gray);
        charityTv.setTextColor(getResources().getColor(R.color.bottomtab_normal));
        meImg.setImageResource(R.drawable.me);
        meTv.setTextColor(getResources().getColor(R.color.bottomtab_press));

    }

    @Override
    public void onRestart() {
        super.onRestart();
        if (currentFragment.equals(meFragment)) {
            addOrShowFragment(getSupportFragmentManager().beginTransaction(), currentFragment);
            charityImg.setImageResource(R.drawable.charity_gray);
            charityTv.setTextColor(getResources().getColor(R.color.bottomtab_normal));
            meImg.setImageResource(R.drawable.me);
            meTv.setTextColor(getResources().getColor(R.color.bottomtab_press));
        }else if(currentFragment.equals(charityFragment)){
            addOrShowFragment(getSupportFragmentManager().beginTransaction(), currentFragment);
            charityImg.setImageResource(R.drawable.charity);
            charityTv.setTextColor(getResources().getColor(R.color.bottomtab_press));
            meImg.setImageResource(R.drawable.me_gray);
            meTv.setTextColor(getResources().getColor(R.color.bottomtab_normal));
        }
    }

    /**
     * 加入或者显示碎片
     *
     * @param transaction
     * @param fragment
     */
    private void addOrShowFragment(FragmentTransaction transaction,
                                   Fragment fragment) {
        if (currentFragment == fragment)
            return;

        if (!fragment.isAdded()) { // 假设当前fragment未被加入，则加入到Fragment管理器中
            transaction.hide(currentFragment)
                    .add(R.id.content_layout, fragment).commit();
        } else {
            transaction.hide(currentFragment).show(fragment).commit();
        }

        currentFragment = fragment;
    }

    private void getPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainUIActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                    1);
        }
    }
}
