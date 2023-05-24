package com.recommend.application.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.recommend.application.R;
import com.recommend.application.base.BaseActivity;
import com.recommend.application.bean.WeatherBean;
import com.recommend.application.bean.WeatherJsonRootBean;
import com.recommend.application.bean.bmob.Constellation;
import com.recommend.application.utils.Constants;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeaterActivity extends BaseActivity {


    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.city_tv)
    TextView cityTv;
    @BindView(R.id.center_tv)
    TextView centerTv;
    @BindView(R.id.wind_tv)
    TextView windTv;
    @BindView(R.id.humidity_tv)
    TextView humidityTv;
    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.fragment_layout)
    FrameLayout frameLayout;


    //通过Handler 机制循环取出数据
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (jsonRootBean != null) {
                    if (jsonRootBean.getResult() != null) {
                        if (jsonRootBean.getResult().getRealtime() != null) {
                            centerTv.setText(jsonRootBean.getResult().getRealtime().getTemperature());
                            windTv.setText(jsonRootBean.getResult().getRealtime().getDirect() + " " +
                                    jsonRootBean.getResult().getRealtime().getPower());
                            humidityTv.setText("湿度 " + jsonRootBean.getResult().getRealtime().getWid());
                            if ("晴".equals(jsonRootBean.getResult().getRealtime().getInfo())) {
                                frameLayout.setBackgroundResource(R.drawable.ic_sunny);
                            }
                            if ("阴".equals(jsonRootBean.getResult().getRealtime().getInfo())) {
                                frameLayout.setBackgroundResource(R.drawable.ic_cloudy_day);
                            }
                            if (!TextUtils.isEmpty(jsonRootBean.getResult().getRealtime().getInfo()) &&
                                    jsonRootBean.getResult().getRealtime().getInfo().contains("雨")) {
                                frameLayout.setBackgroundResource(R.drawable.ic_rain);
                            }
                        }
                        if (jsonRootBean.getResult().getFuture() != null) {
                            //将数据和context通过自定义Adapter的构造方法传给Adapter
                            MyAdapter adapter = new MyAdapter(jsonRootBean.getResult().getFuture(), mActivity);
                            listView.setAdapter(adapter);
                        }

                    }

                }
                if (weatherJsonRootBean != null){
                    if (weatherJsonRootBean.getMain() != null){
                        centerTv.setText(weatherJsonRootBean.getMain().getTemp()+"");
                        if (weatherJsonRootBean.getMain().getTemp() > 20) {
                            frameLayout.setBackgroundResource(R.drawable.ic_sunny);
                        }
                        if (weatherJsonRootBean.getMain().getTemp() < 20 && weatherJsonRootBean.getMain().getTemp() > 15) {
                            frameLayout.setBackgroundResource(R.drawable.ic_rain);
                        }else {
                            frameLayout.setBackgroundResource(R.drawable.ic_cloudy_day);
                        }
                    }
                    if (weatherJsonRootBean.getWeather() != null && weatherJsonRootBean.getWeather().size() > 0){

                        windTv.setText(weatherJsonRootBean.getWeather().get(0).getMain());
                        humidityTv.setText(weatherJsonRootBean.getWeather().get(0).getDescription());
                    }
                }

            }
        }


    };
    private String city = "Seoul";
    private WeatherBean weatherBean;
    private String key = "639e250e02d5dd2b4f6647f817a3680b";
    private JsonRootBean jsonRootBean;
    private String lat = "37.57786";
    private String lng = "126.96350";
    private WeatherJsonRootBean weatherJsonRootBean;


    @Override
    protected void initView() {
        weatherBean = (WeatherBean) getIntent().getSerializableExtra(Constants.DATA);

        if (weatherBean != null) {
            city = weatherBean.getName();
            lat = weatherBean.getLat();
            lng = weatherBean.getLng();
        }
        cityTv.setText(city);
        getData(city);
    }


    private void getData(String city) {

        //1.拿到okHttpClient对象,可以设置连接超时等
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.构造Request请求对象，可以增加头addHeader等
        Request.Builder builder = new Request.Builder();


        //String url = "http://apis.juhe.cn/simpleWeather/query?city=" + city + "&key=" + key;
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lng + "&units=metric&exclude=current&appid=7ea3693e9fcbe4752c2aef5c833e8538";
        Log.e(TAG, "url: " + url);
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
                Log.e("onFailure", e.getMessage());

            }

            //请求成功时调用
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //处于子线程中，能够进行大文件下载，但是无法更新UI
                /**
                 * runOnUiThread方法切换到主线程中，或者用handler机制也可以
                 */
                final String result = response.body().string();//请求成功时返回的东西
                Log.e("onResponse", result);
                if (!TextUtils.isEmpty(result)) {
                    //jsonRootBean = new Gson().fromJson(result, WeatherJsonRootBean.class);
                    weatherJsonRootBean = new Gson().fromJson(result, WeatherJsonRootBean.class);
                    //定义消息对象
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                }

            }
        });
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_weater;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


    public class MyAdapter extends BaseAdapter {
        private List<Future> productions = null;
        private Context context = null;

        public MyAdapter(List<Future> productions, Context context) {
            super();
            this.productions = productions;
            this.context = context;
        }

        @Override
        public int getCount() {
            return productions.size();
        }

        @Override
        public Object getItem(int position) {
            return productions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //获取到ListView的子项
            convertView = LayoutInflater.from(context).inflate(R.layout.list_view_item, null);

            TextView tv_left = convertView.findViewById(R.id.left_tv);
            TextView tv_right = convertView.findViewById(R.id.right_tv);
            //待组装的每一个item的bean，从bean中取出数据
            Future future = productions.get(position);
            if (position == 0) {
                tv_left.setText("今天·" + future.getWeather());
                tv_right.setText(future.getTemperature());
            } else if (position == 1) {
                tv_left.setText("明天·" + future.getWeather());
                tv_right.setText(future.getTemperature());
            } else if (position == 2) {
                tv_left.setText("后天·" + future.getWeather());
                tv_right.setText(future.getTemperature());
            } else {
                tv_left.setText(future.getDate() + "·" + future.getWeather());
                tv_right.setText(future.getTemperature());
            }
           /* purchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "点的是第" + (position + 1) + "行", Toast.LENGTH_LONG).show();
                }
            });*/
            return convertView;
        }
    }


    public class JsonRootBean {
        private String reason;
        private Result result;
        private int error_code;

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getReason() {
            return reason;
        }

        public void setResult(Result result) {
            this.result = result;
        }

        public Result getResult() {
            return result;
        }

        public void setError_code(int error_code) {
            this.error_code = error_code;
        }

        public int getError_code() {
            return error_code;
        }
    }


    public class Result {
        private String city;
        private Realtime realtime;
        private List<Future> future;

        public void setCity(String city) {
            this.city = city;
        }

        public String getCity() {
            return city;
        }

        public void setRealtime(Realtime realtime) {
            this.realtime = realtime;
        }

        public Realtime getRealtime() {
            return realtime;
        }

        public void setFuture(List<Future> future) {
            this.future = future;
        }

        public List<Future> getFuture() {
            return future;
        }
    }

    public class Realtime {
        private String temperature;
        private String humidity;
        private String info;
        private String wid;
        private String direct;
        private String power;
        private String aqi;

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getInfo() {
            return info;
        }

        public void setWid(String wid) {
            this.wid = wid;
        }

        public String getWid() {
            return wid;
        }

        public void setDirect(String direct) {
            this.direct = direct;
        }

        public String getDirect() {
            return direct;
        }

        public void setPower(String power) {
            this.power = power;
        }

        public String getPower() {
            return power;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public String getAqi() {
            return aqi;
        }
    }

    public class Future {
        private String date;
        private String temperature;
        private String weather;
        private Wid wid;
        private String direct;

        public void setDate(String date) {
            this.date = date;
        }

        public String getDate() {
            return date;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public String getWeather() {
            return weather;
        }

        public void setWid(Wid wid) {
            this.wid = wid;
        }

        public Wid getWid() {
            return wid;
        }

        public void setDirect(String direct) {
            this.direct = direct;
        }

        public String getDirect() {
            return direct;
        }
    }

    public class Wid {


        private String day;
        private String night;

        public void setDay(String day) {
            this.day = day;
        }

        public String getDay() {
            return day;
        }

        public void setNight(String night) {
            this.night = night;
        }

        public String getNight() {
            return night;
        }

    }

}
