package com.recommend.application.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.recommend.application.MyApplication;
import com.recommend.application.R;
import com.recommend.application.base.BaseActivity;
import com.recommend.application.bean.bmob.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditInfoActivity extends BaseActivity {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.circle_img)
    CircleImageView circleImg;
    @BindView(R.id.name_edit)
    EditText nameEdit;
    @BindView(R.id.introduction_edit)
    EditText introductionEdit;
    @BindView(R.id.phone_edit)
    EditText phoneEdit;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.man)
    RadioButton man;
    @BindView(R.id.woman)
    RadioButton woman;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.korea)
    RadioButton korea;
    @BindView(R.id.chincest)
    RadioButton chincest;
    @BindView(R.id.radio_group_country)
    RadioGroup radioGroupCountry;

    private User user;
    private int requset_photo = 11;
    private ProgressDialog progressDialog;
    private StringBuilder img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_edit_info);
        user = MyApplication.getInstance().getUser();
        img = new StringBuilder();
        if (user != null) {
            //设置默认用户信息
            if (!"No nickname".equals(user.getNickName()))
                nameEdit.setText(user.getNickName());
            introductionEdit.setText(user.getIntroduction());
            if (!TextUtils.isEmpty(user.getPhone()))
                phoneEdit.setText(user.getPhone().substring(0, 3) + "****" + user.getPhone().substring(7));
            if (!TextUtils.isEmpty(user.getHeadPicture())) {
                Glide.with(mActivity).load(user.getHeadPicture()).into(circleImg);
            }
            man.setChecked(user.getGender() == 0);
            woman.setChecked(user.getGender() == 1);
            korea.setChecked(user.getCountry() == 1);
            chincest.setChecked(user.getCountry() == 0);
        }


    }

    @Override
    protected void initView() {

    }

    @Override
    protected int setLayout() {
        return R.layout.activity_edit_info;
    }

    @OnClick({R.id.back_layout, R.id.circle_img, R.id.login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.circle_img:
                if (XXPermissions.isGranted(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Log.i(TAG, "hasPermission: ");
                    PictureSelector.create(this)
                            .openGallery(PictureMimeType.ofImage())
                            .maxSelectNum(1)
                            .isCamera(true)
                            .enableCrop(false)
                            .withAspectRatio(1, 1)
                            .compress(true)
                            .cropCompressQuality(40)
                            .forResult(requset_photo);
                } else {
                    XXPermissions.with(this)
                            .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.READ_EXTERNAL_STORAGE)
                            .request(new OnPermissionCallback() {
                                @Override
                                public void onGranted(List<String> permissions, boolean all) {
                                    if (all) {

                                    } else {

                                    }
                                }

                                @Override
                                public void onDenied(List<String> permissions, boolean never) {
                                    if (never) {
                                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                                        XXPermissions.startPermissionActivity(mContext, permissions);
                                    }
                                    showToast("Authorization failure");
                                }
                            });
                }
                break;
            case R.id.login_btn:
                String nickName = nameEdit.getText().toString();
                String introduction = introductionEdit.getText().toString();

                if (TextUtils.isEmpty(nickName)) {
                    showToast("Please enter a nickname");
                    return;
                }

                if (user != null) {
                    user.setNickName(nickName);
                    if (!TextUtils.isEmpty(introduction))
                        user.setIntroduction(introduction);
                    if (img.length() != 0) {
                        user.setHeadPicture(img.toString());
                    }
                    user.setCountry(korea.isChecked() ? 1 : 0);
                    user.setGender(man.isChecked() ? 0 : 1);
                    //调用修改方法 并监听回调
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                //成功则更新本地数据
                                MyApplication.getInstance().setLoginUser(user);
                                showToast("Save successfully");
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                showToast("Save failure,Please try again!");
                            }
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == XXPermissions.REQUEST_CODE) {
            if (XXPermissions.isGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //toast("用户已经在权限设置页授予了录音和日历权限");
            } else {
                //toast("用户没有在权限设置页授予权限");
                new AlertDialog.Builder(mContext)
                        .setTitle("Tips")
                        .setCancelable(false)
                        .setMessage("Authorization failure")
                        .setPositiveButton("i know",
                                (dialogInterface, i) -> {
                                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
                                    //System.exit(0);//正常退出
                                })
                        .show();
            }
        }
        if (requestCode == requset_photo) {
            if (data != null) {
                List<LocalMedia> uris = PictureSelector.obtainMultipleResult(data);
                List<File> files = new ArrayList<>();
                List<String> pathList = new ArrayList<>();
                if (uris.size() > 0) {
                    for (int i = 0; i < uris.size(); i++) {
                        if (uris.get(i).isCompressed()) {
                            pathList.add(uris.get(i).getCompressPath());
                            files.add(new File(uris.get(i).getCompressPath()));
                        } else {
                            files.add(new File(uris.get(i).getPath()));
                            pathList.add(uris.get(i).getPath());
                        }
                    }

                    Glide.with(mActivity).load(pathList.get(0)).into(circleImg);
                    if (files.size() > 0) {
                        progressDialog = ProgressDialog.show(this, "", "In the picture uploaded");
                        progressDialog.show();
                        for (String str : pathList) {
                            BmobFile bmobFile = new BmobFile(new File(str));
                            bmobFile.uploadblock(new UploadFileListener() {

                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                                        progressDialog.dismiss();
                                        showToast("Upload successfully");
                                        if (img.length() == 0)
                                            img.append(bmobFile.getFileUrl());
                                        else img.append(",").append(bmobFile.getFileUrl());
                                        Log.e(TAG, "上传文件成功: " + img);
                                    } else {
                                        progressDialog.dismiss();
                                        showToast("Upload failure");
                                        Log.e(TAG, "上传文件失败: " + e.getMessage());
                                    }
                                }

                                @Override
                                public void onProgress(Integer value) {
                                    // 返回的上传进度（百分比）
                                }
                            });
                        }
                    }
                }
            }
        }
    }
}
