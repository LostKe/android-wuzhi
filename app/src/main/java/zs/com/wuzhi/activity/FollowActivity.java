package zs.com.wuzhi.activity;

import android.os.Bundle;

import zs.com.wuzhi.R;

/**
 * Created by youx on 2016-09-20.
 */
public class FollowActivity extends BaseToolBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_list);
    }

    @Override
    boolean isBackHomeVisible() {
        return true;
    }

    @Override
    String getToolBarTitle() {
        return "关注的人";
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
