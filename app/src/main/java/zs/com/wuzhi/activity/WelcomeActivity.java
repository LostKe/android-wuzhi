package zs.com.wuzhi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import zs.com.wuzhi.R;

/**
 * Created by zhangshuqing on 16/7/23.
 */
public class WelcomeActivity  extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view=View.inflate(this, R.layout.activity_welcome,null);
        setContentView(view);
        //动画渐变启动界面
        AlphaAnimation animation=new AlphaAnimation(0.5f,1.0f);
        animation.setDuration(800);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                    redirectTo();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 跳转到...
     */
    private void redirectTo() {
       // Intent uploadLog = new Intent(this, LogUploadService.class);
       // startService(uploadLog);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
