package com.recommend.application.adapter;

import android.text.Html;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.recommend.application.R;
import com.recommend.application.bean.FoodProcess;




public class FoodProcessAdapter extends BaseQuickAdapter<FoodProcess, BaseViewHolder> {

    public FoodProcessAdapter() {
        super(R.layout.item_food_process_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FoodProcess item) {
        helper.setText(R.id.sort_tv, Html.fromHtml((helper.getAbsoluteAdapterPosition()+1)+"——>"+item.getPcontent()));
        ImageView imageView = helper.getView(R.id.image_view);
        if (!TextUtils.isEmpty(item.getPic())){
            Glide.with(mContext).load(item.getPic()).into(imageView);
        }
    }
}
