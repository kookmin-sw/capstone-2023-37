package com.recommend.application.activity;

import android.os.Bundle;
import android.webkit.WebView;

import com.recommend.application.R;
import com.recommend.application.base.BaseActivity;
import com.recommend.application.utils.Constants;

import butterknife.BindView;

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.web_view)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        webView.loadUrl(getIntent().getStringExtra(Constants.URL));
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_web_view;
    }
}
