package com.recommend.application.activity;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gyf.immersionbar.ImmersionBar;
import com.recommend.application.R;
import com.recommend.application.adapter.FoodProcessAdapter;
import com.recommend.application.base.BaseActivity;
import com.recommend.application.bean.bmob.Food;
import com.recommend.application.utils.Constants;

import butterknife.BindView;
import butterknife.OnClick;

public class FoodDetilsActivity extends BaseActivity {
    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.image_view)
    ImageView imageView;
    @BindView(R.id.content_tv)
    TextView contentTv;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private Food food;

    private FoodProcessAdapter foodProcessAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        if (isDark) {
            ImmersionBar.with(mActivity).statusBarColor(R.color.white).statusBarDarkFont(false).init();
        } else {
            ImmersionBar.with(mActivity).statusBarColor(R.color.white).statusBarDarkFont(true).init();
        }
        food = (Food) getIntent().getSerializableExtra(Constants.DATA);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        foodProcessAdapter = new FoodProcessAdapter();
        foodProcessAdapter.bindToRecyclerView(recyclerView);


        if (food != null) {
            nameTv.setText(food.getName());
            if (!TextUtils.isEmpty(food.getPic())){
                Glide.with(mActivity).load(food.getPic()).into(imageView);
            }
            contentTv.setText(Html.fromHtml(food.getContent()));
            if (food.getFoodProcess() != null)
                foodProcessAdapter.setNewData(food.getFoodProcess());
        }
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_food_detils;
    }

    @OnClick({R.id.back_img, R.id.title_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.title_tv:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
