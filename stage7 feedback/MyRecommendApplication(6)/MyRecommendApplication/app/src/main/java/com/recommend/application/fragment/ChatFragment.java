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

}
