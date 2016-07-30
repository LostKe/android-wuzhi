package zs.com.wuzhi.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.Menu;
import android.view.MenuItem;

import zs.com.wuzhi.Helper.ToolbarHelper;
import zs.com.wuzhi.R;

/**
 * 具有toolbar 的Activity
 * Created by zhangshuqing on 16/7/26.
 */
public abstract class BaseToolBarActivity extends AppCompatActivity implements OnMenuItemClickListener {

    ToolbarHelper mToolBarHelper;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (needCompleteButton()) {
            getMenuInflater().inflate(R.menu.base_toolbar_menu, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mToolBarHelper = new ToolbarHelper(this, layoutResID);
        toolbar = mToolBarHelper.getToolBar();
        initToolBar(toolbar);
        setContentView(mToolBarHelper.getContentView());
        setSupportActionBar(toolbar);
        onCreateCustomToolBar();
    }

    protected void initToolBar(Toolbar toolbar) {
        if (isBackHomeVisible()) {
            toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        }
        toolbar.setTitle(getToolBarTitle());
        toolbar.setTitleTextAppearance(getApplicationContext(), R.style.toolbar_title_style);
        toolbar.setOnMenuItemClickListener(this);
    }

    /**
     * 设置toolbar的操作
     */
    protected void onCreateCustomToolBar() {
    }


    public interface OnBackHomeClicklistener {
        void backHomeClick();
    }

    public interface OnCompleteClickListener {
        void onCompleteClick();
    }

    abstract boolean isBackHomeVisible();


    abstract String getToolBarTitle();



    abstract OnBackHomeClicklistener getOnBackHomeListener();


    boolean needCompleteButton() {
        return false;
    }
    /**
     * 若设置显示 完成按钮则需要重写此方法
     *
     * @return
     */
    OnCompleteClickListener getOnCompleteListener() {
        return null;
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getOnBackHomeListener().backHomeClick();
                break;
            case R.id.toolbar_complete:
                getOnCompleteListener().onCompleteClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
