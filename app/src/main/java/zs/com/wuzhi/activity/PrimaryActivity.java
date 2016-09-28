package zs.com.wuzhi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.AsyncHttpResponseHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import zs.com.wuzhi.R;
import zs.com.wuzhi.db.DBHelper;
import zs.com.wuzhi.util.Constant;
import zs.com.wuzhi.util.EncryptUtil;
import zs.com.wuzhi.util.ResponseUtil;
import zs.com.wuzhi.util.WuzhiApi;
import zs.com.wuzhi.widget.SwitchView;

/**
 * 隐私/安全
 * Created by zhangshuqing on 16/7/24.
 */
public class PrimaryActivity extends BaseToolBarActivity implements SwitchView.OnStateChangedListener, View.OnClickListener {

    @BindView(R.id.ll_primary_diary)
    LinearLayout ll_primary_diary;
    @BindView(R.id.primary_diary_switch)
    SwitchView primary_diary_switch;

    @BindView(R.id.ll_primary_gesture_switch)
    LinearLayout ll_primary_gesture_switch;
    @BindView(R.id.primary_gesture_switch)
    SwitchView primary_gesture_switch;

    @BindView(R.id.ll_primary_getsture_edit)
    LinearLayout ll_primary_getsture_edit;
    @BindView(R.id.ll_primary_getsture_edit_parent)
    LinearLayout ll_primary_getsture_edit_parent;

    KProgressHUD hud;
    DBHelper dbHelper;
    String toolbarTitile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper=new DBHelper(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null){
            toolbarTitile = bundle.getString(Constant.TOOL_BAR_TITLE);
        }
        setContentView(R.layout.activity_primary);
        ButterKnife.bind(this);
        hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(true);
        primary_diary_switch.setOnStateChangedListener(this);
        primary_gesture_switch.setOnStateChangedListener(this);
        ll_primary_getsture_edit.setOnClickListener(this);
        init(bundle);
    }

    private void init(Bundle bundle) {
        int action=0;
        if(bundle!=null){
            action = bundle.getInt(Constant.ACTIVITY_INTENT_PRIMARY);
        }

        switch (action) {
            case Constant.ACTIVITY_INTENT_PRIMARY_DIARY:
                ll_primary_gesture_switch.setVisibility(View.GONE);
                ll_primary_getsture_edit_parent.setVisibility(View.GONE);
                initSwitch();
                break;
           case Constant.ACTIVITY_INTENT_PRIMARY_GESTURE:
            default:
                ll_primary_diary.setVisibility(View.GONE);
                Handler handler=new Handler();
                hud.show();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String gestureKey=dbHelper.findGestureKey();

                        if(TextUtils.isEmpty(gestureKey)){
                            gestureSwitch(false);
                        }else{
                            gestureSwitch(true);
                        }
                        hud.dismiss();
                    }
                });
                break;
        }


    }


    private void initSwitch() {
        hud.show();
        WuzhiApi.gettPrivacy(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String content = new String(responseBody);
                int statu = ResponseUtil.getPrivacyStatu(content);
                primary_diary_switch.setOpened(statu == 0 ? true : false);
                hud.dismiss();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                hud.dismiss();
                Toast.makeText(getApplicationContext(), "获取信息失败，请稍后再试！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    boolean isBackHomeVisible() {
        return true;
    }


    @Override
    String getToolBarTitle() {
        return TextUtils.isEmpty(toolbarTitile) ? "安全" : toolbarTitile;
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
    public void toggleToOn(View view) {

        switch (view.getId()) {
            case R.id.primary_diary_switch:
                hud.show();
                WuzhiApi.settingPrivacy(Constant.ACCOUNT_PRIVACY_TYPE_ALL, handler);
                break;
            case R.id.primary_gesture_switch:
                hud.show();
                //进入设置手势密码界面
                Handler post_handler=new Handler();
                post_handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.setClass(PrimaryActivity.this, GestureEditActivity.class);
                        startActivityForResult(intent, Constant.REQUESET_CODE_EDIT_GESTURE);
                        hud.dismiss();
                    }
                });
                break;

        }


    }

    @Override
    public void toggleToOff(View view) {

        switch (view.getId()) {
            case R.id.primary_diary_switch:
                hud.show();
                WuzhiApi.settingPrivacy(Constant.ACCOUNT_PRIVACY_TYPE_SELF, handler);
                break;
            case R.id.primary_gesture_switch:
                //关闭手势校验 需要校验密码
                hud.show();
                Handler handler=new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.setClass(PrimaryActivity.this, GestureVerifyActivity.class);
                        //传入手势密码
                        String gestureKey=dbHelper.findGestureKey();
                        intent.putExtra(Constant.GESTURE_KEY, EncryptUtil.decrypt(gestureKey));
                        startActivityForResult(intent, Constant.REQUESET_CODE_VERIFY_GESTURE);
                        hud.dismiss();
                    }
                });
                break;
        }

    }

    AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            hud.dismiss();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            hud.dismiss();
            Toast.makeText(getApplicationContext(), "设置失败，请稍后再试！", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_primary_getsture_edit://点击修改手势密码
                //编辑手势密码
                //1.首先校验手势密码
                //关闭手势校验 需要校验密码
                hud.show();
                Handler handler=new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.setClass(PrimaryActivity.this, GestureVerifyActivity.class);
                        //传入手势密码
                        String gestureKey=dbHelper.findGestureKey();
                        intent.putExtra(Constant.GESTURE_KEY, EncryptUtil.decrypt(gestureKey));
                        startActivityForResult(intent, Constant.REQUESET_CODE_UPDATE_GESTURE);
                        hud.dismiss();
                    }
                });
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constant.REQUESET_CODE_EDIT_GESTURE:
                switch (resultCode) {
                    case RESULT_OK:
                        String gestureKey = data.getStringExtra(Constant.GESTURE_KEY);
                        dbHelper.insertGesture(EncryptUtil.encrypt(gestureKey));
                        gestureSwitch(true);
                        break;
                    case RESULT_CANCELED:
                        gestureSwitch(false);
                        break;
                }
                break;
            case Constant.REQUESET_CODE_VERIFY_GESTURE:
                switch (resultCode){
                    case RESULT_OK:
                        gestureSwitch(false);
                        break;
                    case RESULT_CANCELED:
                        gestureSwitch(true);
                        break;
                }
                break;
            case Constant.REQUESET_CODE_UPDATE_GESTURE:
                switch (resultCode){
                    case RESULT_OK:
                        hud.show();
                        //进入设置手势密码界面
                        Handler post_handler=new Handler();
                        post_handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent();
                                intent.setClass(PrimaryActivity.this, GestureEditActivity.class);
                                startActivityForResult(intent, Constant.REQUESET_CODE_EDIT_GESTURE);
                                hud.dismiss();
                            }
                        });
                        break;
                    case RESULT_CANCELED:

                        break;
                }
                break;
        }

    }


    /**
     * 手势锁开关操作
     * @param b
     */
    private void gestureSwitch(boolean b){
        if(b){
            //打开
            if(!primary_gesture_switch.isOpened()){
                primary_gesture_switch.setOpened(true);
            }
            ll_primary_getsture_edit_parent.setVisibility(View.VISIBLE);
        }else{//关闭
            if(primary_gesture_switch.isOpened()){
                primary_gesture_switch.setOpened(false);
            }
            //修改手势功能隐藏
            ll_primary_getsture_edit_parent.setVisibility(View.GONE);
            dbHelper.clearGesture();
        }
    }
}
