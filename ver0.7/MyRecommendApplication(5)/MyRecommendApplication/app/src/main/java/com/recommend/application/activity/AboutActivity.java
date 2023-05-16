package com.recommend.application.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.recommend.application.R;
import com.recommend.application.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.back_img)
    ImageView backImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_about);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int setLayout() {
        return R.layout.activity_about;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.back_img)
    public void onViewClicked() {
        finish();
    }
}
