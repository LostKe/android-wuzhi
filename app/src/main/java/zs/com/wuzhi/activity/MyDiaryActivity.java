package zs.com.wuzhi.activity;

import android.os.Bundle;

/**
 * Created by zhangshuqing on 16/8/17.
 */
public class MyDiaryActivity extends BaseListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
    }

    @Override
    boolean isBackHomeVisible() {
        return true;
    }

    @Override
    String getToolBarTitle() {
        return "我的日记";
    }
}
