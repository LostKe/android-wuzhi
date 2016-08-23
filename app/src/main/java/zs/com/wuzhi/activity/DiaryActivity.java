package zs.com.wuzhi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.wuzhi.R;
import zs.com.wuzhi.adapter.DiaryAdpter;
import zs.com.wuzhi.bean.Diary;
import zs.com.wuzhi.bean.PersonDiary;
import zs.com.wuzhi.util.Constant;
import zs.com.wuzhi.util.WuzhiSprider;

/**
 * 用户日记
 * Created by zhangshuqing on 16/7/20.
 */
public class DiaryActivity extends BaseToolBarActivity {

    public final static String PERSON_DIARY = "person_diary";

    @BindView(R.id.tv_diary_date)
    TextView tv_diary_date;

    @BindView(R.id.tv_star_count)
    TextView tv_star_count;

    @BindView(R.id.tv_author)
    TextView tv_author;

    @BindView(R.id.lv_diary_content)
    ListView lv_diary_content;

    PersonDiary personDiary;

    private KProgressHUD hud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String user_id = intent.getExtras().getString(Constant.USER_ID);

        hud=KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        hud.show();
        Handler diaryHandler = new DiaryMessageHandler();
        new InitContentThread(diaryHandler, user_id).start();

    }

    @Override
    boolean isBackHomeVisible() {
        return true;
    }


    @Override
    String getToolBarTitle() {
        return "此刻";
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
                tv_author.setText(personDiary.getUserName());
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
