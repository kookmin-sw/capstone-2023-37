package com.recommend.application.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.recommend.application.R;
import com.recommend.application.bean.MusicData;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class MusicListAdapter extends BaseQuickAdapter<MusicData, BaseViewHolder> {

    public MusicListAdapter() {
        super(R.layout.item_musci_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MusicData item) {
        helper.setText(R.id.name_tv,item.getName());
        helper.setText(R.id.sort_tv,(helper.getAbsoluteAdapterPosition()+1)+"");
        ImageView imageView = helper.getView(R.id.image_view);
        if (!TextUtils.isEmpty(item.getPicurl())){
            Glide.with(mContext).load(item.getPicurl()).into(imageView);
        }

    }
}
