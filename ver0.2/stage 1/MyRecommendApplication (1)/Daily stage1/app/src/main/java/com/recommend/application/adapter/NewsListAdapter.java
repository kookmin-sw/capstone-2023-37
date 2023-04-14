package com.recommend.application.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.recommend.application.R;
import com.recommend.application.bean.NewsData;



public class NewsListAdapter extends BaseQuickAdapter<NewsData, BaseViewHolder> {

    public NewsListAdapter() {
        super(R.layout.item_news_list);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NewsData item) {
        helper.setText(R.id.title_tv,item.getTitle());
        helper.setText(R.id.time_tv,item.getTime());
        helper.setText(R.id.soure_tv,item.getSource());
        ImageView imageView = helper.getView(R.id.image_view);
        if (!TextUtils.isEmpty(item.getImgsrc())){
            Glide.with(mContext).load(item.getImgsrc()).into(imageView);
        }

    }
}
