package zs.com.wuzhi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.AsyncHttpResponseHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import zs.com.wuzhi.R;
import zs.com.wuzhi.util.Constant;
import zs.com.wuzhi.util.WuzhiApi;

/**
 * Created by zhangshuqing on 16/7/30.
 */
public class CommonSubmitActivity extends BaseToolBarActivity {

    private static final int MAX_TEXT_LENGTH=20;

    String title;

    String nickname;
    int type;
    String signature;

    KProgressHUD hud;

    @BindView(R.id.et_commonsubmit)
    EditText et_common_submit;

    @BindView(R.id.tv_clear)
    TextView tv_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        ButterKnife.bind(this);
        hud=KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("正在修改...").setCancellable(false);
        init();

    }

    private void init() {
        Intent intent= getIntent();
        Bundle bundle=intent.getExtras();
        nickname=bundle.getString(Constant.NICKNAME);
        signature=bundle.getString(Constant.SIGNATURE);
        type=bundle.getInt(Constant.ACTION_TYPE);

        switch (type){
            case SettingActivity.ACTION_NICKNAME:
                et_common_submit.setText(nickname);
                //设置光标在文字的末尾处
                et_common_submit.setSelection(nickname.length());
                break;
            case SettingActivity.ACTION_SIGIN:
                et_common_submit.setText(signature);
                //设置光标在文字的末尾处
                et_common_submit.setSelection(signature.length());
                break;
        }
        tv_clear.setText(String.valueOf(MAX_TEXT_LENGTH-(et_common_submit.getText().length())));

        et_common_submit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //动态设置还有多少字的提示
                tv_clear.setText(MAX_TEXT_LENGTH-s.length()+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    @Override
    boolean isBackHomeVisible() {
        return true;
    }

    @Override
    String getToolBarTitle() {
        Intent intent= getIntent();
        Bundle bundle=intent.getExtras();
        title=bundle.getString(Constant.TITLE);
        return title;
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
    boolean needCompleteButton() {
       return true;
    }

    @Override
    OnMenuActionClickListener getOnMenuActionClickListener() {
        return new OnMenuActionClickListener() {
            @Override
            public void onClick() {
                hud.show();
                switch (type){
                    case SettingActivity.ACTION_NICKNAME:
                        nickname=et_common_submit.getText().toString().trim();
                        break;
                    case SettingActivity.ACTION_SIGIN:
                        signature=et_common_submit.getText().toString().trim();
                        break;
                }

                WuzhiApi.setAccountProfile(nickname,signature,handler);
            }
        };
    }


    AsyncHttpResponseHandler handler=new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            hud.dismiss();
            finish();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            hud.dismiss();
            Toast.makeText(getApplicationContext(),"设置失败，请稍后再试！",Toast.LENGTH_SHORT).show();
        }
    };
}
