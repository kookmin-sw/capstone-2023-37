package com.recommend.application.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.recommend.application.MyApplication;
import com.recommend.application.R;
import com.recommend.application.adapter.GridAdapter;
import com.recommend.application.base.BaseActivity;
import com.recommend.application.bean.bmob.Post;
import com.recommend.application.bean.bmob.User;
import com.recommend.application.utils.Constants;
import com.recommend.application.utils.MyGridView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class RelastePostActivity extends BaseActivity {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.content_edit)
    EditText contentEdit;
    @BindView(R.id.grid_view)
    MyGridView gridView;
    @BindView(R.id.login_btn)
    Button loginBtn;

    private GridAdapter gridAdapter;
    private boolean isEdit = true;
    private boolean isAdd = true;
    private int i = 9;
    private ArrayList<String> imageList = new ArrayList<>();
    private int requset_photo = 11;
    private Map<String, BmobFile> map = new HashMap<>();
    private StringBuilder img = new StringBuilder();
    private User loginUser;
    private Post post;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_relaste_post);
    }

    @Override
    protected void initView() {
        if (isDark) {
            ImmersionBar.with(mActivity).statusBarColor(R.color.white).statusBarDarkFont(false).init();
        } else {
            ImmersionBar.with(mActivity).statusBarColor(R.color.white).statusBarDarkFont(true).init();
        }
        loginUser = MyApplication.getInstance().getUser();
        post = (Post) getIntent().getSerializableExtra(Constants.DATA);
        gridAdapter = new GridAdapter(imageList, mContext, isEdit, isAdd);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener((parent, view, position, id) -> {
                    requestPermissions(parent, view, position, id);
                }
        );

        if (post != null){
            contentEdit.setText(post.getContent());
            if (!TextUtils.isEmpty(post.getImage())){
                String[] stringsArray = post.getImage().split(",");
                for (String str:stringsArray){
                    imageList.add(str);
                }
                gridAdapter.notifyDataSetChanged();
            }


        }
    }


    private void requestPermissions(AdapterView<?>
                                            adapterView, View view, int i, long l) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // TODO 申请权限成功。
            if (XXPermissions.isGranted(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Log.i(TAG, "hasPermission: ");
                if (i == adapterView.getChildCount() - 1) {
                    //如果“增加按钮形状的”图片的位置是最后一张，且添加了的图片的数量不超过 ContastUtil.MAX_IMAGES 张，才能点击
                    if (imageList.size() == 9) {
                        //viewPluImg(i, imageList);
                    } else {
                        //添加凭证图片
                        selectPic(9 - imageList.size());
                    }
                } else {
                    //viewPluImg(i, imageList);
                }
            } else {
                //请求读写权限 获取手机相册图片数据
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
        } else {
            Log.i(TAG, "SDK_INT: ");
            if (i == adapterView.getChildCount() - 1) {
                //如果“增加按钮形状的”图片的位置是最后一张，且添加了的图片的数量不超过 ContastUtil.MAX_IMAGES 张，才能点击
                if (imageList.size() == 9) {
                    //最多添加50张图片
                    //viewPluImg(i, imageList);
                } else {
                    //添加凭证图片
                    selectPic(9 - imageList.size());
                }
            } else {
                // viewPluImg(i, imageList);
            }
        }
    }

    private void selectPic(int i) {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(9)
                .isCamera(true)
                .enableCrop(false)
                .withAspectRatio(1, 1)
                .compress(true)
                .cropCompressQuality(40)
                .forResult(requset_photo);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_relaste_post;
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
                if (imageList == null || imageList.size() == 0) {
                    showToast("Please add pictures");
                    return;
                }

                loginBtn.setEnabled(false);
                for (String str:imageList){
                    if (img.length() == 0)
                        img.append(str);
                    else img.append(",").append(str);
                }

                Log.e(TAG, "onViewClicked: " + img.toString());
                if (post == null){
                    post = new Post();
                    post.setContent(context);
                    post.setAuthor(loginUser);
                    post.setImage(img.toString());
                    post.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                //toast("添加数据成功，返回objectId为：" + objectId);
                                showToast("Successful release");
                                setResult(RESULT_OK, new Intent().putExtra(Constants.ID, post.getObjectId()));
                                finish();
                            } else {
                                showToast("Release failure! Please try again");
                                //toast("创建数据失败：" + e.getMessage());
                                loginBtn.setEnabled(true);
                            }
                        }
                    });
                }else {
                    post.setContent(context);
                    post.setImage(img.toString());
                    post.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                //toast("添加数据成功，返回objectId为：" + objectId);
                                showToast("Modified successfully");
                                setResult(RESULT_OK, new Intent().putExtra(Constants.DATA, post));
                                finish();
                            } else {
                                showToast("Modified failure! Please try again");
                                //toast("创建数据失败：" + e.getMessage());
                                loginBtn.setEnabled(true);
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

                    if (files.size() > 0) {
                        progressDialog = ProgressDialog.show(this,"","In the picture uploaded");
                        progressDialog.show();
                        map.clear();
                        for (File str : files) {
                            BmobFile bmobFile = new BmobFile(str);
                            bmobFile.uploadblock(new UploadFileListener() {

                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                                        map.put(str.getPath(),bmobFile);
                                        if (map.size() == files.size()){
                                            progressDialog.dismiss();
                                            showToast("Upload successfully");
                                        }
                                        /*if (img.length() == 0)
                                            img.append(bmobFile.getFileUrl());
                                        else img.append(",").append(bmobFile.getFileUrl());*/
                                        imageList.add(bmobFile.getFileUrl());
                                        gridAdapter.notifyDataSetChanged();
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
