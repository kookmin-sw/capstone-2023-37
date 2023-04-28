package com.recommend.application.activity;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gyf.immersionbar.ImmersionBar;
import com.recommend.application.R;
import com.recommend.application.base.BaseActivity;
import com.recommend.application.fragment.ChatFragment;
import com.recommend.application.fragment.HomeFragment;
import com.recommend.application.fragment.MineFragment;
import com.recommend.application.fragment.PostFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;

    private HomeFragment homeFragment;
    private PostFragment postFragment;
    private ChatFragment chatFragment;
    private MineFragment mineFragment;

    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        if (isDark) {
            ImmersionBar.with(mActivity).statusBarColor(R.color.white).statusBarDarkFont(false).init();
        } else {
            ImmersionBar.with(mActivity).statusBarColor(R.color.white).statusBarDarkFont(true).init();
        }
        //实例化四个fragment
        homeFragment = new HomeFragment();
        postFragment = new PostFragment();
        chatFragment = new ChatFragment();
        mineFragment = new MineFragment();
        mFragments = new ArrayList<>();//初始化fragment容器
        mFragments.add(homeFragment);
        mFragments.add(postFragment);
        mFragments.add(chatFragment);
        mFragments.add(mineFragment);
        //viewpager绑定适配器
        viewPager.setAdapter(new FragmentsPagerAdapter(getSupportFragmentManager()));
        // navigationView点击事件
        bottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
            // menu文件夹中bottom_nav_menu.xml里加的android:orderInCategory属性就是下面item.getOrder()取的值
            if (menuItem.getItemId() == R.id.navigation_item1) {
                viewPager.setCurrentItem(0);
            }
            if (menuItem.getItemId() == R.id.navigation_item2) {
                viewPager.setCurrentItem(1);
            }
            if (menuItem.getItemId() == R.id.navigation_item3) {
                viewPager.setCurrentItem(2);
            }
            if (menuItem.getItemId() == R.id.navigation_item4) {
                viewPager.setCurrentItem(3);
            }
            return true;

        });
        viewPager.setOffscreenPageLimit(mFragments.size());


        /**
         * 监听ViewPager页面变化事件
         * */
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigation.getMenu().getItem(position).setChecked(true);//与底部导航绑定
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class FragmentsPagerAdapter extends FragmentPagerAdapter{
        private FragmentManager mFragmentManager;
        public FragmentsPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
        }
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
        @Override
        public int getCount() {
            return mFragments.size();
        }
    }


}
