package com.recommend.application.fragment;


import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.recommend.application.MyApplication;
import com.recommend.application.R;
import com.recommend.application.activity.ConstellationActivity;
import com.recommend.application.adapter.ConstellationAdapter;
import com.recommend.application.base.BaseFragment;
import com.recommend.application.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class ConstellationFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    List<String> stringList = new ArrayList<>();
    ConstellationAdapter constellationAdapter;


    public static ConstellationFragment newInstance() {
        ConstellationFragment fragment = new ConstellationFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_constellation;
    }

    @Override
    protected void initEventAndData() {

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        stringList = MyApplication.getInstance().getConstellationList();
        //创建12星座数据

        constellationAdapter = new ConstellationAdapter();
        constellationAdapter.bindToRecyclerView(recyclerView);
        constellationAdapter.setNewData(stringList);
        constellationAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(mContext, ConstellationActivity.class);
            intent.putExtra(Constants.POSITION,position);
            startActivity(intent);
        });
    }


}
