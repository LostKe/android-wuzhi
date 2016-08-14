package zs.com.wuzhi.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.AsyncHttpResponseHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import zs.com.wuzhi.R;
import zs.com.wuzhi.util.Constant;
import zs.com.wuzhi.util.ResponseUtil;
import zs.com.wuzhi.util.WuzhiApi;
import zs.com.wuzhi.widget.SwitchView;

/**
 * Created by zhangshuqing on 16/7/24.
 */
public class PrimaryActivity extends BaseToolBarActivity implements SwitchView.OnStateChangedListener{

    @BindView(R.id.primary_switch)
    SwitchView switchView;

    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);
        ButterKnife.bind(this);
        hud=KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(true);
        init();
    }

    private void init() {
       switchView.setOnStateChangedListener(this);
        initSwitch();
    }

    private void initSwitch(){
        hud.show();
        WuzhiApi.gettPrivacy(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String content=new String(responseBody);
                int statu=ResponseUtil.getPrivacyStatu(content);
                switchView.setOpened(statu==0?true:false);
                hud.dismiss();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                hud.dismiss();
                Toast.makeText(getApplicationContext(),"获取信息失败，请稍后再试！",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    boolean isBackHomeVisible() {
        return true;
    }


    @Override
    String getToolBarTitle() {
        return "隐私";
    }

    @Override
    public OnBackHomeClicklistener getOnBackHomeListener() {
        OnBackHomeClicklistener onBackHomeClicklistener = new OnBackHomeClicklistener() {
            @Override
            public void backHomeClick() {
                finish();
            }
        };
        return onBackHomeClicklistener;
    }

    @Override
    public void toggleToOn() {
        hud.show();
        WuzhiApi.settingPrivacy(Constant.ACCOUNT_PRIVACY_TYPE_ALL,handler);
    }

    @Override
    public void toggleToOff() {
        hud.show();
        WuzhiApi.settingPrivacy(Constant.ACCOUNT_PRIVACY_TYPE_SELF,handler);
    }

    AsyncHttpResponseHandler handler=new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            hud.dismiss();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            hud.dismiss();
            Toast.makeText(getApplicationContext(),"设置失败，请稍后再试！",Toast.LENGTH_SHORT).show();
        }
    };
}
