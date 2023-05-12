package com.recommend.application.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.recommend.application.R;
import com.recommend.application.activity.PostDetailActivity;
import com.recommend.application.adapter.PostAdapter;
import com.recommend.application.base.BaseFragment;
import com.recommend.application.bean.bmob.Post;
import com.recommend.application.bean.bmob.User;
import com.recommend.application.utils.Constants;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class MyPostFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private PostAdapter postAdapter;
    private User loginUser;
    public static MyPostFragment newInstance(User loginUser) {
        MyPostFragment fragment = new MyPostFragment();
        Bundle args = new Bundle();
        fragment.loginUser = loginUser;
        fragment.setArguments(args);
        return fragment;
    }


   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_post, container, false);
    }*/

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_post;
    }

    @Override
    protected void initEventAndData() {
        layoutManager = new GridLayoutManager(mActivity,2);

        recyclerView.setLayoutManager(layoutManager);
        postAdapter = new PostAdapter(1);
        postAdapter.bindToRecyclerView(recyclerView);
        postAdapter.setOnItemClickListener((adapter, view, position) -> {
            Post post = postAdapter.getItem(position);
            if (post == null) return;
            startActivity(new Intent(mContext, PostDetailActivity.class)
                    .putExtra(Constants.ID, post.getObjectId()));
        });
        getData();





    }

    private void getData() {
        BmobQuery<Post> query = new BmobQuery<>();
        //希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
        query.addWhereEqualTo("author",loginUser);
        query.include("author");
        query.order("-updatedAt");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    if (list != null) {
                        postAdapter.setNewData(list);
                    }
                }

            }
        });

    }

}
