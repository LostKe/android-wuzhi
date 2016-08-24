package zs.com.wuzhi.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * 下拉刷新，上拉加载控件
 * Created by zhangshuqing on 16/8/15.
 */
public class SuperRefreshLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {


    private ListView mListView;

    /** 手滑动的距离*/
    private int mTouchSlop;


    private SuperRefreshLayoutListener mListener;

    private boolean mIsOnLoading = false;

    private boolean mCanLoadMore = false;

    private int mYDown;

    private int mLastY;

    private int mTextColor;

    private int mFooterBackground;

    private boolean mIsMoving = false;


    public SuperRefreshLayout(Context context) {
        super(context);
    }

    public SuperRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        if(mListener!=null && !mIsOnLoading){
            mListener.onRefresh();
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(mListView==null){//初始化listView
            getListView();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(canLoad()){
            loadData();
        }
    }


    /**
     * 获取ListView并添加Footer
     */
    private void getListView() {
        int child = getChildCount();
        if (child > 0) {

            for (int i = 0;i<child;i++){
                View childView = getChildAt(i);
                if (childView instanceof ListView) {
                    mListView = (ListView) childView;
                    // 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
                    mListView.setOnScrollListener(this);
                    break;
                }
            }

        }
    }

    /**
     * 监听手指的操作
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 按下
                mYDown = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                // 移动
                mIsMoving = true;
                mLastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                mIsMoving = false;
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }


    /**
     * 判断是否到了最底部
     */
    private boolean isInBottom() {
        return (mListView != null && mListView.getAdapter() != null)
                && mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
    }


    /**
     * 如果到了最底部,而且是上拉操作.那么执行onLoad方法
     */
    private void loadData() {
        if (mListener != null) {
            setIsOnLoading(true);
            mListener.onLoadMore();
        }
    }

    /**
     * 是否可以加载更多, 条件是到了最底部, listview不在加载中, 且为上拉操作.
     *
     * @return 是否可以加载更多
     */
    private boolean canLoad() {
        return isInBottom() && !mIsOnLoading && isPullUp() && mCanLoadMore;
    }

    /**
     * 是否是上拉操作()
     *
     * @return 是否是上拉操作
     */
    private boolean isPullUp() {
        return (mYDown - mLastY) >= mTouchSlop;
    }


    public void setCanLoadMore() {
        this.mCanLoadMore = true;
    }

    public void setNoMoreData() {
        this.mCanLoadMore = false;
    }
    /**
     * 设置正在加载
     *
     * @param loading loading
     */
    public void setIsOnLoading(boolean loading) {
        mIsOnLoading = loading;
        if (!mIsOnLoading) {
            mYDown = 0;
            mLastY = 0;
        }
    }

    /**
     * 加载结束记得调用
     */
    public void onLoadComplete() {
        setIsOnLoading(false);
        setRefreshing(false);
    }

    /**
     * 下拉刷新
     */
    public interface SuperRefreshLayoutListener {
        /**
         * 刷新当前
         */
        void onRefresh();

        /**
         * 加载更多
         */
        void onLoadMore();
    }


    public void setSuperRefreshLayoutListener(SuperRefreshLayoutListener loadListener) {
        mListener = loadListener;
    }

}
