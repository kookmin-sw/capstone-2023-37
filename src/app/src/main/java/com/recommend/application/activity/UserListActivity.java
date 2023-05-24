package com.recommend.application.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.recommend.application.R;
import com.recommend.application.adapter.UserListAdapter;
import com.recommend.application.base.BaseActivity;
import com.recommend.application.bean.bmob.ChatBean;
import com.recommend.application.bean.bmob.User;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class UserListActivity extends BaseActivity {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private UserListAdapter userListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_list);
    }

    @Override
    protected void initView() {


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        userListAdapter = new UserListAdapter();
        userListAdapter.bindToRecyclerView(recyclerView);
        getData();
    }

    private void getData() {
        //获取全部人员数据
        BmobQuery<User> query = new BmobQuery<>();
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    if (object != null) {
                        userListAdapter.setNewData(object);
                    }
                }
                refreshLayout.setRefreshing(false);
            }
        });


    }

    @Override
    protected int setLayout() {
        return R.layout.activity_user_list;
    }

    @OnClick({R.id.back_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onKeyDown(KeyEvent.KEYCODE_BACK,null);
                break;

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
