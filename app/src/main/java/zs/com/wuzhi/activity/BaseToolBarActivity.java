package zs.com.wuzhi.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import zs.com.wuzhi.Helper.ToolbarHelper;
import zs.com.wuzhi.R;

/**
 * 具有toolbar 的Activity
 * Created by zhangshuqing on 16/7/26.
 */
public abstract class BaseToolBarActivity extends AppCompatActivity {

    ToolbarHelper mToolBarHelper ;

    Toolbar toolbar;

    ActionBar mActionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mToolBarHelper=new ToolbarHelper(this,layoutResID);
        toolbar=mToolBarHelper.getToolBar();
        initToolBar(toolbar);
        toolbar.setTitle(getToolBarTitle());
        setContentView(mToolBarHelper.getContentView());
        setSupportActionBar(toolbar);
        mActionBar=getSupportActionBar();
        onCreateCustomToolBar();
    }

    protected  void initToolBar(Toolbar toolbar){
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }

    /**
     * 设置toolbar的操作
     */
    protected  void onCreateCustomToolBar(){
        mActionBar.setDisplayHomeAsUpEnabled(isBackHomeVisible());
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        mActionBar.setHomeAsUpIndicator(upArrow);
    }

    OnBackHomeClicklistener listener;


    public interface OnBackHomeClicklistener{
        void backHomeClick();
    }

     abstract boolean isBackHomeVisible();

     abstract String  getToolBarTitle();


    abstract OnBackHomeClicklistener  getOnBackHomeListener();


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                OnBackHomeClicklistener l=getOnBackHomeListener();
                if(l!=null){
                    getOnBackHomeListener().backHomeClick();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
