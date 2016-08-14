package zs.com.wuzhi.fragment;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.wuzhi.R;
import zs.com.wuzhi.activity.LoginActivity;
import zs.com.wuzhi.activity.PrimaryActivity;
import zs.com.wuzhi.activity.SettingActivity;
import zs.com.wuzhi.application.AppApplication;
import zs.com.wuzhi.util.Constant;

/**
 * Created by zhangshuqing on 16/7/19.
 */
public class FragmentMe extends Fragment implements View.OnClickListener {

    Application app;

    @BindView(R.id.my_primary)
    LinearLayout mPrimary;

    @BindView(R.id.my_setting_ll)
    LinearLayout mSetting;

    @BindView(R.id.my_diary_ll)
    LinearLayout mDiary;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mPrimary.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mDiary.setOnClickListener(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        //检查登录状态，未登录跳转到登录界面
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.my_primary:
                checkLogin(intent, PrimaryActivity.class);
                break;
            case R.id.my_setting_ll:
                checkLogin(intent, SettingActivity.class);
                break;
            case R.id.my_diary_ll:

                break;

        }
        startActivity(intent);
    }


    private void checkLogin(Intent intent, Class clz) {
        if (!AppApplication.context().isLogin()) {
            //检查是否已经登录，未登录跳转到登录界面
            Bundle bundle = new Bundle();
            intent.setClass(getContext(), LoginActivity.class);
            bundle.putString(Constant.NEXT_ACTIVITY, clz.getName());
            intent.putExtra(Constant.SETTING_BUNDLE,bundle);
        } else {
            intent.setClass(getContext(), clz);
        }

    }


}
