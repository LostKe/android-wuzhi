package zs.com.wuzhi.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import zs.com.wuzhi.Helper.TopBarHelper;
import zs.com.wuzhi.R;

/**
 * Created by zhangshuqing on 16/10/23.
 */
public class TopBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topbar);
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        TopBarHelper helper = new TopBarHelper(this, layoutResID);
        setContentView(helper.getContentView());
        Log.e(">>>>>>>>", "test");
    }
}
