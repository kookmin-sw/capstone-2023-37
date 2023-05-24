package com.recommend.application.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.recommend.application.MyApplication;
import com.recommend.application.R;

public class ToastUtil {
    static ToastUtil td;
    Context context;
    Toast toast;
    String msg;

    public static void show(int resId) {
        show(MyApplication.getInstance().getString(resId));
    }

    public static void show(String msg) {
        if (td == null) {
            td = new ToastUtil(MyApplication.getInstance());
        }
        td.setText(msg);
        td.create().show();
    }

    public static void shortShow(String msg,int gravity) {
        if (td == null) {
            td = new ToastUtil(MyApplication.getInstance());
        }

        td.setText(msg);
        td.createShort(gravity).show();
    }

    public static void shortShow(String msg) {
        if (td == null) {
            td = new ToastUtil(MyApplication.getInstance());
        }
        td.setText(msg);
        td.createShort(Gravity.CENTER).show();
    }

    public ToastUtil(Context context) {
        this.context = context;
    }

    public Toast create() {
        View contentView = View.inflate(context, R.layout.dialog_toast, null);
        TextView tvMsg = contentView.findViewById(R.id.tv_toast_msg);
        if(toast == null){
            toast = new Toast(context);
        }
        toast.setView(contentView);
        toast.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.BOTTOM, 0, 40);
        toast.setDuration(Toast.LENGTH_SHORT);
        tvMsg.setText(msg);
        return toast;
    }

    public Toast createShort(int gravity) {
        View contentView = View.inflate(context, R.layout.dialog_toast, null);
        TextView tvMsg = contentView.findViewById(R.id.tv_toast_msg);
        if(toast == null){
            toast = new Toast(context);
        }
        toast.setView(contentView);
        toast.setGravity(Gravity.CENTER_HORIZONTAL| gravity, 0,
                dp2px(context.getResources().getDimension(R.dimen.toolbar_Height)));
        toast.setDuration(Toast.LENGTH_SHORT);
        tvMsg.setText(msg);
        return toast;
    }

    public void show() {
        if (toast != null) {
            toast.show();
        }
    }

    public void setText(String text) {
        msg = text;
    }

    static int dp2px(float dp) {
        final float scale = MyApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
