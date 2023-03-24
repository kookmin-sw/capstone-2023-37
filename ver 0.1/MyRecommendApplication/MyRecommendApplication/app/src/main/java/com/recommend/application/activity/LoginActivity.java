package com.recommend.application.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.immersionbar.ImmersionBar;
import com.recommend.application.MyApplication;
import com.recommend.application.R;
import com.recommend.application.base.BaseActivity;
import com.recommend.application.bean.User;
import com.recommend.application.utils.Constants;
import com.recommend.application.utils.SharedPreferenceUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.account_edit)
    EditText accountEdit;
    @BindView(R.id.pwd_edit)
    EditText pwdEdit;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.register_tv)
    TextView registerTv;
    @BindView(R.id.forget_tv)
    TextView forgetTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accountEdit != null){
            //取出共享数据中存储的手机号，即上次登录的手机号
            accountEdit.setText(SharedPreferenceUtil.getString(mContext, Constants.PHONE,""));
        }
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        if (isDark) {
            ImmersionBar.with(mActivity).statusBarColor(R.color.white).statusBarDarkFont(false).init();
        } else {
            ImmersionBar.with(mActivity).statusBarColor(R.color.white).statusBarDarkFont(true).init();
        }
    }


    @OnClick({R.id.login_btn, R.id.register_tv, R.id.forget_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                String phone = accountEdit.getText().toString();//获取用户输入的号
                String pwd = pwdEdit.getText().toString();//获取用户输入的密码

                if (TextUtils.isEmpty(phone)) {//判断输入的手机号是否为空
                    showToast("Please input phone");//提示用户手机号不能为空
                    return;
                }

                if (TextUtils.isEmpty(pwd)) {//判断输入的密码是否为空
                    showToast("Please input password");
                    return;
                }
                //查询方法，先查询该手机号是否注册
                BmobQuery<User> query = new BmobQuery<>();
                query.addWhereEqualTo("phone", phone);//设置用户输入的手机
                query.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        //查询方法回调事件
                        if (e == null) {
                            if (list == null || list.size() == 0){//账号不存在即手机号未注册
                                Toast.makeText(mActivity, "The account does not exist", Toast.LENGTH_SHORT).show();
                            }else {
                                User user = list.get(0);//账号存在 则判断密码是否输入正确
                                if (user != null){
                                    if (pwd.equals(user.getPwd())){
                                        Toast.makeText(mActivity, "Login success", Toast.LENGTH_SHORT).show();
                                        MyApplication.getInstance().setLoginUser(user);
                                        SharedPreferenceUtil.putString(mContext,Constants.PHONE,phone);
                                        startActivity(new Intent(mActivity,MainActivity.class));
                                        finish();
                                    }else {
                                        Toast.makeText(mActivity, "Password entered incorrectly", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.unknown_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
            case R.id.register_tv:
                //跳转至注册页面
                startActivity(new Intent(mContext, RegisterActivity.class));
                break;
            case R.id.forget_tv:
                //跳转至重设密码界面
                startActivity(new Intent(mContext, RegisterActivity.class)
                .putExtra(Constants.TYPE,1));
                break;
        }
    }

}
