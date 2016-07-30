package zs.com.wuzhi.application;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by zhangshuqing on 16/7/30.
 */
public class Wuzhi extends Application {

    OkHttpClient okHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        okHttpClient=new OkHttpClient().newBuilder()
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L,TimeUnit.MILLISECONDS).build();
    }
}
