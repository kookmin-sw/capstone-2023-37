package com.recommend.application.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.recommend.application.R;

import java.util.List;

public class ConstellationAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ConstellationAdapter() {
        super(R.layout.item_constellation_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        helper.setText(R.id.name_tv,item);
    }
}
