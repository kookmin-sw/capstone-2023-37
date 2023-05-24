package com.recommend.application.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.recommend.application.MyApplication;
import com.recommend.application.R;
import com.recommend.application.activity.ConstellationActivity;
import com.recommend.application.activity.WeaterActivity;
import com.recommend.application.adapter.ConstellationAdapter;
import com.recommend.application.adapter.WeatherAdapter;
import com.recommend.application.base.BaseFragment;
import com.recommend.application.bean.WeatherBean;
import com.recommend.application.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class WeatherFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    List<WeatherBean> stringList = new ArrayList<>();
    WeatherAdapter weatherAdapter;



    public static WeatherFragment newInstance() {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_weather;
    }

    @Override
    protected void initEventAndData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        stringList = MyApplication.getInstance().getWeatherList();




        weatherAdapter = new WeatherAdapter();
        weatherAdapter.bindToRecyclerView(recyclerView);
        weatherAdapter.setNewData(stringList);
        weatherAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(mContext, WeaterActivity.class);
            intent.putExtra(Constants.DATA,stringList.get(position));
            startActivity(intent);
        });
    }

}
