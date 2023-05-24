package com.recommend.application.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.recommend.application.MyApplication;
import com.recommend.application.R;
import com.recommend.application.activity.AboutActivity;
import com.recommend.application.activity.EditInfoActivity;
import com.recommend.application.activity.FeedbackListActivity;
import com.recommend.application.activity.LoginActivity;
import com.recommend.application.activity.MyPostActivity;
import com.recommend.application.base.BaseFragment;
import com.recommend.application.bean.bmob.User;
import com.recommend.application.utils.CleanDataUtils;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class MineFragment extends BaseFragment {


    @BindView(R.id.circle_img)
    CircleImageView circleImg;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.introduction_tv)
    TextView introductionTv;
    @BindView(R.id.top_layout)
    LinearLayout topLayout;
    @BindView(R.id.personal_layout)
    LinearLayout personalLayout;

    @BindView(R.id.cache_tv)
    TextView cacheTv;
    @BindView(R.id.clear_cahe_layout)
    LinearLayout clearCaheLayout;
    @BindView(R.id.about_layout)
    LinearLayout aboutLayout;
    @BindView(R.id.login_out_tv)
    TextView loginOutTv;
    @BindView(R.id.my_post_layout)
    LinearLayout myPostLayout;
    @BindView(R.id.feedback_layout)
    LinearLayout feedbackLayout;
    private User user;

    /*@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_mine, container, false);
        }
    */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initEventAndData() {
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
        try {
            String totalCacheSize = CleanDataUtils.getTotalCacheSize(mActivity);
            cacheTv.setText(totalCacheSize);
        } catch (Exception ignored) {
        }
    }

    @OnClick({R.id.personal_layout, R.id.clear_cahe_layout, R.id.login_out_tv,
            R.id.my_post_layout, R.id.about_layout,R.id.feedback_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.feedback_layout:
                startActivity(new Intent(mActivity, FeedbackListActivity.class));
                break;
            case R.id.about_layout:
                startActivity(new Intent(mActivity, AboutActivity.class));
                break;
            case R.id.my_post_layout:
                startActivity(new Intent(mActivity, MyPostActivity.class));
                break;
            case R.id.personal_layout:
                Intent intent = new Intent(mContext, EditInfoActivity.class);
                startActivityForResult(intent, 11);
                break;
            case R.id.clear_cahe_layout:
                if (!TextUtils.isEmpty(cacheTv.getText().toString())) {
                    try {
                        CleanDataUtils.clearAllCache(mActivity);
                        cacheTv.setText("");
                        Toast.makeText(mActivity, "Clearing cache succeeded", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.login_out_tv:
                new AlertDialog.Builder(mContext)
                        .setTitle("Tip")
                        .setMessage("Are you sure you want to exit account?")
                        .setNeutralButton("exit", (dialog, which) -> {
                            MyApplication.getInstance().setLoginUser(null);
                            startActivity(new Intent(mActivity,
                                    LoginActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));

                        })
                        .setPositiveButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 11:
                user = MyApplication.getInstance().getUser();
                if (user != null) {
                    MyApplication.getInstance().setLoginUser(user);
                    if (!TextUtils.isEmpty(user.getHeadPicture())) {
                        Glide.with(mActivity).load(user.getHeadPicture()).into(circleImg);
                    }
                    Drawable drawable;
                    if (user.getGender() == 0) {
                        drawable = mActivity.getResources().getDrawable(R.drawable.man);
                    } else {
                        drawable = mActivity.getResources().getDrawable(R.drawable.woman);
                    }
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    nameTv.setCompoundDrawables(null, null, drawable, null);
                    nameTv.setText(user.getNickName());
                    introductionTv.setText(user.getIntroduction());
                }

                break;
        }
    }
}
