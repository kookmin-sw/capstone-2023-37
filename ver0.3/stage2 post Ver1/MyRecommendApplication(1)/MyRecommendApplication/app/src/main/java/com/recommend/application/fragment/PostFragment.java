package com.recommend.application.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.recommend.application.R;
import com.recommend.application.activity.PostDetailActivity;
import com.recommend.application.activity.RelastePostActivity;
import com.recommend.application.adapter.PostAdapter;
import com.recommend.application.base.BaseFragment;
import com.recommend.application.bean.bmob.Post;
import com.recommend.application.utils.Constants;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

import static android.app.Activity.RESULT_OK;


public class PostFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.floating_btn)
    FloatingActionButton floatingBtn;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private PostAdapter postAdapter;
    private StaggeredGridLayoutManager layoutManager;



  /*  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }*/

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_post;
    }

    @Override
    protected void initEventAndData() {
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //防止item 交换位置
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        recyclerView.setLayoutManager(layoutManager);
        postAdapter = new PostAdapter();
        postAdapter.bindToRecyclerView(recyclerView);
        postAdapter.setOnItemClickListener((adapter, view, position) -> {
            Post post = postAdapter.getItem(position);
            if (post == null) return;
            startActivity(new Intent(mContext, PostDetailActivity.class)
                    .putExtra(Constants.ID, post.getObjectId()));
        });
        getData();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新获取数据
                getData();
            }
        });

        //防止拖动过程中顶部出现空白
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //防止第一行到顶部有空白区域
                layoutManager.invalidateSpanAssignments();
            }
        });

    }

    private void getData() {
        BmobQuery<Post> query = new BmobQuery<>();
        //希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
        query.include("author");
        query.order("-updatedAt");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    if (list != null) {
                        if (list.size() > 0)
                            LiveEventBus.get("postData").post(list.get(new Random().nextInt(list.size())));
                        postAdapter.setNewData(list);
                    }
                }
                refreshLayout.setRefreshing(false);
            }
        });

    }


    @OnClick({R.id.floating_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.floating_btn:
                //浮动按钮点击跳转至发布帖子页面
                startActivityForResult(new Intent(mContext, RelastePostActivity.class)
                        , 110);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //新建发帖子成功后 回调事件  并将新数据加在列表第一位
        if (requestCode == 110 && resultCode == RESULT_OK) {
            if (data != null) {
                String str = data.getStringExtra(Constants.ID);
                if (TextUtils.isEmpty(str))
                    return;
                BmobQuery<Post> categoryBmobQuery = new BmobQuery<>();
                categoryBmobQuery.getObject(str, new QueryListener<Post>() {
                    @Override
                    public void done(Post object, BmobException e) {
                        if (e == null) {
                            if (object != null) {
                                postAdapter.addData(0, object);
                            }
                            //Snackbar.make(mBtnEqual, "查询成功：" + object.size(), Snackbar.LENGTH_LONG).show();
                        } else {
                            Log.e("BMOB", e.toString());
                            //Snackbar.make(mBtnEqual, e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

            }
        }
    }
}
