package zs.com.wuzhi.net;

import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Locale;

import zs.com.wuzhi.application.AppApplication;
import zs.com.wuzhi.util.Constant;

/**
 * Created by zhangshuqing on 16/8/13.
 */

public class ApiHttpClient {

    public static AsyncHttpClient client;

    public static AsyncHttpClient uploadClient;



    public ApiHttpClient() {
    }

    public static AsyncHttpClient getHttpClient() {
        return client;
    }

    public static void setHttpClient(AsyncHttpClient c,AsyncHttpClient cu, AppApplication appContext) {
        client = c;
        uploadClient=cu;
        client.addHeader("Accept-Language", Locale.getDefault().toString());
        client.addHeader("Connection", "Keep-Alive");
        client.addHeader("content-type", "application/x-www-form-urlencoded");
        if (!TextUtils.isEmpty(getCookie(appContext))) {
            client.addHeader(Constant.COOKIE, String.format(Constant.cookie_format, getCookie(appContext)));
            uploadClient.addHeader(Constant.COOKIE, String.format(Constant.cookie_format, getCookie(appContext)));
        }

    }


    public static void get(String url, AsyncHttpResponseHandler handler) {
        client.get(url, handler);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler handler) {
        client.get(url, params, handler);
    }


    public static void post(String url, AsyncHttpResponseHandler handler) {
        client.post(url, handler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler handler) {
        client.post(url, params, handler);

    }


    public static void refreshCookie(AppApplication appContext) {

        client.addHeader(Constant.COOKIE, String.format(Constant.cookie_format, getCookie(appContext)));

        uploadClient.addHeader(Constant.COOKIE, String.format(Constant.cookie_format, getCookie(appContext)));
    }



    public static String getCookie(AppApplication appContext) {
        return appContext.getProperty(Constant.SET_COOKIE);
    }



}
