package zs.com.wuzhi.fragment;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kaopiz.kprogresshud.KProgressHUD;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.wuzhi.R;
import zs.com.wuzhi.activity.FollowActivity;
import zs.com.wuzhi.activity.GestureVerifyActivity;
import zs.com.wuzhi.activity.LoginActivity;
import zs.com.wuzhi.activity.MyDiaryActivity;
import zs.com.wuzhi.activity.PrimaryActivity;
import zs.com.wuzhi.activity.SettingActivity;
import zs.com.wuzhi.activity.SmsNumberCheckActivity;
import zs.com.wuzhi.application.AppApplication;
import zs.com.wuzhi.db.DBHelper;
import zs.com.wuzhi.util.Constant;
import zs.com.wuzhi.util.EncryptUtil;

/**
 * Created by zhangshuqing on 16/7/19.
 */
public class FragmentMe extends Fragment implements View.OnClickListener {

    Application app;


    @BindView(R.id.my_setting_ll)
    LinearLayout mSetting;

    @BindView(R.id.my_diary_ll)
    LinearLayout mDiary;

    @BindView(R.id.my_fllow)
    LinearLayout mMyFollow;

    @BindView(R.id.my_security)
    LinearLayout mMySercurity;

    @BindView(R.id.sms_send)
    LinearLayout smsSend;

    KProgressHUD hud;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mSetting.setOnClickListener(this);
        mDiary.setOnClickListener(this);
        mMyFollow.setOnClickListener(this);
        mMySercurity.setOnClickListener(this);
        smsSend.setOnClickListener(this);
        hud = KProgressHUD.create(getContext()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(true);
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
            case R.id.my_setting_ll:
                checkLogin(intent, SettingActivity.class);
                break;
            case R.id.my_diary_ll:
                if(AppApplication.context().isLogin()){
                    //已经是登录状态 去校验手势
                    DBHelper dbHelper=new DBHelper(getContext());

                    Handler handler=new Handler();
                    final String gestureKey= dbHelper.findGestureKey();

                    if(!TextUtils.isEmpty(gestureKey)){
                        hud.show();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Intent gIntent=new Intent();
                                //去校验手势
                                gIntent.setClass(getContext(), GestureVerifyActivity.class);
                                //传入手势密码
                                gIntent.putExtra(Constant.GESTURE_KEY, EncryptUtil.decrypt(gestureKey));
                                startActivityForResult(gIntent, Constant.REQUESET_CODE_VERIFY_GESTURE);
                                hud.dismiss();

                            }
                        });
                        return;

                    }
                }

                checkLogin(intent, MyDiaryActivity.class);
                break;
            case R.id.my_fllow:
                checkLogin(intent, FollowActivity.class);
                break;
            case R.id.my_security:
                Bundle bundle=new Bundle();
                bundle.putInt(Constant.ACTIVITY_INTENT_PRIMARY,Constant.ACTIVITY_INTENT_PRIMARY_GESTURE);
                bundle.putString(Constant.TOOL_BAR_TITLE,"安全");
                intent.putExtras(bundle);
                checkLogin(intent, PrimaryActivity.class);
                break;
            case R.id.sms_send:
                intent.setClass(getContext(),SmsNumberCheckActivity.class);


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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constant.REQUESET_CODE_VERIFY_GESTURE:
                if(Activity.RESULT_OK==resultCode){
                    //校验通过
                    Intent intent=new Intent();
                    intent.setClass(getContext(),MyDiaryActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }
}
