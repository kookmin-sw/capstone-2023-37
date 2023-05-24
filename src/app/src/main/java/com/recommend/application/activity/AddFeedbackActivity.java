package com.recommend.application.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.recommend.application.MyApplication;
import com.recommend.application.R;
import com.recommend.application.base.BaseActivity;
import com.recommend.application.bean.bmob.Feedback;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class AddFeedbackActivity extends BaseActivity {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.content_edit)
    EditText contentEdit;
    @BindView(R.id.login_btn)
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_add_feedback);
    }

    @Override
    protected void initView() {
        titleTv.setText("Add Feedback");
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_add_feedback;
    }

    @OnClick({R.id.back_img, R.id.login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.login_btn:
                String context = contentEdit.getText().toString();
                if (TextUtils.isEmpty(context)) {
                    showToast(contentEdit.getHint().toString());
                    return;
                }
                Feedback feedback = new Feedback();
                feedback.setUser(MyApplication.getInstance().getUser());
                feedback.setContent(context);
                feedback.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null){
                            Toast.makeText(mActivity, "Submitted successfully", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        }else {
                            Toast.makeText(mActivity, "Submitted failure", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "done: "+e.toString());
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
