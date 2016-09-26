package zs.com.wuzhi.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import zs.com.wuzhi.R;
import zs.com.wuzhi.gestureLock.GestureEditEnum;
import zs.com.wuzhi.gestureLock.GestureEnum;
import zs.com.wuzhi.gestureLock.widget.DotViewGroup;
import zs.com.wuzhi.gestureLock.widget.GestureLockViewGroup;
import zs.com.wuzhi.util.Constant;
import zs.com.wuzhi.util.ConvertUtil;
import zs.com.wuzhi.widget.PromptDialog;

/**
 * Created by zhangshuqing on 16/9/24.
 */
public class GestureEditActivity extends BaseToolBarActivity implements View.OnClickListener{


    List<Integer> answer=new ArrayList<Integer>();

    GestureLockViewGroup gesture_edit_lockView;

    DotViewGroup dotViewGroup;

    TextView resetTv;

    TextView gesture_edit_tip;

    GestureEditEnum editStatu=GestureEditEnum.EDIT_INIT;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_edit);
        initView();
    }

    @Override
    boolean isBackHomeVisible() {
        return true;
    }

    @Override
    String getToolBarTitle() {
        return "设置手势密码";
    }

    @Override
    OnBackHomeClicklistener getOnBackHomeListener() {
        return new OnBackHomeClicklistener() {
            @Override
            public void backHomeClick() {
                Intent intent=new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        };
    }

    private void initView() {
        resetTv= (TextView) findViewById(R.id.rest_tv);
        resetTv.setOnClickListener(this);
        gesture_edit_tip= (TextView) findViewById(R.id.gesture_edit_tip);
        dotViewGroup= (DotViewGroup) findViewById(R.id.dotView);

        gesture_edit_lockView= (GestureLockViewGroup) findViewById(R.id.gesture_edit_lockView);
        gesture_edit_lockView.setGestureType(GestureEnum.EDIT);
        gesture_edit_lockView.setOnGestureLockViewListener(new GestureLockViewGroup.OnGestureLockViewListener() {
            @Override
            public void onBlockSelected(int cId) {

            }

            @Override
            public void onGestureEvent(boolean matched) {

            }

            @Override
            public void onUnmatchedExceedBoundary() {

            }

            @Override
            public void onChooseWay(List<Integer> list) {
                if(list==null || list.size()==0){
                    return;
                }
                if(editStatu==GestureEditEnum.EDIT_INIT){//第一次编辑
                    dotViewGroup.setPath(list);
                    answer.addAll(list);
                    resetTv.setVisibility(View.VISIBLE);
                    editStatu=GestureEditEnum.EDIT_FIRST;

                }else{
                    //比较和第一的手势
                    if(ConvertUtil.compare(list,answer)){
                        //两次一致
                        editStatu=GestureEditEnum.EDIT_MATCH;
                        PromptDialog dialog=new PromptDialog(GestureEditActivity.this,R.drawable.card_icon_addtogroup_confirm,"设置成功");
                        dialog.showDialog();

                        final Timer timer=new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                //点击条目 关闭当前Activity  传递参数至父Activity
                                Intent intent=new Intent();
                                intent.putExtra(Constant.GESTURE_KEY, ConvertUtil.listToString(answer));
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        },1001);


                    }else {
                        //提示不一致
                        editStatu=GestureEditEnum.EDIT_NOT_MATCH;
                    }

                }
                makeTip(editStatu);
            }
        });

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rest_tv:
                resetTv.setVisibility(View.INVISIBLE);
                answer.clear();
                editStatu=GestureEditEnum.EDIT_INIT;
                dotViewGroup.reset();
                makeTip(editStatu);
                break;
        }
    }

    private void makeTip(GestureEditEnum value){
        Animation animation = AnimationUtils.loadAnimation(GestureEditActivity.this,R.anim.shake);
        switch (value){
            case EDIT_INIT:
                gesture_edit_tip.setText("绘制解锁图案");
                gesture_edit_tip.setTextColor(0xFF979999);
                break;
            case EDIT_FIRST:
                gesture_edit_tip.setText("再次绘制解锁图案");
                break;
            case EDIT_NOT_MATCH:
                gesture_edit_tip.setText("两次绘制图案不一致，请重新绘制");
                gesture_edit_tip.setTextColor(Color.RED);
                gesture_edit_tip.startAnimation(animation);
                break;
            case EDIT_MATCH:
                gesture_edit_tip.setText("设置成功");
                gesture_edit_tip.setTextColor(0xff108ee9);
                gesture_edit_tip.startAnimation(animation);
                break;
        }

    }
}
