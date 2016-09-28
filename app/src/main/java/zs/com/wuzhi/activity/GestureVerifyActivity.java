package zs.com.wuzhi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import zs.com.wuzhi.Helper.GlideCircleTransform;
import zs.com.wuzhi.R;
import zs.com.wuzhi.db.DBHelper;
import zs.com.wuzhi.gestureLock.widget.GestureLockViewGroup;
import zs.com.wuzhi.util.Constant;
import zs.com.wuzhi.util.ConvertUtil;
import zs.com.wuzhi.util.ResponseUtil;
import zs.com.wuzhi.util.WuzhiApi;
import zs.com.wuzhi.widget.PromptDialog;


/**
 * Created by zhangshuqing on 16/9/24.
 */
public class GestureVerifyActivity extends BaseToolBarActivity implements View.OnClickListener{

    GestureLockViewGroup gestureLockViewGroup;

    TextView gesture_verify_tip;

    ImageView userLogo;

    TextView gesture_verify_forgot;

    String gestureKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureKey=getIntent().getExtras().getString(Constant.GESTURE_KEY);
        setContentView(R.layout.activity_gesture_verify);
        initView();
    }



    private void initView() {
        gestureLockViewGroup= (GestureLockViewGroup) findViewById(R.id.gesture_verify_lockView);
        gestureLockViewGroup.setAnswer(ConvertUtil.stringToArray(gestureKey));

        gesture_verify_tip= (TextView) findViewById(R.id.gesture_verify_tip);
        gesture_verify_forgot= (TextView) findViewById(R.id.gesture_verify_forgot);
        gesture_verify_forgot.setOnClickListener(this);
        userLogo= (ImageView) findViewById(R.id.user_logo);
        WuzhiApi.getAvatar(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String content=new String(responseBody);
                String imgUrl= ResponseUtil.getAvatar(content);
                Glide.with(GestureVerifyActivity.this).load(imgUrl).transform(new GlideCircleTransform(GestureVerifyActivity.this)).into(userLogo);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
        gestureLockViewGroup.setOnGestureLockViewListener(new GestureLockViewGroup.OnGestureLockViewListener() {
            @Override
            public void onBlockSelected(int cId) {

            }

            @Override
            public void onGestureEvent(boolean matched) {
                if(!matched){
                    //设置动画
                    Animation anim= AnimationUtils.loadAnimation(GestureVerifyActivity.this,R.anim.shake);
                    gesture_verify_tip.setVisibility(View.VISIBLE);
                    gesture_verify_tip.startAnimation(anim);
                }else {
                    gesture_verify_tip.setVisibility(View.INVISIBLE);
                    PromptDialog dialog=new PromptDialog(GestureVerifyActivity.this,R.drawable.card_icon_addtogroup_confirm,"校验成功");
                    dialog.showDialog();
                    final Timer timer=new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            setResult(RESULT_OK);
                            finish();
                        }
                    },1001);
                }
            }

            @Override
            public void onUnmatchedExceedBoundary() {

            }

            @Override
            public void onChooseWay(List<Integer> list) {

            }
        });

    }

    @Override
    boolean isBackHomeVisible() {
        return true;
    }

    @Override
    String getToolBarTitle() {
        return "校验手势";
    }

    @Override
    OnBackHomeClicklistener getOnBackHomeListener() {
        return new OnBackHomeClicklistener() {
            @Override
            public void backHomeClick() {
                setResult(RESULT_CANCELED);
                finish();
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gesture_verify_forgot:
                DBHelper dbHelper=new DBHelper(this);
                dbHelper.clearGesture();//将手势密码设置为空
                //跳转到登录界面
                Intent intent=new Intent();
                intent.setClass(this,LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
