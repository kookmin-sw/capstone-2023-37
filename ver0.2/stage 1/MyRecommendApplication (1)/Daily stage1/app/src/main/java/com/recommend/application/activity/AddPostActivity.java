package com.recommend.application.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.recommend.application.R;
import com.recommend.application.base.BaseActivity;

public class AddPostActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_add_post);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int setLayout() {
        return R.layout.activity_add_post;
    }
}
