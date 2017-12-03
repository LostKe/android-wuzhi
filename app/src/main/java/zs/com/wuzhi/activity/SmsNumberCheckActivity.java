package zs.com.wuzhi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.AsyncHttpResponseHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import zs.com.wuzhi.Helper.PermissionHelper;
import zs.com.wuzhi.R;
import zs.com.wuzhi.util.Constant;
import zs.com.wuzhi.util.WuzhiApi;

/**
 * Created by zhangshuqing on 2017/12/3.
 */
public class SmsNumberCheckActivity extends BaseToolBarActivity implements View.OnClickListener {

    private static final String TAG = "SmsNumberCheckActivity";
    @BindView(R.id.et_sms_check_code)
    AppCompatEditText sms_check_code;


    @BindView(R.id.et_sms_msg)
    EditText et_sms_msg;


    @BindView(R.id.btn_sms_check)
    Button btn_sms_check;

    KProgressHUD hud;

    String smsMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_submit);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        btn_sms_check.setOnClickListener(this);
    }


    @Override
    boolean isBackHomeVisible() {
        return true;
    }

    @Override
    String getToolBarTitle() {
        return "获取联系人";
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
            case R.id.btn_sms_check:


                String code_text = sms_check_code.getText().toString().trim();
                if (TextUtils.isEmpty(code_text)) {
                    Toast.makeText(this, "请输入CODE", Toast.LENGTH_SHORT).show();
                    return;
                }

                smsMsg=et_sms_msg.getText().toString().trim();
                if(TextUtils.isEmpty(smsMsg)){
                    Toast.makeText(this, "请输入短信内容", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(PermissionHelper.checkPermission(this,PermissionHelper.SEND_SMS)){
                    hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Please wait").setCancellable(true);
                    hud.show();
                    Log.i(TAG, "CODE:" + code_text);
                    //从服务器获取用户信息
                    WuzhiApi.getUserPhone(code_text.toLowerCase(), handler);
                }


        }
    }


    AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            hud.dismiss();
            Object objMsg = JSON.parse(responseBody);
            JSONArray array = JSONArray.parseArray(String.valueOf(objMsg));
            if (array.size() == 0) {
                Toast.makeText(SmsNumberCheckActivity.this, "没有获取到联系人，请确认CODE", Toast.LENGTH_SHORT).show();
                return;
            } else {
                //将数据传递
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("USER_PHONE", String.valueOf(objMsg));
                bundle.putString("SMS_MSG", smsMsg);
                intent.putExtra(Constant.SETTING_BUNDLE, bundle);
                intent.setClass(getApplicationContext(), SendSmsActivity.class);
                startActivity(intent);
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            hud.dismiss();

        }
    };
}
