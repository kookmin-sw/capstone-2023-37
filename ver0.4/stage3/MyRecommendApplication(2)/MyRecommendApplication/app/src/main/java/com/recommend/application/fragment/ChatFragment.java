package com.recommend.application.fragment;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.recommend.application.MyApplication;
import com.recommend.application.R;
import com.recommend.application.activity.ChatActivity;
import com.recommend.application.activity.CreateChatRoomActivity;
import com.recommend.application.adapter.ChatRoomAdapter;
import com.recommend.application.base.BaseFragment;
import com.recommend.application.bean.bmob.ChatRoom;
import com.recommend.application.bean.bmob.User;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class ChatFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.create_btn)
    Button createBtn;
    @BindView(R.id.no_data_layout)
    LinearLayout noDataLayout;
    @BindView(R.id.create_tv)
    TextView createTv;
    @BindView(R.id.btn_tv)
    TextView btnTv;
    private ChatRoomAdapter chatRoomAdapter;
    private User loginUser;


 /*   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }*/

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void initEventAndData() {
        loginUser = MyApplication.getInstance().getUser();
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        chatRoomAdapter = new ChatRoomAdapter(loginUser);
        chatRoomAdapter.bindToRecyclerView(recyclerView);

        //getData();

    }

    private void getData() {
        BmobQuery<ChatRoom> query = new BmobQuery<>();
        //查询所有聊天室，包括查询创建者的信息和 人数
        query.include("createUser,users");
        query.order("-createdAt")
                .findObjects(new FindListener<ChatRoom>() {
                    @Override
                    public void done(List<ChatRoom> object, BmobException e) {
                        if (e == null) {
                            if (object != null) {
                                if (object.size() == 0) {
                                    noDataLayout.setVisibility(View.VISIBLE);
                                } else {
                                    noDataLayout.setVisibility(View.GONE);
                                }
                                chatRoomAdapter.setNewData(object);
                            }
                        } else {
                        }
                    }
                });
    }


    @OnClick({R.id.create_btn, R.id.no_data_layout, R.id.create_tv,R.id.btn_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_tv:
                startActivity(new Intent(mContext, ChatActivity.class));
                break;
            case R.id.create_tv:
            case R.id.create_btn:
                startActivityForResult(new Intent(mContext, CreateChatRoomActivity.class), 1001);
                break;
            case R.id.no_data_layout:
                break;
        }
    }
/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //新建聊天室成功后返回 重新请求数据
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            getData();
        }
    }*/
}
