package zs.com.wuzhi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.AsyncHttpResponseHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import zs.com.wuzhi.R;
import zs.com.wuzhi.application.AppApplication;
import zs.com.wuzhi.bean.UserInfo;
import zs.com.wuzhi.util.Constant;
import zs.com.wuzhi.util.ResponseUtil;
import zs.com.wuzhi.util.WuzhiApi;

/**
 * Created by zhangshuqing on 16/7/24.
 */
public class SettingActivity extends BaseToolBarActivity implements View.OnClickListener{

    @BindView(R.id.ll_setting_pic)
    LinearLayout ll_setting_pic;
    @BindView(R.id.ll_setting_nickname)
    LinearLayout ll_setting_nickname;
    @BindView(R.id.ll_setting_sigin)
    LinearLayout ll_setting_sigin;
    @BindView(R.id.ll_setting_logout)
    LinearLayout ll_setting_logout;

    @BindView(R.id.ll_setting_primary)
    LinearLayout ll_setting_primary;

    @BindView(R.id.iv_setting)
    ImageView iv_setting;

    @BindView(R.id.tv_setting_nickname)
    TextView tv_setting_nickname;

    @BindView(R.id.tv_setting_signature)
    TextView tv_setting_signature;



    public static  final int ACTION_PIC=1;
    public static final int ACTION_NICKNAME=2;
    public static final int ACTION_SIGIN=3;

    KProgressHUD hud;

    String selfImgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        hud=KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(true);

        init();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        initUserInfo();
    }

    @Override
    boolean isBackHomeVisible() {
        return true;
    }


    @Override
    String getToolBarTitle() {
        return "个人设置";
    }

    @Override
    public OnBackHomeClicklistener getOnBackHomeListener() {
        return new OnBackHomeClicklistener() {
            @Override
            public void backHomeClick() {
                finish();
            }
        };
    }


    private void init() {
        ll_setting_logout.setOnClickListener(this);
        ll_setting_nickname.setOnClickListener(this);
        ll_setting_sigin.setOnClickListener(this);
        ll_setting_pic.setOnClickListener(this);
        ll_setting_primary.setOnClickListener(this);
        initUserInfo();
    }

    private void initUserInfo(){
        hud.show();
        WuzhiApi.gettAccountInfo(handler);
    }

    AsyncHttpResponseHandler handler=new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String content=new String(responseBody);
            //首先获取主页信息
            UserInfo userInfo=ResponseUtil.getUserInfo(content);
            tv_setting_nickname.setText(userInfo.getNickName());
            tv_setting_signature.setText(userInfo.getSignature());
            WuzhiApi.get(Constant.MAIN+userInfo.getMineUrl(), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String mineContent=new String(responseBody);
                    selfImgUrl= ResponseUtil.getImgUrl(mineContent);
                    if (!TextUtils.isEmpty(selfImgUrl)) {
                        Glide.with(getApplicationContext()).load(selfImgUrl).into(iv_setting);
                    }
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
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            hud.dismiss();
            Toast.makeText(getApplicationContext(),"获取信息失败，请稍后再试！",Toast.LENGTH_SHORT).show();
        }
    };




    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        switch (v.getId()){
            case R.id.ll_setting_pic:
                //显示大头像，可以上传头像
                bundle.putString(Constant.SELF_IMG_URL,selfImgUrl);
                intent.setClass(this,SelfPhotoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                    break;
            case R.id.ll_setting_nickname:
                //修改昵称
                bundle.putInt(Constant.ACTION_TYPE,ACTION_NICKNAME);
                bundle.putString(Constant.TITLE,"昵称");
                bundle.putString(Constant.NICKNAME,tv_setting_nickname.getText().toString());
                bundle.putString(Constant.SIGNATURE,tv_setting_signature.getText().toString());
                intent.setClass(this,CommonSubmitActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.ll_setting_sigin:
                bundle.putInt(Constant.ACTION_TYPE,ACTION_SIGIN);
                //修改昵称
                bundle.putString(Constant.TITLE,"签名");
                bundle.putString(Constant.NICKNAME,tv_setting_nickname.getText().toString());
                bundle.putString(Constant.SIGNATURE,tv_setting_signature.getText().toString());
                intent.setClass(this,CommonSubmitActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                //修改 签名
                break;
            case R.id.ll_setting_logout:
                //清除cookie ，
                AppApplication.context().logout();
                //退出登录 回到首页
                intent.setClass(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.ll_setting_primary:
                intent.setClass(this,PrimaryActivity.class);
                bundle.putInt(Constant.ACTIVITY_INTENT_PRIMARY,Constant.ACTIVITY_INTENT_PRIMARY_DIARY);
                bundle.putString(Constant.TOOL_BAR_TITLE,"隐私");
                intent.putExtras(bundle);
                startActivity(intent);
                break;

        }



    }

}
