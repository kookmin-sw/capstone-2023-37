package com.recommend.application.fragment;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.recommend.application.R;
import com.recommend.application.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;


public class PostFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.floating_btn)
    FloatingActionButton floatingBtn;




  /*  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }*/

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_post;
    }

    @Override
    protected void initEventAndData() {

    }


    @OnClick({ R.id.floating_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.floating_btn:
                break;
        }
    }
}
