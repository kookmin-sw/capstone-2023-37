package com.recommend.application.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.immersionbar.ImmersionBar;
import com.recommend.application.R;
import com.recommend.application.base.BaseActivity;
import com.recommend.application.bean.bmob.Post;
import com.recommend.application.bean.bmob.User;
import com.recommend.application.utils.Constants;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailActivity extends BaseActivity {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.user_img)
    CircleImageView userImg;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.content_tv)
    TextView contentTv;
    @BindView(R.id.time_tv)
    TextView timeTv;

    private String id;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_post_detail);
    }

    @Override
    protected void initView() {
        if (isDark) {
            ImmersionBar.with(mActivity).statusBarColor(R.color.white).statusBarDarkFont(false).init();
        } else {
            ImmersionBar.with(mActivity).statusBarColor(R.color.white).statusBarDarkFont(true).init();
        }
        id = getIntent().getStringExtra(Constants.ID);
        if (!TextUtils.isEmpty(id)){
            BmobQuery<Post> query = new BmobQuery<>();
            query.include("author");
            query.getObject(id, new QueryListener<Post>() {
                @Override
                public void done(Post object, BmobException e) {
                    if (e == null) {
                        post = object;
                        setData();
                        //Snackbar.make(mBtnEqual, "查询成功：" + object.size(), Snackbar.LENGTH_LONG).show();
                    } else {
                        Log.e("BMOB", e.toString());
                        //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }
            });

        }

    }

    private void setData() {
        if (post != null){
            User author = post.getAuthor();
            if (author != null){
                userName.setText(author.getNickName());
                if (!TextUtils.isEmpty(author.getHeadPicture())){
                    Glide.with(mActivity).load(author.getHeadPicture()).into(userImg);
                }
            }
            contentTv.setText(post.getContent());
            timeTv.setText(post.getCreatedAt());

            String str = post.getImage();
            if (!TextUtils.isEmpty(str)){
                String[] strArray = str.split(",");
                List<String> imgList = new ArrayList<>(Arrays.asList(strArray));
                banner.setImageLoader(new MyLoader());//加载图片  图片加载器
                banner.setImages(imgList); //添加图片集合
                banner.isAutoPlay(false); //是否自动轮播,true则为自动轮播，默认为true
                banner.setDelayTime(24*60*60*1000);      //设置每张图片显示的时间、单位秒
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                banner.start();
               /* //设置轮播图样式 共有6种 可以点进去更换查看不同样式
                banner
                        //加载图片  图片加载器
                        .setImageLoader(new MyLoader())
                        //添加图片集合
                        .setImages(imgList)
                        //设置图片轮播的不同显示效果  可以点进去跟换样式
                        .setBannerAnimation(Transformer.Default)
                        //设置每张图片显示的时间
                        .setDelayTime(24*60*60*1000)
                        //是否自动轮播,true则为自动轮播，默认为true
                        .isAutoPlay(false)
                        //设置指示器的位置  左中右三种
                        .setIndicatorGravity(BannerConfig.CENTER)
                        //轮播图的监听
                        .setOnBannerListener(new OnBannerListener() {
                            @Override
                            public void OnBannerClick(int position) {

                            }
                        })
                        //启动轮播图 不要忘记
                        .start();*/

            }

        }
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_post_detail;
    }

    @OnClick({R.id.back_img, R.id.user_img, R.id.user_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.user_img:
                break;
            case R.id.user_name:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }
}
