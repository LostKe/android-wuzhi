package zs.com.wuzhi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.wuzhi.R;
import zs.com.wuzhi.util.Constant;

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
    public static  final int ACTION_PIC=1;
    public static final int ACTION_NICKNAME=2;
    public static final int ACTION_SIGIN=3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        init();
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
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        switch (v.getId()){
            case R.id.ll_setting_pic:
                //显示大头像，可以上传头像
                    break;
            case R.id.ll_setting_nickname:
                //修改昵称
                bundle.putInt(Constant.ACTION_TYPE,ACTION_NICKNAME);
                bundle.putString(Constant.TITLE,"昵称");
                bundle.putString(Constant.CONTENT,"谎");
                intent.setClass(this,CommonSubmitActivity.class);
                break;
            case R.id.ll_setting_sigin:
                bundle.putInt(Constant.ACTION_TYPE,ACTION_SIGIN);
                bundle.putString(Constant.TITLE,"签名");
                bundle.putString(Constant.CONTENT,"时间是贼，偷走一切");
                intent.setClass(this,CommonSubmitActivity.class);
                //修改 签名
                break;
            case R.id.ll_setting_logout:
                //退出登录 回到首页
                intent.setClass(this,MainActivity.class);
                break;

        }
        intent.putExtras(bundle);
        startActivity(intent);

    }

}
