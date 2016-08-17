package zs.com.wuzhi.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.wuzhi.MainTab;
import zs.com.wuzhi.R;




/**
 * Created by zhangshuqing on 16/7/26.
 */
public class MainActivity extends BaseToolBarActivity implements View.OnClickListener {


    @BindView(R.id.tabHost)
    FragmentTabHost mTabHost;

    @BindView(R.id.quick_option_iv)
    ImageView iv_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public boolean isBackHomeVisible() {
        return false;
    }

    @Override
    public String getToolBarTitle() {
        return getResources().getString(R.string.app_name);
    }

    @Override
    public BaseToolBarActivity.OnBackHomeClicklistener getOnBackHomeListener() {
        return null;
    }


    private void initView() {
        mTabHost.setup(this,getSupportFragmentManager(),R.id.tab_content);
        initTabs();
        iv_add.setOnClickListener(this);
        mTabHost.setCurrentTab(0);
    }

    private void initTabs() {
        MainTab[] tabs= MainTab.values();
        for (int i=0;i<tabs.length;i++){
            MainTab tab= tabs[i];
            TabHost.TabSpec spec=mTabHost.newTabSpec(tab.getTag());
            View indicator= LayoutInflater.from(this).inflate(R.layout.tab_indicator,null);
            ImageView icon_iv= (ImageView) indicator.findViewById(R.id.iv_icon);
            TextView tab_title= (TextView) indicator.findViewById(R.id.tab_title);
            Drawable drawable = this.getResources().getDrawable(tab.getResId());
            icon_iv.setImageDrawable(drawable);
            //tab_title.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);

            if (tab==MainTab.ADD) {//日记
                indicator.setVisibility(View.INVISIBLE);
            }
            tab_title.setText(tab.getTag());
            spec.setIndicator(indicator);
            mTabHost.addTab(spec,tab.getClazz(),null);

           // mTabHost.getTabWidget().getChildAt(i).setOnTouchListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()){
            case R.id.quick_option_iv:
                //进去写日记界面
                intent.setClass(this,AddDiaryActivity.class);
                break;
        }
        startActivity(intent);
    }
}

