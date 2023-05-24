package com.recommend.application.adapter;

import android.text.TextUtils;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.recommend.application.R;
import com.recommend.application.bean.bmob.Feedback;
import com.recommend.application.bean.bmob.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedbackListAdapter extends BaseQuickAdapter<Feedback, BaseViewHolder> {

    public FeedbackListAdapter() {
        super(R.layout.item_feedback);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Feedback item) {
        helper.setText(R.id.content_tv,item.getContent());
        helper.setText(R.id.time_tv,item.getCreatedAt());
        CircleImageView circleImageView = helper.getView(R.id.image_view);
        User user = item.getUser();
        if (user != null){
            helper.setText(R.id.name_tv,item.getUser().getNickName());
            if (!TextUtils.isEmpty(item.getUser().getHeadPicture())){
                Glide.with(mContext).load(item.getUser().getHeadPicture()).into(circleImageView);
            }
        }


    }
}
