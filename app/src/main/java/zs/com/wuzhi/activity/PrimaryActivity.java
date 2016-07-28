package zs.com.wuzhi.activity;

import android.os.Bundle;

import zs.com.wuzhi.R;

/**
 * Created by zhangshuqing on 16/7/24.
 */
public class PrimaryActivity extends BaseToolBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);
    }

    @Override
    boolean isBackHomeVisible() {
        return true;
    }

    @Override
    String getToolBarTitle() {
        return "隐私";
    }

    @Override
    public OnBackHomeClicklistener getOnBackHomeListener() {
        OnBackHomeClicklistener onBackHomeClicklistener = new OnBackHomeClicklistener() {
            @Override
            public void backHomeClick() {
                finish();
            }
        };
        return onBackHomeClicklistener;
    }
}
