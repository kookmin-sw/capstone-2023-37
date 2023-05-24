package com.recommend.application.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.recommend.application.utils.ToastUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * BaseFragment,Fragment的基类
 */
public abstract class BaseFragment extends Fragment {

    protected View mView;
    protected Activity mActivity;
    protected Context mContext;

    Unbinder unbinder;
    protected String TAG;


    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        mContext = context;
        TAG = this.getClass().getSimpleName();
        super.onAttach(context);
    }






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), null);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);
        initEventAndData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (unbinder != null)
            unbinder.unbind();
    }




    protected abstract int getLayoutId();

    protected abstract void initEventAndData();




    protected void showToast(String msg) {
        if (!TextUtils.isEmpty(msg))
            ToastUtil.shortShow(msg);
    }
}
