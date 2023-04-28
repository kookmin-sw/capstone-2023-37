package com.recommend.application.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.recommend.application.MyApplication;
import com.recommend.application.utils.ToastUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * BaseActicity,Activity的基类
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG;
    protected Activity mActivity;
    protected Context mContext;
    private Unbinder unbinder;
    protected boolean isDark;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayout());
        unbinder = ButterKnife.bind(this);
        TAG = this.getClass().getSimpleName();
        mActivity = this;
        mContext = this;
        MyApplication.getInstance().addActivity(this);
        isDark = isDarkTheme();
        initView();

    }

    //获取深色主题还是默认
    protected boolean isDarkTheme() {
        return (mContext.getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_YES) != 0;
    }


    protected abstract void initView();


    protected void showToast(String msg){
        if (!TextUtils.isEmpty(msg))
            ToastUtil.shortShow(msg);
    }


    protected abstract int setLayout();


    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();

    }
}
