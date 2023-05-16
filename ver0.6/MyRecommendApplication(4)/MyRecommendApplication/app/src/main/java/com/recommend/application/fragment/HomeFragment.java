package com.recommend.application.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.recommend.application.R;
import com.recommend.application.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class HomeFragment extends BaseFragment {
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private List<Fragment> mFragments;//fragment容器
    private ArrayList<String> mTitle;//Tablayout标题容器
    private MusicFragment musicFragment;
    private FoodFragment foodFragment;
    private WeatherFragment weatherFragment;
    private ConstellationFragment constellationFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initEventAndData() {
        mFragments = new ArrayList<>();//初始化fragment容器

        musicFragment = MusicFragment.newInstance();
        foodFragment = FoodFragment.newInstance();
        weatherFragment = WeatherFragment.newInstance();
        constellationFragment = ConstellationFragment.newInstance();


        //将子fragment添加至容器
        mFragments.add(TodayFragment.newInstance());
        mFragments.add(musicFragment);
        mFragments.add(foodFragment);
        mFragments.add(weatherFragment);
        mFragments.add(constellationFragment);


        mTitle = new ArrayList<>();
        //将标题添加至容器
        mTitle.add("Today");
        mTitle.add("Music");
        mTitle.add("Food");
        mTitle.add("Weather");
        mTitle.add("Constellation");

        /**
         * 预加载
         */
        //viewPager.setOffscreenPageLimit(mFragments.size());
        /**
         * 设置适配器
         */
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }
            @Override
            public int getCount() {
                return mFragments.size();
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });
        tabLayout.setupWithViewPager(viewPager);//将Tablayout与viewpager进行关联

    }




}
