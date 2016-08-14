package zs.com.wuzhi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.AsyncHttpResponseHandler;

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
public class LoginActivity extends BaseToolBarActivity implements View.OnClickListener {

    @BindView(R.id.et_username)
    AppCompatEditText userName;

    @BindView(R.id.et_password)
    AppCompatEditText password;

    @BindView(R.id.btn_login)
    Button bt_login;

    KProgressHUD hud;

    Intent mIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
        mIntent=getIntent();

    }

    private void init() {
        AppApplication application=AppApplication.context();
        userName.setText(application.getProperty(Constant.USER_NAME));
        password.setText(application.getProperty(Constant.PASS_WORD));
        bt_login.setOnClickListener(this);
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
                String userName_text=userName.getText().toString().trim();
                String pwd= password.getText().toString().trim();
                AppApplication application=AppApplication.context();
                application.setProperty(Constant.USER_NAME,userName_text);
                application.setProperty(Constant.PASS_WORD, EncryptUtil.encrypt(pwd));
                WuzhiApi.login(userName_text, pwd, handler);
                break;
        }
    }

    AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            hud.dismiss();
            if(200==statusCode){
                String content = new String(responseBody);
                String loginMsg= ResponseUtil.getLoginMessage(content);
                Toast.makeText(getApplicationContext(),loginMsg,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            hud.dismiss();
            if(302==statusCode){//状态码302 重定向 表示登录成功
                String set_cookie=ResponseUtil.getCookie(headers);
                AppApplication application=AppApplication.context();
                application.setProperty(Constant.SET_COOKIE,set_cookie);
                application.setLoginStatu(true);
                application.refreshCookie();
                if(mIntent!=null && mIntent.getBundleExtra(Constant.SETTING_BUNDLE)!=null){
                    Bundle bundle=mIntent.getBundleExtra(Constant.SETTING_BUNDLE);
                    String next_activity_name=bundle.getString(Constant.NEXT_ACTIVITY);
                    if(!TextUtils.isEmpty(next_activity_name)){
                        Intent next=new Intent();
                        next.setClassName(getApplicationContext(),next_activity_name);
                        startActivity(next);
                    }
                }

                finish();
            }else{
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



}
