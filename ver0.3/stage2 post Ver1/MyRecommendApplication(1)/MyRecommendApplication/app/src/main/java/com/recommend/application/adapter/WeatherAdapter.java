package com.recommend.application.adapter;

import androidx.annotation.NonNull;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.recommend.application.R;
import com.recommend.application.bean.WeatherBean;



public class WeatherAdapter extends BaseQuickAdapter<WeatherBean, BaseViewHolder> {


    public WeatherAdapter() {
        super(R.layout.item_weather_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, WeatherBean item) {
        helper.setText(R.id.name_tv,item.getName());
    }
}
