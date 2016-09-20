package zs.com.wuzhi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.wuzhi.R;
import zs.com.wuzhi.adapter.DiaryAdpter;
import zs.com.wuzhi.bean.Diary;
import zs.com.wuzhi.db.DBHelper;
import zs.com.wuzhi.util.Constant;

/**
 * 我的日记详情
 * Created by zhangshuqing on 16/8/23.
 */
public class MyDiaryDetailActivity extends  BaseToolBarActivity{

    @BindView(R.id.tv_diary_date)
    TextView tv_diary_date;

    @BindView(R.id.tv_star_count)
    TextView tv_star_count;



    @BindView(R.id.lv_diary_content)
    ListView lv_diary_content;

    @BindView(R.id.iv_flower)
    ImageView iv_flower;

    @BindView(R.id.ll_bottom)
    LinearLayout ll_bottom;

    private KProgressHUD hud;

    ExecutorService service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String diary_id = intent.getExtras().getString(Constant.DIARY_ID);
        String diary_current = intent.getExtras().getString(Constant.DIARY_CURRENT);
        hud=KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        hud.show();
        initView(diary_id,diary_current);
    }

    private void initView(final String key,String diary_current) {
        iv_flower.setVisibility(View.GONE);
        tv_star_count.setVisibility(View.GONE);
        tv_diary_date.setText(diary_current.trim());
        ll_bottom.setVisibility(View.INVISIBLE);
        service= Executors.newSingleThreadExecutor();
        final Handler handder=new DataHandler();
        service.execute(new Runnable() {
            @Override
            public void run() {
                //获取数据
                DBHelper helper=new DBHelper(getApplicationContext());
                String text=helper.findContent(key);
                List<Diary> dayDaiyList=JSON.parseArray(text, Diary.class);
                Message message=Message.obtain();
                message.obj=dayDaiyList;
                handder.sendMessage(message);
            }
        });

    }


    class DataHandler extends  Handler{
        @Override
        public void handleMessage(Message msg) {
            //获取到数据，更新UI
            List<Diary> dayDaiyList= (List<Diary>) msg.obj;
            DiaryAdpter adpter=new DiaryAdpter(dayDaiyList,getApplicationContext());
            lv_diary_content.setAdapter(adpter);
            hud.dismiss();
        }
    }



    @Override
    boolean isBackHomeVisible() {
        return true;
    }

    @Override
    String getToolBarTitle() {
        return "日记列表";
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
}
