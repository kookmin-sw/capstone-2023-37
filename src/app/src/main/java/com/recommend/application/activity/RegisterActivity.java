package com.recommend.application.activity;


import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.immersionbar.ImmersionBar;
import com.recommend.application.R;
import com.recommend.application.base.BaseActivity;
import com.recommend.application.bean.bmob.User;
import com.recommend.application.utils.Constants;
import com.recommend.application.utils.SharedPreferenceUtil;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.account_edit)
    EditText accountEdit;
    @BindView(R.id.code_edit)
    EditText codeEdit;
    @BindView(R.id.get_code_btn)
    Button getCodeBtn;
    @BindView(R.id.pwd_edit)
    EditText pwdEdit;
    @BindView(R.id.create_btn)
    Button createBtn;
    @BindView(R.id.title_tv)
    TextView titleTv;

    private int type = 0;//0为注册    1为重设密码

    private CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            if (!mActivity.isFinishing()) {
                //单位天
                long day = millisUntilFinished / (1000 * 24 * 60 * 60);
                //单位时
                long hour = (millisUntilFinished - day * (1000 * 24 * 60 * 60)) / (1000 * 60 * 60);
                //单位分
                long minute = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60)) / (1000 * 60);
                //单位秒
                long second = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000;

                getCodeBtn.setText(String.format(Locale.CHINA, "%ds later", second));
            }
        }

        /**
         *倒计时结束后调用的
         */

        @Override
        public void onFinish() {
            if (!mActivity.isFinishing()) {
                if (accountEdit != null) {
                    String phone = accountEdit.getText().toString();
                    if (!TextUtils.isEmpty(phone) && phone.length() == 11) {
                        if (getCodeBtn != null) {
                            getCodeBtn.setEnabled(true);
                            getCodeBtn.setBackgroundColor(mActivity.getResources().getColor(R.color._007FFF));

                        }
                    } else {
                        if (getCodeBtn != null) {
                            getCodeBtn.setEnabled(false);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                getCodeBtn.setBackgroundColor(mActivity.getResources().getColor(R.color.dcdcdc));

                            }
                        }
                    }
                }
                if (getCodeBtn != null) {
                    getCodeBtn.setText(mContext.getResources().getString(R.string.get));
                }

            }
        }

    };

    @Override
    protected int setLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_register);
    }

    @Override
    protected void initView() {

        type = getIntent().getIntExtra(Constants.TYPE, 0);
        if (isDark) {
            ImmersionBar.with(mActivity).statusBarColor(R.color.white).statusBarDarkFont(false).init();
        } else {
            ImmersionBar.with(mActivity).statusBarColor(R.color.white).statusBarDarkFont(true).init();
        }

        //监听手机号输入框
        accountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                //如果为空  获取验证码按钮不可点击
                if (TextUtils.isEmpty(str) || str.length() != 11) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        getCodeBtn.setBackgroundColor(mActivity.getResources().getColor(R.color.dcdcdc));
                    }
                    getCodeBtn.setEnabled(false);
                } else {
                    getCodeBtn.setEnabled(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        getCodeBtn.setBackgroundColor(mActivity.getResources().getColor(R.color._007FFF));
                    }
                }
            }
        });
        if (type == 0) {
            titleTv.setText("Reset password");
            createBtn.setText("confirm");
        }

    }


    @OnClick({R.id.back_img, R.id.get_code_btn, R.id.create_btn})
    public void onViewClicked(View view) {
        String phone = accountEdit.getText().toString();
        String password = pwdEdit.getText().toString();
        String code = codeEdit.getText().toString();
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.get_code_btn:

                BmobSMS.requestSMSCode(phone, "DailyLife", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer smsId, BmobException e) {
                        if (e == null) {
                            countDownTimer.start();//获取成功，并开启倒计时功能
                            getCodeBtn.setBackgroundColor(mActivity.getResources().getColor(R.color.dcdcdc));
                            getCodeBtn.setEnabled(false);//倒计时中 获取验证码按钮不可点击
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                getCodeBtn.setBackground(null);
                            }
                            Toast.makeText(mActivity, "The verification code is successfully obtained", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "done: " + "ID：" + smsId + "\n");
                        } else {
                            Toast.makeText(mActivity, "Failed to obtain the verification code.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "done: " + "Failed：" + e.getErrorCode() + "-" + e.getMessage() + "\n");
                        }
                    }
                });
                break;
            case R.id.create_btn:
                if (TextUtils.isEmpty(phone)) {
                    showToast("Please input phone");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    showToast("Please input code");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    showToast("Please input password");
                    return;
                }
                //先查询该手机号是否已经注册
                BmobQuery<User> query = new BmobQuery<>();
                query.addWhereEqualTo("phone", phone);
                query.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if (e == null) {
                            if (type == 1) {//如果为重设密码状态
                                if (list == null || list.size() == 0) {//重设密码时  账号不存在
                                    Toast.makeText(mActivity, "The account does not exist", Toast.LENGTH_SHORT).show();
                                } else {
                                    CheckVerificationCode(phone, password, code,list.get(0));

                                }
                            } else {
                                if (list == null || list.size() == 0) {
                                    CheckVerificationCode(phone, password, code,null);
                                } else {
                                    Toast.makeText(mActivity, "The phone number has been registered", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.unknown_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }

    //验证短信验证码是否正确
    private void CheckVerificationCode(String phone, String password, String code,User user) {
        BmobSMS.verifySmsCode(phone, code, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (type == 1){
                        if (user != null) {
                            user.setPwd(password);//重新设置密码
                            //调用更新方法
                            user.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(mActivity, "Succeeded in resetting the password.", Toast.LENGTH_SHORT).show();
                                        SharedPreferenceUtil.putString(mActivity, Constants.PHONE, phone);
                                        finish();
                                    } else {
                                        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.unknown_error), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }else {
                        //创建新用户
                        User user = new User();
                        user.setPhone(phone);
                        user.setPwd(password);
                        user.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    Toast.makeText(mActivity, "Successful registration", Toast.LENGTH_SHORT).show();
                                    SharedPreferenceUtil.putString(mActivity, Constants.PHONE, phone);
                                    finish();
                                } else {
                                    Toast.makeText(mActivity, "Registration failure", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }


                } else {
                    Toast.makeText(mActivity, "Verification code error", Toast.LENGTH_SHORT).show();
                    //mTvInfo.append("Verification code error" + e.getErrorCode() + "-" + e.getMessage() + "\n");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
