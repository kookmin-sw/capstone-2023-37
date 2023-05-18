package com.recommend.application.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.recommend.application.R;
import com.recommend.application.adapter.FeedbackListAdapter;
import com.recommend.application.base.BaseActivity;
import com.recommend.application.bean.bmob.Feedback;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FeedbackListActivity extends BaseActivity {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.add_btn)
    Button addBtn;

    private FeedbackListAdapter feedbackListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_feedback_list);
    }

    @Override
    protected void initView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        feedbackListAdapter = new FeedbackListAdapter();
        feedbackListAdapter.bindToRecyclerView(recyclerView);
        getData();


    }

    @Override
    protected int setLayout() {
        return R.layout.activity_feedback_list;
    }

    @OnClick({R.id.back_img,R.id.add_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.add_btn:
                startActivityForResult(new Intent(mContext,AddFeedbackActivity.class),1001);
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK){
            getData();

        }
    }

    private void getData() {
        BmobQuery<Feedback> query = new BmobQuery<>();
        query.include("user");
        query.order("-createdAt")
                .findObjects(new FindListener<Feedback>() {
                    @Override
                    public void done(List<Feedback> object, BmobException e) {
                        if (e == null) {
                            feedbackListAdapter.setNewData(object);
                        } else {

                        }
                    }
                });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


}
