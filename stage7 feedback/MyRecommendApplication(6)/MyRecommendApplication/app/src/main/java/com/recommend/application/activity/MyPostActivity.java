package com.recommend.application.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.recommend.application.MyApplication;
import com.recommend.application.R;
import com.recommend.application.adapter.PostAdapter;
import com.recommend.application.base.BaseActivity;
import com.recommend.application.bean.bmob.Post;
import com.recommend.application.bean.bmob.User;
import com.recommend.application.fragment.MyPostFragment;
import com.recommend.application.utils.Constants;
import com.recommend.application.utils.ViewPagerForScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyPostActivity extends BaseActivity {

    @BindView(R.id.circle_img)
    CircleImageView circleImg;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.introduction_tv)
    TextView introductionTv;
    @BindView(R.id.top_layout)
    LinearLayout topLayout;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPagerForScrollView viewPager;
    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private User user;

    private List<Fragment> mFragments;//fragment容器
    private ArrayList<String> mTitle;//Tablayout标题容器
    private MyPostFragment myPostFragment;

    private GridLayoutManager layoutManager;
    private PostAdapter postAdapter;

    @Override
    protected int setLayout() {
        return R.layout.activity_my_post;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my_post);
    }

    @Override
    protected void initView() {
        if (isDark) {
            ImmersionBar.with(mActivity).statusBarColor(R.color.white).statusBarDarkFont(false).init();
        } else {
            ImmersionBar.with(mActivity).statusBarColor(R.color.white).statusBarDarkFont(true).init();
        }
        user = MyApplication.getInstance().getUser();
        if (user != null) {
            MyApplication.getInstance().setLoginUser(user);
            if (!TextUtils.isEmpty(user.getHeadPicture())) {
                Glide.with(mActivity).load(user.getHeadPicture()).into(circleImg);
            }
            nameTv.setText(user.getNickName());
            Drawable drawable;
            if (user.getGender() == 0) {
                drawable = mActivity.getResources().getDrawable(R.drawable.man);
            } else {
                drawable = mActivity.getResources().getDrawable(R.drawable.woman);
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            nameTv.setCompoundDrawables(drawable, null, null, null);
            introductionTv.setText(user.getIntroduction());
        }


        mFragments = new ArrayList<>();//初始化fragment容器

        myPostFragment = MyPostFragment.newInstance(user);


        //将子fragment添加至容器
        mFragments.add(myPostFragment);


        mTitle = new ArrayList<>();
        //将标题添加至容器
        mTitle.add("My Post");


        /**
         * 预加载
         */
        //viewPager.setOffscreenPageLimit(mFragments.size());
        /**
         * 设置适配器
         */
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });
        tabLayout.setupWithViewPager(viewPager);//将Tablayout与viewpager进行关联
        layoutManager = new GridLayoutManager(mActivity, 2);

        recyclerView.setLayoutManager(layoutManager);
        postAdapter = new PostAdapter(1);
        postAdapter.bindToRecyclerView(recyclerView);
        postAdapter.setOnItemClickListener((adapter, view, position) -> {
            Post post = postAdapter.getItem(position);
            if (post == null) return;
            startActivity(new Intent(mContext, PostDetailActivity.class)
                    .putExtra(Constants.TYPE, 1)
                    .putExtra(Constants.ID, post.getObjectId()));
        });
        postAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                Post post = postAdapter.getItem(position);
                if (post != null) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Tip")
                            .setMessage("Are you sure you want to delete this?")
                            .setNeutralButton("sure", (dialog, which) -> {

                                post.delete(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            postAdapter.remove(position);//删除数据源,移除集合中当前下标的数据
                                            postAdapter.notifyItemRemoved(position);//刷新被删除的地方
                                            postAdapter.notifyItemRangeChanged(position, postAdapter.getItemCount()); //刷新被删除数据，以及其后面的数据
                                        }
                                    }
                                });

                            })
                            .setPositiveButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }

                return true;
            }
        });
        getData();

    }


    private void getData() {
        BmobQuery<Post> query = new BmobQuery<>();
        //希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
        query.addWhereEqualTo("author", user);
        query.include("author");
        query.order("-createdAt");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    if (list != null) {
                        if (postAdapter != null)
                            postAdapter.setNewData(list);
                    }
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);

    }

    @OnClick(R.id.back_img)
    public void onViewClicked() {
        finish();
    }
}
