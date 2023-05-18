package com.recommend.application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.recommend.application.R;


import java.io.File;
import java.util.List;
import java.util.Objects;

public class GridAdapter extends BaseAdapter {
    private List<String> datas;
    private Context context;
    private LayoutInflater inflater;
    /**
     * 可以动态设置最多上传几张，之后就不显示+号了，用户也无法上传了
     * 默认9张
     */
    private int maxImages = 9;
    private boolean isEdit;
    private boolean isAdd;

    public GridAdapter(List<String> datas, Context context, boolean isEdit, boolean isAdd) {
        this.datas = datas;
        this.context = context;
        this.isEdit = isEdit;
        this.isAdd = isAdd;
        inflater = LayoutInflater.from(context);
    }

    public void setPathList(List<String> pathList) {
        this.datas = pathList;
    }
    /**
     * 获取最大上传张数
     *
     *
     */
    public int getMaxImages() {
        return maxImages;
    }

    /**
     * 设置最大上传张数
     *
     *
     */
    public void setMaxImages(int maxImages) {
        this.maxImages = maxImages;
    }

    /**
     * 让GridView中的数据数目加1最后一个显示+号
     * 当到达最大张数时不再显示+号
     * @return 返回GridView中的数量
     */
    @Override
    public int getCount() {
        int count;
        if (isAdd){
            count = datas == null ? 1 : datas.size() + 1;
        }else {
            count = datas.size();
        }
        if (count >= maxImages) {
            return Objects.requireNonNull(datas).size();
        } else {
            return count;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void notifyDataSetChanged(List<String> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.img_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /*
         *代表+号之前的需要正常显示图片**/
        if (datas != null && position < datas.size()) {
            if (datas.get(position).contains("http")){
                Glide.with(context)
                        .load(datas.get(position))
                        //.priority(Priority.HIGH)
                        .into(viewHolder.ivimage);
                if (isEdit){
                    viewHolder.btdel.setVisibility(View.VISIBLE);
                    viewHolder.btdel.setOnClickListener(v -> {
                        datas.remove(position);
                        notifyDataSetChanged();
                    });
                }
            }else {
                final File file = new File(datas.get(position));
                Glide.with(context)
                        .load(file)
                        //.priority(Priority.HIGH)
                        .into(viewHolder.ivimage);
                viewHolder.btdel.setVisibility(View.VISIBLE);
                viewHolder.btdel.setOnClickListener(v -> {
                    if (file.exists()) {
                        file.delete();
                    }
                    datas.remove(position);
                    notifyDataSetChanged();
                });
            }


        } else {
            /*
             *代表+号的需要+号图片显示图片**/
            if (isAdd){
                Glide.with(context)
                        .load(R.drawable.ic_add_img)
                        //.priority(Priority.HIGH)
                        //.centerCrop()
                        .into(viewHolder.ivimage);
                viewHolder.ivimage.setScaleType(ImageView.ScaleType.FIT_XY);
                viewHolder.btdel.setVisibility(View.GONE);
            }
        }

        return convertView;

    }

    public static class ViewHolder {
        public final ImageView ivimage;
        public final ImageView btdel;
        public final View root;

        public ViewHolder(View root) {
            ivimage = root.findViewById(R.id.iv_img);
            btdel = root.findViewById(R.id.iv_delete);
            this.root = root;
        }
    }
}


