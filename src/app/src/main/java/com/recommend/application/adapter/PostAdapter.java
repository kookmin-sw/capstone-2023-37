package com.recommend.application.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.recommend.application.R;
import com.recommend.application.bean.bmob.Post;
import com.recommend.application.utils.RoundImageView;

import java.lang.reflect.Field;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends BaseQuickAdapter<Post, BaseViewHolder> {

    private int type = 0;
    public PostAdapter(int type) {
        super(R.layout.item_post_layout);

        this.type = type;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Post item) {
        int position = helper.getAbsoluteAdapterPosition();
        CircleImageView circleImageView = helper.getView(R.id.head_image_view);
        if (item.getAuthor() != null) {
            helper.setText(R.id.name_tv, item.getAuthor().getNickName());
            if (!TextUtils.isEmpty(item.getAuthor().getHeadPicture())) {
                Glide.with(mContext).load(item.getAuthor().getHeadPicture()).into(circleImageView);
            }
        }
        helper.setText(R.id.content_tv, item.getContent());
        RoundImageView imageView = helper.getView(R.id.image_view);
        RoundImageView imageViewTwo = helper.getView(R.id.image_view_two);
        if (0 == type){
            if (position % 2 == 0 ){
                imageView.setVisibility(View.VISIBLE);
                imageViewTwo.setVisibility(View.GONE);
            }else {
                imageView.setVisibility(View.GONE);
                imageViewTwo.setVisibility(View.VISIBLE);
            }
        }

        String img = item.getImage();
        if (!TextUtils.isEmpty(img)) {
            String[] stringsArray = img.split(",");
            Glide.with(mContext).load(stringsArray[0]).into(imageView);
            Glide.with(mContext).load(stringsArray[0]).into(imageViewTwo);
        }


    }
}
