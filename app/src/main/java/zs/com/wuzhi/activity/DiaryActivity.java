package zs.com.wuzhi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.wuzhi.R;
import zs.com.wuzhi.adapter.DiaryAdpter;
import zs.com.wuzhi.bean.Diary;
import zs.com.wuzhi.bean.PersonDiary;
import zs.com.wuzhi.db.DBHelper;
import zs.com.wuzhi.util.Constant;
import zs.com.wuzhi.util.WuzhiSprider;
import zs.com.wuzhi.widget.PromptDialog;

/**
 * 用户日记
 * Created by zhangshuqing on 16/7/20.
 */
public class DiaryActivity extends BaseToolBarActivity implements View.OnClickListener{

    public final static String PERSON_DIARY = "person_diary";

    public final static int FOLLOW_QRY=1;
    public final static int FOLLOW_INSERT=2;

    @BindView(R.id.tv_diary_date)
    TextView tv_diary_date;

    @BindView(R.id.tv_star_count)
    TextView tv_star_count;


    @BindView(R.id.lv_diary_content)
    ListView lv_diary_content;

    @BindView(R.id.ll_bottom)
    LinearLayout ll_bottom;//检测到如果该用户已经被关注了 应该隐藏

    PersonDiary personDiary;

    private KProgressHUD hud;

    ExecutorService service;
    String userId;

    String toolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        toolbarTitle=intent.getExtras().getString(Constant.TOOL_BAR_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        ButterKnife.bind(this);
        userId = intent.getExtras().getString(Constant.USER_ID);
        initFooter(userId);
        hud=KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        hud.show();
        Handler diaryHandler = new DiaryMessageHandler();
        new InitContentThread(diaryHandler, userId).start();

    }

    private void initFooter(final String userId) {
        service= Executors.newSingleThreadExecutor();
        //查询数据库中关注人列表是否有 该条记录
        final FooterUIHandler handler=new FooterUIHandler();
        service.execute(new Runnable() {
            @Override
            public void run() {
                DBHelper helper=new DBHelper(getApplicationContext());
                boolean b=helper.isExistFollow(userId);
                Message message=Message.obtain();
                message.what=FOLLOW_QRY;
                message.obj=b;
                handler.sendMessage(message);
            }
        });

        ll_bottom.setOnClickListener(this);

    }

    @Override
    boolean isBackHomeVisible() {
        return true;
    }


    @Override
    String getToolBarTitle() {
        return TextUtils.isEmpty(toolbarTitle)?"此刻":toolbarTitle;
    }

    @Override
    OnBackHomeClicklistener getOnBackHomeListener() {
        return new OnBackHomeClicklistener() {
            @Override
            public void backHomeClick() {
                finish();
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_bottom:
                hud.show();
                final FooterUIHandler hanler=new FooterUIHandler();
                service.execute(new Runnable() {
                    @Override
                    public void run() {
                        //将id插入数据库
                        DBHelper helper=new DBHelper(getApplicationContext());
                        helper.insertFollow(userId);
                        Message message=Message.obtain();
                        message.what=FOLLOW_INSERT;
                        hanler.sendMessage(message);

                    }
                });
                break;
        }
    }


    /**
     * 处理日记
     */
    class DiaryMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Object obj =  msg.getData().getSerializable(PERSON_DIARY);
            if(obj!=null){
                personDiary= (PersonDiary) obj;
                //设置头
                tv_diary_date.setText(personDiary.getCurrent());
                //设置尾
               // tv_author.setText(personDiary.getUserName());
                //设置星
                tv_star_count.setText(personDiary.getFlower());
            }
            //设置日记内容
            List<Diary> diaryList=new ArrayList<Diary>();
            if(personDiary!=null && personDiary.getDiaryList()!=null){
                diaryList=personDiary.getDiaryList();
            }
            DiaryAdpter diaryAdpter = new DiaryAdpter(diaryList,getApplication());
            lv_diary_content.setAdapter(diaryAdpter);
            hud.dismiss();

        }
    }

    class FooterUIHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case FOLLOW_QRY:
                    boolean b= (boolean) msg.obj;
                    if(!b){
                        ll_bottom.setVisibility(View.VISIBLE);
                        ll_bottom.setOnClickListener(DiaryActivity.this);
                    }
                    break;
                case FOLLOW_INSERT:
                    ll_bottom.setVisibility(View.GONE);
                    hud.dismiss();
                    PromptDialog dialog=new PromptDialog(DiaryActivity.this,R.drawable.card_icon_addtogroup_confirm,"关注成功");
                    dialog.showDialog();
                    break;
            }
        }
    }

    class InitContentThread extends Thread {
        Handler handler;
        String user_id;

        InitContentThread(Handler handler, String user_id) {
            this.handler = handler;
            this.user_id = user_id;
        }

        @Override
        public void run() {
            PersonDiary personDiary = WuzhiSprider.getPersonDiaryById(user_id);
            Message msg = Message.obtain();
            msg.getData().putSerializable(PERSON_DIARY, personDiary);
            handler.sendMessage(msg);
        }
    }

}
