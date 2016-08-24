package zs.com.wuzhi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import zs.com.wuzhi.adapter.BaseListAdapter;
import zs.com.wuzhi.adapter.DayDiaryAdpter;
import zs.com.wuzhi.bean.DiarySummary;
import zs.com.wuzhi.bean.PageBean;
import zs.com.wuzhi.db.DBHelper;
import zs.com.wuzhi.factory.TextResponseFactory;
import zs.com.wuzhi.util.Constant;
import zs.com.wuzhi.util.WuzhiApi;

/**
 * Created by zhangshuqing on 16/8/17.
 */
public class MyDiaryActivity extends BaseListActivity {

    DBHelper dbHelper=new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initData() {
        dbHelper.clearDiary();
        super.initData();
    }

    @Override
    boolean isBackHomeVisible() {
        return true;
    }

    @Override
    String getToolBarTitle() {
        return "我的日记";
    }


    @Override
    protected BaseListAdapter<DiarySummary> getListAdapter() {
        return new DayDiaryAdpter(this);
    }

    @Override
    protected TextResponseFactory.Type getType() {
        return TextResponseFactory.Type.DAY_DIARY;
    }

    @Override
    protected void setListData(PageBean pageBean) {
        //下拉刷新清空数据
        if(mIsRefresh){
            mAdapter.clear();

            mAdapter.addItem(pageBean.getItems());

        }else{
            mAdapter.addItem(pageBean.getItems());
        }
        mIsRefresh=false;
        if(!hasNextPage(pageBean.getLastPage(),pageBean.getNextPage())){
            superRefreshLayout.setNoMoreData();
            setFooterType(TYPE_NO_MORE);
        }else{
            superRefreshLayout.setCanLoadMore();
        }
        if(pageBean.getItems().size()==0){
            setFooterType(TYPE_NO_MORE);
        }
    }

    //向服务器请求数据
    @Override
    public void requestDataStart() {
        if(mIsRefresh){
            dbHelper.clearDiary();
        }
        String request_url=mIsRefresh? Constant.MAIN:Constant.MAIN+pageBean.getNextPage();
        WuzhiApi.get(request_url,mHandler);
    }


    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        DiarySummary summary= (DiarySummary) mAdapter.getItem(position);
        String key=summary.getNextPage()+summary.getCurrentDay();
        Intent intent=new Intent();
        intent.putExtra(Constant.DIARY_ID,key);
        intent.putExtra(Constant.DIARY_CURRENT,summary.getCurrentDay());
        intent.setClass(this,MyDiaryDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public Context getContext() {
        return this;
    }


    private boolean hasNextPage(String lastpage,String nextpage){
        boolean r=true;
        if(TextUtils.isEmpty(lastpage) && TextUtils.isEmpty(nextpage)){
            r= false;
        }

        if(Constant.PAGE_END.equals(nextpage)){
            r= false;
        }
        return r;
    }


}
