package com.recommend.application.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.recommend.application.R;
import com.recommend.application.bean.bmob.ChatRoom;
import com.recommend.application.bean.bmob.User;


import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoomAdapter extends BaseQuickAdapter<ChatRoom, BaseViewHolder> {


    private final User loginUser;

    public ChatRoomAdapter(User loginUser) {
        super(R.layout.item_chat_room_layout);
        this.loginUser = loginUser;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ChatRoom item) {
        CircleImageView circleImageView = helper.getView(R.id.image_view);
        TextView joinTv = helper.getView(R.id.btn_tv);
        helper.setText(R.id.name_tv,item.getName()+"(1)");
        helper.setText(R.id.remake_tv,item.getRemake());
        if (item.getCreateUser() != null){
            if (item.getObjectId().equals(loginUser.getObjectId())){
                joinTv.setText("enter");
            }
            if (!TextUtils.isEmpty(item.getCreateUser().getHeadPicture())){
                Glide.with(mContext).load(item.getCreateUser().getHeadPicture()).into(circleImageView);
            }
        }
        if (item.getUsers() != null && item.getUsers().getObjects() != null){
            helper.setText(R.id.name_tv,item.getName()+"("+item.getUsers().getObjects().size()+")");
        }


    }
}
