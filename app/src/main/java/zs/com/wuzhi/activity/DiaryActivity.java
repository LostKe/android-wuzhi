package zs.com.wuzhi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.wuzhi.R;
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

    class DiaryAdpter extends BaseAdapter {
        ViewHolder viewHolder;
        @Override
        public int getCount() {
            if(personDiary==null){
                return 0;
            }else{
                List<Diary> diaryList=personDiary.getDiaryList();
                return diaryList==null?0:diaryList.size();
            }

        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Diary diary=personDiary.getDiaryList().get(position);
            if(convertView==null){
                convertView=getLayoutInflater().inflate(R.layout.diary_item,null);
                viewHolder=new ViewHolder();
                viewHolder.tv_content= (TextView) convertView.findViewById(R.id.tv_diary_content);
                viewHolder.tv_time= (TextView) convertView.findViewById(R.id.tv_diary_time);
                convertView.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_content.setText(diary.getContent());
            viewHolder.tv_time.setText(diary.getTime());
            return convertView;
        }

        final class ViewHolder {
            public TextView tv_time;
            public TextView tv_content;
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
                tv_author.setText(personDiary.getUserName());
                //设置星
                tv_star_count.setText(personDiary.getFlower());
            }
            //设置日记内容
            DiaryAdpter diaryAdpter = new DiaryAdpter();
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
