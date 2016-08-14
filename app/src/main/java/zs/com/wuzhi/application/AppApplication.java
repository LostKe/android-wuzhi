package zs.com.wuzhi.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.PersistentCookieStore;

import java.security.KeyStore;
import java.util.Properties;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.scheme.PlainSocketFactory;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.scheme.SchemeRegistry;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import zs.com.wuzhi.net.ApiHttpClient;
import zs.com.wuzhi.util.Constant;
import zs.com.wuzhi.util.EncryptUtil;
import zs.com.wuzhi.util.ResponseUtil;
import zs.com.wuzhi.util.WuzhiApi;

/**
 * Created by zhangshuqing on 16/7/30.
 */
public class AppApplication extends Application {

    static Context _context;
    static Resources _resource;
    boolean isLogin=false;


    @Override
    public void onCreate() {
        super.onCreate();
        _context = getApplicationContext();
        _resource = _context.getResources();
        init();
    }

    public static synchronized AppApplication context() {
        return (AppApplication) _context;
    }

    public static Resources resources() {
        return _resource;
    }

    private void init() {
        // 初始化网络请求
        AsyncHttpClient client = new AsyncHttpClient(getSchemeRegistry());
        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        client.setCookieStore(myCookieStore);
        ApiHttpClient.setHttpClient(client, this);

        initLogin();
    }

    private void initLogin() {
        String userName=getProperty(Constant.USER_NAME);
        String pwd=getProperty(Constant.PASS_WORD);

        pwd=EncryptUtil.decrypt(pwd);

        if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(pwd)){

            WuzhiApi.login(userName, pwd, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if(302==statusCode){//状态码302 重定向 表示登录成功
                        String set_cookie= ResponseUtil.getCookie(headers);
                        setProperty(Constant.SET_COOKIE,set_cookie);
                        setLoginStatu(true);
                        refreshCookie();
                    }
                }
            });

        }
    }

    /**
     * 更新cookie
     */
    public void refreshCookie() {
        ApiHttpClient.refreshCookie(this);
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }

    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }


    public String getProperty(String key) {
        String res = AppConfig.getAppConfig(this).get(key);
        return res;
    }


    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    public static SchemeRegistry getSchemeRegistry() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));
            return registry;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isLogin(){
        return isLogin;
    }

    public void setLoginStatu(boolean b){
        this.isLogin=b;
    }


    public void logout(){
        setProperty(Constant.SET_COOKIE,"");
        setLoginStatu(false);
        setProperty(Constant.PASS_WORD,"");
    }

}
