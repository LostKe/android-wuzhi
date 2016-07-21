package zs.com.wuzhi.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import zs.com.wuzhi.R;

/**
 * 用户日记
 * Created by zhangshuqing on 16/7/20.
 */
public class DiaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        ButterKnife.bind(this);

    }

}
