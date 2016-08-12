package zs.com.wuzhi.activity;

import android.os.Bundle;

import butterknife.ButterKnife;
import zs.com.wuzhi.R;

/**
 * Created by zhangshuqing on 16/8/6.
 */
public class AddDiaryActivity  extends BaseToolBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        ButterKnife.bind(this);
    }

    @Override
    boolean isBackHomeVisible() {
        return true;
    }

    @Override
    String getToolBarTitle() {
        return "日记";
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
    boolean needCompleteButton() {
       return true;
    }

    @Override
    OnCompleteClickListener getOnCompleteListener() {
        return super.getOnCompleteListener();
    }
}
