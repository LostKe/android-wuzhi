package zs.com.wuzhi.activity;

import android.os.Bundle;

import butterknife.ButterKnife;
import zs.com.wuzhi.R;

/**
 * 用户日记
 * Created by zhangshuqing on 16/7/20.
 */
public class DiaryActivity extends BaseToolBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        ButterKnife.bind(this);

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


}
