package zs.com.wuzhi.util;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

import zs.com.wuzhi.application.AppApplication;
import zs.com.wuzhi.net.ApiHttpClient;

/**
 * Created by zhangshuqing on 16/8/13.
 */
public class WuzhiApi {

    /**
     * 登陆
     *
     * @param username
     * @param password
     * @param handler
     */
    public static void login(String username, String password, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put(Constant.LOGIN_EMAIL, username);
        params.put(Constant.LOGIN_PASSOWRD, password);
        ApiHttpClient.post(Constant.LOGIN, params, handler);

    }


    public static void getDiaryHtml(AsyncHttpResponseHandler handler) {
        ApiHttpClient.get(Constant.NOTE_ADD, handler);
    }


    /**
     * 发表日记
     * @param content
     * @param handler
     */
    public static void postDiary(String content,String crsyToken, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put(Constant.NOTE_ADD_CONTENT, content);
        params.put(Constant.NOTE_CRSF_TOKEN, crsyToken);
        ApiHttpClient.post(Constant.NOTE_ADD, params, handler);
    }

    /**
     * 设置隐私
     * @param type
     * @param handler
     */
    public static void settingPrivacy(String type, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put(Constant.ACCOUNT_PRIVACY_TYPE, type);
        ApiHttpClient.post(Constant.ACCOUNT_PRIVACY, params, handler);
    }

    /**
     * 设置昵称和签名
     * @param name 昵称
     * @param signature 签名
     * @param handler
     */
    public static void setAccountProfile(String name,String signature, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put(Constant.ACCOUNT_PROFILE_NAME, name);
        params.put(Constant.ACCOUNT_PROFILE_SIGNATURE, signature);
        ApiHttpClient.post(Constant.ACCOUNT_PROFILE, params, handler);
    }

    /**
     * 获取个人信息
     *
     * Response 中附带了个人信息
     *
     * @param handler
     */
    public static void gettAccountInfo( AsyncHttpResponseHandler handler) {
        ApiHttpClient.get(Constant.ACCOUNT_PROFILE, handler);
    }


    /**
     * 获取个人信息
     *
     * Response 中附带了个人信息
     *
     * @param handler
     */
    public static void gettPrivacy( AsyncHttpResponseHandler handler) {
        ApiHttpClient.get(Constant.ACCOUNT_PRIVACY, handler);
    }


    public static void updateAvatar(AppApplication context, File file, AsyncHttpResponseHandler handler) throws FileNotFoundException {
        RequestParams params = new RequestParams();
        params.put(Constant.AVATAR_NAME, file);
        ApiHttpClient.uploadClient.post(Constant.AVATAR,params,handler);
    }



    public static void get(String url,AsyncHttpResponseHandler handler){
        ApiHttpClient.get(url, handler);
    }


}
