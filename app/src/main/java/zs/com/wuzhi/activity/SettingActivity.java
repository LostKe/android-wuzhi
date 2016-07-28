package zs.com.wuzhi.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.wuzhi.R;

/**
 * Created by zhangshuqing on 16/7/24.
 */
public class SettingActivity extends BaseToolBarActivity implements View.OnClickListener,BaseToolBarActivity.OnBackHomeClicklistener{

    @BindView(R.id.ll_setting_pic)
    LinearLayout ll_setting_pic;
    @BindView(R.id.ll_setting_nickname)
    LinearLayout ll_setting_nickname;
    @BindView(R.id.ll_setting_sigin)
    LinearLayout ll_setting_sigin;
    @BindView(R.id.ll_setting_logout)
    LinearLayout ll_setting_logout;


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
        return this;
    }


    private void init() {
        ll_setting_logout.setOnClickListener(this);
        ll_setting_nickname.setOnClickListener(this);
        ll_setting_sigin.setOnClickListener(this);
        ll_setting_pic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_setting_pic:
                //显示大头像，可以上传头像
                    break;
            case R.id.ll_setting_nickname:
                //修改昵称
                break;
            case R.id.ll_setting_sigin:
                //修改 签名
                break;
            case R.id.ll_setting_logout:
                //退出登录
                break;

        }
    }

    @Override
    public void backHomeClick() {
        finish();
    }
}
