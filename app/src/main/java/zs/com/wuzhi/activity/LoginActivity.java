package zs.com.wuzhi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.geetest.gt3unbindsdk.Bind.GT3GeetestBind;
import com.geetest.gt3unbindsdk.Bind.GT3GeetestUtilsBind;
import com.geetest.gt3unbindsdk.Bind.GT3Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import zs.com.wuzhi.R;
import zs.com.wuzhi.application.AppApplication;
import zs.com.wuzhi.util.Constant;
import zs.com.wuzhi.util.EncryptUtil;
import zs.com.wuzhi.util.ResponseUtil;
import zs.com.wuzhi.util.TDevice;
import zs.com.wuzhi.util.WuzhiApi;

/**
 * Created by zhangshuqing on 16/7/25.
 */
public class LoginActivity extends BaseToolBarActivity implements View.OnClickListener, GT3GeetestUtilsBind.GT3Listener {
    //TODO 需要去查找这两条url
    private static final String captchaURL = "http://www.geetest.com/demo/gt/register-slide";
    // 设置二次验证的URL，需替换成自己的服务器URL
    private static final String validateURL = "http://www.geetest.com/demo/gt/validate-slide";

    @BindView(R.id.et_username)
    AppCompatEditText userName;

    @BindView(R.id.et_password)
    AppCompatEditText password;

    @BindView(R.id.btn_login)
    Button bt_login;

    KProgressHUD hud;

    Intent mIntent;

    private GT3GeetestUtilsBind gt3GeetestUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
        gt3GeetestUtils.setGtListener(this);
        mIntent = getIntent();
        //TODO 需要登录
//        bt_login.setOnClickListener(this);

    }

    private void init() {
        /**
         * 初始化
         * 务必放在onCreate方法里面执行
         */
        gt3GeetestUtils = new GT3GeetestUtilsBind(LoginActivity.this);
        gt3GeetestUtils.gtDologo(captchaURL, validateURL, null);//加载验证码之前判断有没有logo

        /**
         * 点击调起验证
         */
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gt3GeetestUtils.getGeetest(LoginActivity.this);
                //设置是否可以点击屏幕边缘关闭验证码
                gt3GeetestUtils.setDialogTouch(true);

            }
        });



    }

    @Override
    boolean isBackHomeVisible() {
        return true;
    }


    @Override
    String getToolBarTitle() {
        return "登录";
    }

    @Override
    OnBackHomeClicklistener getOnBackHomeListener() {
        return new OnBackHomeClicklistener() {
            @Override
            public void backHomeClick() {
                finish();
            }
        };
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (!preparedLogin()) {
                    return;
                }
                hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Please wait").setCancellable(false);
                hud.show();
                String userName_text = userName.getText().toString().trim();
                String pwd = password.getText().toString().trim();
                AppApplication application = AppApplication.context();
                application.setProperty(Constant.USER_NAME, userName_text);
                application.setProperty(Constant.PASS_WORD, EncryptUtil.encrypt(pwd));
                //TODO 这里需要设置 验证相关的参数
                WuzhiApi.login(userName_text, pwd, null, null, handler);
                break;
        }
    }

    AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            hud.dismiss();
            if (200 == statusCode) {
                String content = new String(responseBody);
                String loginMsg = ResponseUtil.getLoginMessage(content);
                Toast.makeText(getApplicationContext(), loginMsg, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            hud.dismiss();
            if (302 == statusCode) {//状态码302 重定向 表示登录成功
                String set_cookie = ResponseUtil.getCookie(headers);
                AppApplication application = AppApplication.context();
                application.setProperty(Constant.SET_COOKIE, set_cookie);
                application.setLoginStatu(true);
                application.refreshCookie();
                if (mIntent != null && mIntent.getBundleExtra(Constant.SETTING_BUNDLE) != null) {
                    Bundle bundle = mIntent.getBundleExtra(Constant.SETTING_BUNDLE);
                    String next_activity_name = bundle.getString(Constant.NEXT_ACTIVITY);
                    if (!TextUtils.isEmpty(next_activity_name)) {
                        Intent next = new Intent();
                        next.setClassName(getApplicationContext(), next_activity_name);
                        startActivity(next);
                    }
                }

                finish();
            } else {
                Toast.makeText(getApplicationContext(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private boolean preparedLogin() {

        if (!TDevice.hasInternet()) {
            Toast.makeText(this, "当前没有可用的网络", Toast.LENGTH_SHORT).show();
            return false;
        }

        String userName_text = userName.getText().toString();
        String password_text = password.getText().toString();
        if (userName_text.length() == 0) {
            userName.setError("请输入邮箱");
            return false;
        }
        if (password_text.length() == 0) {
            password.setError("请输入密码");
            return false;
        }
        return true;
    }


    @Override
    public void gt3CloseDialog() {

    }

    @Override
    public void gt3CancelDialog() {

    }

    @Override
    public void gt3DialogReady() {

    }

    @Override
    public void gt3FirstGo() {

    }

    @Override
    public void gt3FirstResult(JSONObject jsonObject) {

    }

    @Override
    public Map<String, String> gt3SecondResult() {
        return null;
    }

    @Override
    public void gt3GetDialogResult(String s) {

    }

    /**
     * 自定义二次验证，当gtSetIsCustom为ture时执行这里面的代码
     */
    @Override
    public void gt3GetDialogResult(boolean b, String result) {
        if (b) {
            //利用异步进行解析这result进行二次验证，结果成功后调用gt3GeetestUtils.gt3TestFinish()方法调用成功后的动画，然后在gt3DialogSuccess执行成功之后的结果
            try {
                JSONObject  res_json = new JSONObject(result);
                Map<String, String> validateParams = new HashMap<>();

                validateParams.put("geetest_challenge", res_json.getString("geetest_challenge"));

                validateParams.put("geetest_validate", res_json.getString("geetest_validate"));

                validateParams.put("geetest_seccode", res_json.getString("geetest_seccode"));


                AppApplication application = AppApplication.context();
                userName.setText(application.getProperty(Constant.USER_NAME));
                bt_login.setOnClickListener(this);



            } catch (JSONException e) {
                e.printStackTrace();
            }
            //  二次验证成功调用
            gt3GeetestUtils.gt3TestFinish();


        } else {
            //  二次验证失败调用
            gt3GeetestUtils.gt3TestClose();
        }
    }

    @Override
    public void gt3DialogOnError(String s) {
        gt3GeetestUtils.cancelAllTask();
    }

    @Override
    public void gt3DialogSuccessResult(String result) {
        if (!TextUtils.isEmpty(result)) {
            try {
                JSONObject jobj = new JSONObject(result);
                String sta = jobj.getString("status");
                if ("success".equals(sta)) {
                    gt3GeetestUtils.gt3TestFinish();
                } else {
                    gt3GeetestUtils.gt3TestClose();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            gt3GeetestUtils.gt3TestClose();
        }
        Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();
    }

    @Override
    public void gt3AjaxResult(String s) {

    }

    @Override
    public Map<String, String> captchaApi1() {
        return null;
    }

    @Override
    public boolean gtSetIsCustom() {
        return true;
    }

    @Override
    public void gereg_21() {

    }

    @Override
    public void gt3GeetestStatisticsJson(JSONObject jsonObject) {

    }
}
