package com.recommend.application.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.recommend.application.MyApplication;
import com.recommend.application.R;
import com.recommend.application.base.BaseActivity;
import com.recommend.application.bean.bmob.ChatRoom;
import com.recommend.application.bean.bmob.User;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class CreateChatRoomActivity extends BaseActivity {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.name_edit)
    EditText nameEdit;
    @BindView(R.id.num_edit)
    EditText numEdit;
    @BindView(R.id.remake_edit)
    EditText remakeEdit;
    @BindView(R.id.login_btn)
    Button loginBtn;
    private User loginUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_create_chat_room);
    }

    @Override
    protected void initView() {
        //获取当前登录人
        loginUser = MyApplication.getInstance().getUser();

    }

    @Override
    protected int setLayout() {
        return R.layout.activity_create_chat_room;
    }

    @OnClick({R.id.back_img, R.id.login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.login_btn:
                String name = nameEdit.getText().toString();
                String remake = remakeEdit.getText().toString();
                String num = numEdit.getText().toString();

                if (TextUtils.isEmpty(name)){
                    showToast("Please enter ChatRoom name");
                    return;
                }
                if (TextUtils.isEmpty(num)){
                    showToast("Maximum number of people");
                    return;
                }
                if (TextUtils.isEmpty(remake)){
                    showToast("Please enter Introduction");
                    return;
                }
                //新建聊天室 并将填写的值赋给新建对象
                ChatRoom chatRoom = new ChatRoom();
                chatRoom.setCreateUser(loginUser);
                chatRoom.setMaxNum(Integer.parseInt(num));
                chatRoom.setName(name);
                chatRoom.setRemake(remake);
                chatRoom.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null){
                            showToast("Create successfully");
                            setResult(RESULT_OK);
                            finish();
                        }else {
                            showToast(e.getMessage());
                        }
                    }
                });

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
}
