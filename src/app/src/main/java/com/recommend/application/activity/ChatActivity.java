package com.recommend.application.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gyf.immersionbar.ImmersionBar;
import com.recommend.application.MyApplication;
import com.recommend.application.R;
import com.recommend.application.adapter.ChatAdapter;
import com.recommend.application.base.BaseActivity;
import com.recommend.application.bean.bmob.ChatBean;
import com.recommend.application.bean.bmob.User;
import com.recommend.application.utils.MyTimeTask;

import java.util.List;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class ChatActivity extends BaseActivity {
    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.fasong)
    Button fasong;
    @BindView(R.id.more_img)
    ImageView moreImg;
    private ChatAdapter chatAdapter;
    private User loginUser;//当前登录人
    private ChatBean lastChatBean;
    private int page = 1;
    private int isFirst = 0;
    private MyTimeTask timeTask;

    //间隔10s请求一次消息
    private void startTask() {
        timeTask = new MyTimeTask(new TimerTask() {
            @Override
            public void run() {
                Log.e(TAG, "run: 请求消息");
                getData();
            }
        });
        timeTask.startTask(1000 * 10);

    }

    @Override
    protected int setLayout() {
        return R.layout.activity_chat;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_chat);
    }

    @Override
    protected void initView() {
        if (isDark) {
            ImmersionBar.with(mActivity).statusBarColor(R.color.white).statusBarDarkFont(false).init();
        } else {
            ImmersionBar.with(mActivity).statusBarColor(R.color.white).statusBarDarkFont(true).init();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        loginUser = MyApplication.getInstance().getUser();
        chatAdapter = new ChatAdapter(loginUser);
        chatAdapter.bindToRecyclerView(recyclerView);

        //开启定时任务
        startTask();
    }

    private void getData() {
        BmobQuery<ChatBean> query = new BmobQuery<>();
        query.include("user");
        query.findObjects(new FindListener<ChatBean>() {
            @Override
            public void done(List<ChatBean> object, BmobException e) {
                if (e == null) {
                    if (object != null) {
                        Log.e(TAG, "done: " + object.size());
                        if (object.size() > 0) {
                            if (lastChatBean != null) {
                                ChatBean c = object.get(object.size() - 1);
                                if (lastChatBean.getObjectId().equals(c.getObjectId())) {

                                } else {
                                    //如果有新消息，则直接加入消息列表
                                    try {
                                        for (int i = 0; i < object.size(); i++) {
                                            for (int j = 0; j < chatAdapter.getData().size(); j++) {
                                                if (object.get(i).getObjectId().equals(chatAdapter.getData().get(j).getObjectId())) {
                                                    object.remove(i);
                                                    i--;
                                                }
                                            }
                                        }
                                    } catch (ArrayIndexOutOfBoundsException ee) {
                                        ee.printStackTrace();
                                    }
                                    chatAdapter.setNewData(object);
                                    //列表滑动到最后一条消息处
                                    recyclerView.smoothScrollToPosition(chatAdapter.getData().size());
                                }
                            } else {
                                chatAdapter.setNewData(object);
                                recyclerView.smoothScrollToPosition(chatAdapter.getData().size());
                            }
                            //记录最后一条消息
                            lastChatBean = object.get(object.size() - 1);
                        }
                    }
                }
                refreshLayout.setRefreshing(false);
            }
        });
    }


    @OnClick({R.id.back_img, R.id.fasong,R.id.more_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.more_img:
                startActivity(new Intent(mContext,UserListActivity.class));
                break;
            case R.id.back_img:
                finish();
                break;
            case R.id.fasong:
                //发送新消息
                String name = content.getText().toString();
                if (!TextUtils.isEmpty(name)) {
                    ChatBean chatBean = new ChatBean();
                    chatBean.setContent(name);
                    chatBean.setUser(loginUser);
                    chatBean.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                //toast("添加数据成功，返回objectId为：" + objectId);
                                content.setText("");
                                chatAdapter.addData(chatBean);
                                lastChatBean = chatBean;
                                recyclerView.smoothScrollToPosition(chatAdapter.getData().size());
                            } else {
                                Log.e(TAG, "done: " + e.toString());
                                //toast("创建数据失败：" + e.getMessage());
                                showToast("Send failure," + e.getMessage() + "  " + e.getErrorCode());
                            }
                            content.setText("");
                            content.clearFocus();
                        }
                    });
                }
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeTask != null) {
            timeTask.stopTask();
        }
    }
}
