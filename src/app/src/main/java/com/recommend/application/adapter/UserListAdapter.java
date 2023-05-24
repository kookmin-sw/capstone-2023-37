package com.recommend.application.adapter;

import android.text.TextUtils;

import androidx.annotation.NonNull;


import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.recommend.application.R;
import com.recommend.application.bean.bmob.User;



import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends BaseQuickAdapter<User, BaseViewHolder> {

    public UserListAdapter() {
        super(R.layout.item_user_list_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, User item) {
        CircleImageView circleImageView = helper.getView(R.id.image_view);

        if (!TextUtils.isEmpty(item.getHeadPicture())){
            Glide.with(mContext).load(item.getHeadPicture()).into(circleImageView);
        }
        helper.setText(R.id.name_tv,item.getNickName());
    }
}
