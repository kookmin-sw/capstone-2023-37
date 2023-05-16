package com.recommend.application.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.recommend.application.MyApplication;
import com.recommend.application.R;
import com.recommend.application.activity.FoodDetilsActivity;
import com.recommend.application.adapter.FoodListAdapter;
import com.recommend.application.base.BaseFragment;
import com.recommend.application.bean.FoodRoot;
import com.recommend.application.bean.MusicRoot;
import com.recommend.application.bean.bmob.Food;
import com.recommend.application.utils.Constants;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class FoodFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private String[] keywords = {"蛋","鱼","豆腐","牛肉"};
    private int num = 10;
    private FoodListAdapter foodListAdapter;

    public static FoodFragment newInstance() {
        FoodFragment fragment = new FoodFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_food;
    }

    @Override
    protected void initEventAndData() {

        //使用网格布局，一行2列
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        foodListAdapter = new FoodListAdapter();
        foodListAdapter.bindToRecyclerView(recyclerView);
        foodListAdapter.setOnItemClickListener((adapter, view, position) -> {
            Food food = foodListAdapter.getItem(position);
            if (food == null)return;
            Intent intent = new Intent(mContext, FoodDetilsActivity.class);
            intent.putExtra(Constants.DATA,food);
            startActivity(intent);

        });
        getBmobData();


        getData();

    }

    private void getBmobData() {
        BmobQuery<Food> query = new BmobQuery<>();
        query.order("-createdAt")//查找所有数据，并根据事件降序排列
                .findObjects(new FindListener<Food>() {
                    @Override
                    public void done(List<Food> object, BmobException e) {
                        if (e == null) {
                            if (object != null){
                                Collections.shuffle(object);//随机打乱顺序
                                foodListAdapter.setNewData(object);
                                LiveEventBus.get("foodData").post(object.get(new Random().nextInt(object.size())));
                            }
                        } else {

                        }
                    }
                });
    }

    private void getData() {
        //1.拿到okHttpClient对象,可以设置连接超时等
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.构造Request请求对象，可以增加头addHeader等
        Request.Builder builder = new Request.Builder();
        String url = "https://way.jd.com/jisuapi/search?keyword="+keywords[1]
                /*+ keywords[new Random().nextInt(keywords.length)]*/ +
                "&num=" + num + "&appkey=" + MyApplication.getInstance().getApiKey();
        Log.e(TAG, "url: "+url);
        //url()中可以放入网址
        Request request = builder.
                get().url(url)
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
                Log.e(TAG, "onResponse: " + res);
                setData(res);
            }
        });
    }

    private void setData(String res) {
        FoodRoot foodRoot = new Gson().fromJson(res, FoodRoot.class);
        if (foodRoot != null && foodRoot.getResult() != null && foodRoot.getResult().getResult() != null
                && foodRoot.getResult().getResult().getList() != null) {
            mActivity.runOnUiThread(() -> {

                foodListAdapter.addData(foodRoot.getResult().getResult().getList());
            });
        }
    }


}
