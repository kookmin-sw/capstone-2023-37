package com.recommend.application.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.recommend.application.MyApplication;
import com.recommend.application.R;
import com.recommend.application.activity.ConstellationActivity;
import com.recommend.application.activity.FoodDetilsActivity;
import com.recommend.application.activity.NewsDetailsActivity;
import com.recommend.application.activity.WeaterActivity;
import com.recommend.application.adapter.NewsListAdapter;
import com.recommend.application.base.BaseFragment;
import com.recommend.application.bean.MusicData;
import com.recommend.application.bean.NewsData;
import com.recommend.application.bean.NewsRoot;
import com.recommend.application.bean.WeatherBean;
import com.recommend.application.bean.bmob.Food;
import com.recommend.application.utils.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class TodayFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    NewsListAdapter newsListAdapter;
    @BindView(R.id.image_view)
    ImageView imageView;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.food_image_view)
    ImageView foodImageView;
    @BindView(R.id.food_name_tv)
    TextView foodNameTv;
    @BindView(R.id.weather_name_tv)
    TextView weatherNameTv;
    @BindView(R.id.constellation_name_tv)
    TextView constellationNameTv;
    @BindView(R.id.post_image_view)
    ImageView postImageView;
    @BindView(R.id.post_name_tv)
    TextView postNameTv;
    private List<WeatherBean> stringList;
    private List<String> constellationList;
    private MusicData mMusicData;
    private Food mFood;
    private WeatherBean weatherBean;
    private int constellationPosition;

    public static TodayFragment newInstance() {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_today;
    }

    @Override
    protected void initEventAndData() {

        //设置列表的加载方向：纵向
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        newsListAdapter = new NewsListAdapter();
        newsListAdapter.bindToRecyclerView(recyclerView);
        newsListAdapter.setOnItemClickListener((adapter, view, position) -> {
            NewsData bean = newsListAdapter.getItem(position);
            if (bean == null) return;
            Intent intent = new Intent(mContext, NewsDetailsActivity.class);
            intent.putExtra(Constants.ID, bean.getDocid());
            intent.putExtra(Constants.TITLE, bean.getTitle());
            startActivity(intent);

        });
        //getNewsDatat();
        LiveEventBus.get("musicData", MusicData.class)
                .observe(this, musicData -> {
                    mMusicData = musicData;
                    if (musicData != null) {
                        nameTv.setText(musicData.getName());
                        if (!TextUtils.isEmpty(musicData.getPicurl())) {
                            Glide.with(mContext).load(musicData.getPicurl()).into(imageView);
                        }
                    }
                });
        LiveEventBus.get("foodData", Food.class)
                .observe(this, musicData -> {
                    mFood = musicData;
                    if (musicData != null) {
                        foodNameTv.setText(musicData.getName());
                        if (!TextUtils.isEmpty(musicData.getPic())) {
                            Glide.with(mContext).load(musicData.getPic()).into(foodImageView);
                        }
                    }
                });

        stringList = MyApplication.getInstance().getWeatherList();
        if (stringList != null) {
            weatherBean = stringList.get(new Random().nextInt(stringList.size()));
            if (weatherBean != null) {
                weatherNameTv.setText(weatherBean.getName());
            }
        }


        constellationList = MyApplication.getInstance().getConstellationList();
        if (constellationList != null) {
            constellationPosition = new Random().nextInt(constellationList.size());
            constellationNameTv.setText(constellationList.get(constellationPosition));
        }


    }

    private void getNewsDatat() {
        //1.拿到okHttpClient对象,可以设置连接超时等
        OkHttpClient okHttpClient = new OkHttpClient();

        //2.构造Request请求对象，可以增加头addHeader等
        Request.Builder builder = new Request.Builder();
        //url()中可以放入网址
        Request request = builder.
                get().
                url("https://v2.alapi.cn/api/new/toutiao?start=1&token=" + MyApplication.getInstance().getToken())
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
                Log.e(TAG, "onResponse: " + res);

                //InputStream is=response.body().byteStream();
                // 执行IO操作时，能够下载很大的文件，并且不会占用很大内存
                /**
                 * runOnUiThread方法切换到主线程中，或者用handler机制也可以
                 */

                NewsRoot newsRoot = new Gson().fromJson(res, NewsRoot.class);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 更新ui
                        if (newsRoot != null && newsRoot.getData() != null && newsListAdapter != null) {
                            newsListAdapter.setNewData(newsRoot.getData());
                        }
                    }
                });

            }
        });
    }

    @OnClick({R.id.image_view, R.id.name_tv, R.id.food_image_view, R.id.food_name_tv,
            R.id.weather_name_tv, R.id.constellation_name_tv, R.id.post_image_view, R.id.post_name_tv})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.image_view:
            case R.id.name_tv:
                if (mMusicData != null){
                    Uri uri = Uri.parse(mMusicData.getUrl());
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                break;
            case R.id.food_image_view:
            case R.id.food_name_tv:
                if (mFood == null)return;
                intent = new Intent(mContext, FoodDetilsActivity.class);
                intent.putExtra(Constants.DATA,mFood);
                startActivity(intent);
                break;
            case R.id.weather_name_tv:
                if (weatherBean == null)return;
                intent = new Intent(mContext, WeaterActivity.class);
                intent.putExtra(Constants.DATA,weatherBean);
                startActivity(intent);
                break;
            case R.id.constellation_name_tv:
                intent = new Intent(mContext, ConstellationActivity.class);
                intent.putExtra(Constants.POSITION,constellationPosition);
                startActivity(intent);
                break;
            case R.id.post_image_view:
            case R.id.post_name_tv:
                break;
        }
    }
}
