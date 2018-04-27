package com.example.hfp.changhaowoer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainUIActivity extends AppCompatActivity implements View.OnClickListener {
    // 三个tab布局
    private RelativeLayout charityLayout,publishLayout, meLayout;
    // 底部标签切换的Fragment
    private Fragment charityFragment,meFragment,
            currentFragment;
    // 底部标签图片
    private ImageView charityImg, publishImg, meImg;
    // 底部标签的文本
    private TextView charityTv, meTv;
    private FragmentManager mFragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ui);
        initUI();
        initTab();
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
//开启一个事务
//            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
//            fragmentTransaction.add(R.id.content_layout, charityFragment);
//            fragmentTransaction.addToBackStack(null);
//            //提交事务
//            fragmentTransaction.commit();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_charity: // 义工
                clickTab1Layout();
                break;
            case R.id.rl_publish: // 发布
                new AlertDialog.Builder(this)
                        .setTitle("标题")
                        .setMessage("简单的消息提示框")
                        .setPositiveButton("确定", null)
                        .show();
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

        addOrShowFragment(getSupportFragmentManager().beginTransaction(), meFragment);
        charityImg.setImageResource(R.drawable.charity_gray);
        charityTv.setTextColor(getResources().getColor(R.color.bottomtab_normal));
        meImg.setImageResource(R.drawable.me);
        meTv.setTextColor(getResources().getColor(R.color.bottomtab_press));

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
}
