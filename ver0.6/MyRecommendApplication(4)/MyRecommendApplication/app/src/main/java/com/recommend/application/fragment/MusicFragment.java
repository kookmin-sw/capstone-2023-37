package com.recommend.application.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.recommend.application.R;
import com.recommend.application.activity.WebViewActivity;
import com.recommend.application.adapter.MusicListAdapter;
import com.recommend.application.base.BaseFragment;
import com.recommend.application.bean.MusicData;
import com.recommend.application.bean.MusicRoot;
import com.recommend.application.bean.NewDetailsData;
import com.recommend.application.bean.NewDetailsRoot;
import com.recommend.application.bean.bmob.Food;
import com.recommend.application.utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MusicFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private MusicListAdapter musicListAdapter;
    private int pageSize = 1;
    private Set<String> stringSet = new HashSet<>();

    public static MusicFragment newInstance() {
        MusicFragment fragment = new MusicFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_music;
    }

    @Override
    protected void initEventAndData() {


        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        musicListAdapter = new MusicListAdapter();
        musicListAdapter.bindToRecyclerView(recyclerView);
        musicListAdapter.setOnItemClickListener((adapter, view, position) -> {
            MusicData bean = musicListAdapter.getItem(position);
            if (bean == null)return;
            Uri uri = Uri.parse(bean.getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        //getMusicData();
        getData();

    }

    private void getData() {
        BmobQuery<MusicData> query = new BmobQuery<>();
        query.order("-createdAt")//查找所有数据，并根据事件降序排列
                .findObjects(new FindListener<MusicData>() {
                    @Override
                    public void done(List<MusicData> object, BmobException e) {
                        if (e == null) {
                            if (object != null){
                                Collections.shuffle(object);//随机打乱顺序
                                musicListAdapter.setNewData(object.subList(0,20));
                                LiveEventBus.get("musicData").post(object.get(new Random().nextInt(object.size())));
                            }
                        } else {

                        }
                    }
                });

    }

    private void getMusicData() {
        //1.拿到okHttpClient对象,可以设置连接超时等
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.构造Request请求对象，可以增加头addHeader等
        Request.Builder builder = new Request.Builder();
        //url()中可以放入网址
        //使用公用免费API查询 网易云音乐热歌榜
        Request request = builder.
                get().
                url("https://api.uomg.com/api/rand.music?sort=热歌榜&format=json")
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
                MusicRoot musicRoot = new Gson().fromJson(res, MusicRoot.class);

                if (musicRoot != null && musicRoot.getData() != null) {
                    mActivity.runOnUiThread(() -> {
                        if (stringSet.add(musicRoot.getData().getName())){
                            musicListAdapter.addData(musicRoot.getData());
                            pageSize++;
                        }
                        if (pageSize <= 20){ //只查询前20首
                            getMusicData();
                        }
                    });
                }
            }
        });
    }


}
