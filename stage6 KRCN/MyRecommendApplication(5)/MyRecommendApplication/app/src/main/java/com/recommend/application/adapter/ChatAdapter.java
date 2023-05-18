package com.recommend.application.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.recommend.application.R;
import com.recommend.application.bean.bmob.ChatBean;
import com.recommend.application.bean.bmob.User;
import com.recommend.application.utils.RoundImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends BaseQuickAdapter<ChatBean, BaseViewHolder> {
    private User myUser;

    public ChatAdapter(User myUser) {
        super(R.layout.item_chat_layout);
        this.myUser = myUser;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ChatBean item) {
        ConstraintLayout getLayout = helper.getView(R.id.get_layout);
        ConstraintLayout sendLayout = helper.getView(R.id.send_layout);
        TextView sendName = helper.getView(R.id.send_name_tv);
        TextView getName = helper.getView(R.id.name_tv);
        CircleImageView getImage = helper.getView(R.id.imageView);
        CircleImageView sendImage = helper.getView(R.id.send_imageView);
        TextView sendContent = helper.getView(R.id.send_content_tv);
        TextView getContent = helper.getView(R.id.content_tv);
        TextView timeTv = helper.getView(R.id.time_tv);

        if (myUser != null && item.getUser() != null){
            if (myUser.getObjectId().equals(item.getUser().getObjectId())){
                getLayout.setVisibility(View.GONE);
                sendLayout.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(item.getUser().getHeadPicture())){
                    Glide.with(mContext).load(item.getUser().getHeadPicture()).into(sendImage);
                }else {
                    Glide.with(mContext).load(R.mipmap.ic_launcher).into(sendImage);
                }
                sendContent.setText(item.getContent());
                sendName.setText(item.getUser().getNickName());
            }else {
                getLayout.setVisibility(View.VISIBLE);
                sendLayout.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(item.getUser().getHeadPicture())){
                    Glide.with(mContext).load(item.getUser().getHeadPicture()).into(getImage);
                }else {
                    Glide.with(mContext).load(R.mipmap.ic_launcher).into(getImage);
                }
                getContent.setText(item.getContent());
                getName.setText(item.getUser().getNickName());
            }
        }
        if (!TextUtils.isEmpty(item.getCreatedAt())){
            timeTv.setText(item.getCreatedAt());
        }else {
            timeTv.setText("");
        }
    }
}

