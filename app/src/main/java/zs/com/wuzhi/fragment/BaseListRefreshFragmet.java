package zs.com.wuzhi.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 下拉刷新 ListView base类
 * Created by zhangshuqing on 16/7/18.
 */
public abstract class BaseListRefreshFragmet extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public  void onDestroy(){
        super.onDestroy();
    }

    /**
     * 初始化下拉刷新控件
     */
    protected void initRefresh(){
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    /**
     * 下拉刷新后的动作
     */
    protected abstract void refresh();

    /**
     * 初始化界面
     */
    public  abstract void initView();

}
