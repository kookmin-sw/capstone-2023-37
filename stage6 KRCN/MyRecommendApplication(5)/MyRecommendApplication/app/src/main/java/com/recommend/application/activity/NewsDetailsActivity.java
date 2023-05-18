package com.recommend.application.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.recommend.application.MyApplication;
import com.recommend.application.R;
import com.recommend.application.base.BaseActivity;
import com.recommend.application.bean.NewDetailsData;
import com.recommend.application.bean.NewDetailsRoot;
import com.recommend.application.utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsDetailsActivity extends BaseActivity {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.content_tv)
    TextView contentTv;
    @BindView(R.id.news_title_tv)
    TextView newsTitleTv;
    @BindView(R.id.info_tv)
    TextView infoTv;

    private String docId;//新闻ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_news_details);
    }

    @Override
    protected void initView() {
        if (isDark) {
            ImmersionBar.with(mActivity).statusBarColor(R.color.white).statusBarDarkFont(false).init();
        } else {
            ImmersionBar.with(mActivity).statusBarColor(R.color.white).statusBarDarkFont(true).init();
        }

        docId = getIntent().getStringExtra(Constants.ID);//接收上个页面传递过来的数据
        newsTitleTv.setText(getIntent().getStringExtra(Constants.TITLE));

        //1.拿到okHttpClient对象,可以设置连接超时等
        OkHttpClient okHttpClient = new OkHttpClient();

        //2.构造Request请求对象，可以增加头addHeader等
        Request.Builder builder = new Request.Builder();
        //url()中可以放入网址
        Request request = builder.
                get().
                url("https://v2.alapi.cn/api/new/detail?docid=" + docId + "&token="+ MyApplication.getInstance().getToken())
                .build();
        //3.将Request封装为Call
        Call call = okHttpClient.newCall(request);
        //4.执行call
        //方法一Response response=call.execute();//汇抛出IO异常，同步方法
        //方法二,异步方法，放到队列中,处于子线程中，无法更新UI
        call.enqueue(new Callback() {
            //请求时失败时调用
            @Override
            public void onFailure(Call call, IOException e) {

            }

            //请求成功时调用
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //处于子线程中，能够进行大文件下载，但是无法更新UI
                final String res = response.body().string();//请求成功时返回的东西
                //InputStream is=response.body().byteStream();
                // 执行IO操作时，能够下载很大的文件，并且不会占用很大内存
                /**
                 * runOnUiThread方法切换到主线程中，或者用handler机制也可以
                 */
                Log.e(TAG, "onResponse: "+res);
                NewDetailsRoot newDetailsRoot = new Gson().fromJson(res, NewDetailsRoot.class);
                if (newDetailsRoot != null && newDetailsRoot.getData() != null) {
                    String str = newDetailsRoot.getData().getBody();
                    String info = "来自："+newDetailsRoot.getData().getSource()+"   "+newDetailsRoot.getData().getPtime()+"\n"+newDetailsRoot.getData().getCategory();
                    if (newDetailsRoot.getData().getImg() != null && newDetailsRoot.getData().getImg().size() > 0) {
                        for (NewDetailsData.Img img : newDetailsRoot.getData().getImg()) {
                            if (str.contains(img.getRef())) {
                                String[] ss = img.getPixel().split("\\*");
                                str = str.replaceAll(img.getRef(), "<img height=\\\"" + ss[0] + "px\\\" width=\\\"100%\\\" src=\\\"" + img.getSrc() + "\\\"/>");
                            }
                        }
                        Log.e(TAG, "run: " + str);
                    }
                    final Spanned sp = Html.fromHtml(str, source -> {
                        InputStream is;
                        try {
                            is = (InputStream) new URL(source).getContent();
                            Drawable d = Drawable.createFromStream(is, "src");
                            d.setBounds(0, 0, d.getIntrinsicWidth(),
                                    d.getIntrinsicHeight());
                            is.close();
                            return d;
                        } catch (Exception e) {
                            return null;
                        }
                    }, null);
                    mActivity.runOnUiThread(() -> {
                        // 更新ui
                        contentTv.setText(sp);
                        infoTv.setText(info);

                    });
                }
            }
        });


    }

    @Override
    protected int setLayout() {
        return R.layout.activity_news_details;
    }

    @OnClick({R.id.back_img, R.id.title_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();//返回键 直接退出页面
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
