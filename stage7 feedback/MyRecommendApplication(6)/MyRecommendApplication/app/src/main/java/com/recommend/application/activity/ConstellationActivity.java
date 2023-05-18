package com.recommend.application.activity;


import android.app.Person;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.recommend.application.MyApplication;
import com.recommend.application.R;
import com.recommend.application.base.BaseActivity;
import com.recommend.application.bean.bmob.Constellation;
import com.recommend.application.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ConstellationActivity extends BaseActivity {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.time_tv)
    TextView timeTv;
    @BindView(R.id.content_tv)
    TextView contentTv;

    private int position;
    private String consName = "水瓶座";
    private Constellation constellation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int setLayout() {
        return R.layout.activity_constellation;
    }

    @Override
    protected void initView() {
        if (isDark) {
            ImmersionBar.with(mActivity).statusBarColor(R.color.white).statusBarDarkFont(false).init();
        } else {
            ImmersionBar.with(mActivity).statusBarColor(R.color.white).statusBarDarkFont(true).init();
        }
        titleTv.setText("Your month horoscope");
        position = getIntent().getIntExtra(Constants.POSITION, 0);
        switch (position) {
            case 0:
                consName = "水瓶座";
                break;
            case 1:
                consName = "双鱼座";
                break;
            case 2:
                consName = "白羊座";
                break;
            case 3:
                consName = "金牛座";
                break;
            case 4:
                consName = "双子座";
                break;
            case 5:
                consName = "巨蟹座";
                break;
            case 6:
                consName = "狮子座";
                break;
            case 7:
                consName = "处女座";
                break;
            case 8:
                consName = "天秤座";
                break;
            case 9:
                consName = "天蝎座";
                break;
            case 10:
                consName = "射手座";
                break;
            case 11:
                consName = "摩羯座";
                break;
        }
        Calendar calendar = Calendar.getInstance();

        int thisMonth = calendar.get(Calendar.MONTH);
        thisMonth = thisMonth+1;
        Log.d(TAG, "@ thisMonth : " + thisMonth);
        //--and条件1
        BmobQuery<Constellation> eq1 = new BmobQuery<>();
        eq1.addWhereEqualTo("name", consName);
        //--and条件2
        BmobQuery<Constellation> eq2 = new BmobQuery<>();
        eq2.addWhereEqualTo("month", thisMonth);

        //最后组装完整的and条件
        List<BmobQuery<Constellation>> andQuerys = new ArrayList<>();
        andQuerys.add(eq1);
        andQuerys.add(eq2);
        //查询符合整个and条件的人
        BmobQuery<Constellation> query = new BmobQuery<>();
        query.and(andQuerys);
        query.findObjects(new FindListener<Constellation>() {
            @Override
            public void done(List<Constellation> object, BmobException e) {
                if (e == null) {
                    if (object == null || object.size() == 0){
                        getJuHeData();
                    }else {
                        constellation = object.get(0);
                        setData();
                    }
                } else {
                    getJuHeData();
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });


    }

    private void getJuHeData() {
        //1.拿到okHttpClient对象,可以设置连接超时等
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.构造Request请求对象，可以增加头addHeader等
        Request.Builder builder = new Request.Builder();
        String url = "http://web.juhe.cn/constellation/getAll?key=597babff9013db4666dd5bff34f86a1d&consName="+consName+
                "&type=month";
        Log.e(TAG, "url: "+url);
        //url()中可以放入网址
        Request request = builder.
                get().url(url)
                .build();
        //3.将Request封装为Call
        Call call = okHttpClient.newCall(request);
        //4.执行call
        //方法一Response response=call.execute();//汇抛出IO异常，同步方法
        //方法二,异步方法，放到队列中,处于子线程中，无法更新UI
        call.enqueue(new Callback() {
            //请求时失败时调用
            @Override
            public void onFailure(Call call, IOException e) {

            }

            //请求成功时调用
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //处于子线程中，能够进行大文件下载，但是无法更新UI
                final String res = response.body().string();//请求成功时返回的东西
                //InputStream is=response.body().byteStream();
                // 执行IO操作时，能够下载很大的文件，并且不会占用很大内存
                /**
                 * runOnUiThread方法切换到主线程中，或者用handler机制也可以
                 */
                Log.e(TAG, "onResponse: " + res);
                if (!TextUtils.isEmpty(res)){
                    constellation = new Gson().fromJson(res,Constellation.class);
                    if (constellation != null){
                        constellation.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null){

                                }else {
                                    Log.e(TAG, "done: "+e.toString());
                                }
                            }
                        });
                        ConstellationActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setData();
                            }
                        });
                    }
                }

            }
        });


    }

    private void setData() {
        nameTv.setText(constellation.getName());
        timeTv.setText(constellation.getDate());
        contentTv.setText("\t\t"+constellation.getHealth()+"\n\n"+
                "\t\t"+constellation.getLove()+"\n\n"+
                "\t\t"+constellation.getMoney()+"\n\n");

    }


    @OnClick({R.id.back_img, R.id.title_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.title_tv:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
