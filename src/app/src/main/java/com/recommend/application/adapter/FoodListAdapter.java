package com.recommend.application.adapter;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.recommend.application.R;
import com.recommend.application.bean.FoodProcess;
import com.recommend.application.bean.bmob.Food;
import com.recommend.application.utils.RoundImageView;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class FoodListAdapter extends BaseQuickAdapter<Food, BaseViewHolder> {

    public FoodListAdapter() {
        super(R.layout.item_food_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Food item) {
        helper.setText(R.id.name_tv,item.getName());
        RoundImageView imageView = helper.getView(R.id.image_view);
        if (!TextUtils.isEmpty(item.getPic())){
            Glide.with(mContext).load(item.getPic()).into(imageView);
        }

        /*item.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    if (item.getFoodProcess() != null){
                        for (int i = 0; i < item.getFoodProcess().size(); i++) {
                            FoodProcess foodProcess = item.getFoodProcess().get(i);
                            foodProcess.setPid(s);
                            foodProcess.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {

                                }
                            });
                        }
                    }
                }
            }
        });*/
    }
}
