package zs.com.wuzhi.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.TextHttpResponseHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import zs.com.wuzhi.R;
import zs.com.wuzhi.adapter.BaseListAdapter;
import zs.com.wuzhi.bean.PageBean;
import zs.com.wuzhi.factory.TextResponseFactory;
import zs.com.wuzhi.widget.SuperRefreshLayout;

/**
 * Created by zhangshuqing on 16/8/17.
 */
public abstract class BaseListActivity<T> extends BaseToolBarActivity implements SuperRefreshLayout.SuperRefreshLayoutListener, AdapterView.OnItemClickListener, BaseListAdapter.Callback {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_LOADING = 1;
    public static final int TYPE_NO_MORE = 2;
    public static final int TYPE_ERROR = 3;
    public static final int TYPE_NET_ERROR = 4;


    @BindView(R.id.superRefreshLaout)
    SuperRefreshLayout superRefreshLayout;

    @BindView(R.id.listView)
    protected ListView mListView;

    private View mFooterView;

    protected BaseListAdapter<T> mAdapter;



    protected PageBean<T> pageBean;

    protected boolean mIsRefresh;

    private ProgressBar mFooterProgressBar;
    private TextView mFooterText;

    protected static ExecutorService mExeService = Executors.newFixedThreadPool(3);

    protected String CACHE_NAME = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_list);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        superRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        superRefreshLayout.setSuperRefreshLayoutListener(this);

        mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_view_footer, null);
        mFooterProgressBar = (ProgressBar) mFooterView.findViewById(R.id.pb_footer);
        mFooterText = (TextView) mFooterView.findViewById(R.id.tv_footer);
        setFooterType(TYPE_LOADING);
        mListView.addFooterView(mFooterView);

        initData();

    }


    protected TextHttpResponseHandler mHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            onRequestFinish();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            pageBean =TextResponseFactory.getDefaultFactory().convertToPageBaen(responseString,getType());
            if (pageBean!=null && pageBean.getItems().size() != 0  ) {
                    setListData(pageBean);
            } else {
                setFooterType(TYPE_NO_MORE);
            }
            onRequestFinish();
        }
    };


    @Override
    boolean isBackHomeVisible() {
        return false;
    }

    @Override
    String getToolBarTitle() {
        return null;
    }

    protected abstract BaseListAdapter<T> getListAdapter();

    protected abstract TextResponseFactory.Type getType();

    protected abstract void setListData(PageBean pageBean);


    protected void initData() {
        mAdapter = getListAdapter();
        mListView.setAdapter(mAdapter);
        onRefresh();


    }

    /**
     * 默认 结束当前Activity
     *
     * @return
     */
    @Override
    OnBackHomeClicklistener getOnBackHomeListener() {
        return new OnBackHomeClicklistener() {
            @Override
            public void backHomeClick() {
                finish();
            }
        };
    }

    @Override
    public void onRefresh() {
        //刷新数据
        mIsRefresh = true;
        requestData();

    }

    @Override
    public void onLoadMore() {
        //加载下一页数据
        requestData();
    }

    protected void requestData() {
        requestDataStart();
        setFooterType(TYPE_LOADING);
    }

    protected void onRequestFinish() {
        onComplete();
    }

    protected void onComplete() {
        superRefreshLayout.onLoadComplete();
        mIsRefresh = false;
    }


    /**
     * 子类实现请求数据
     */
    public abstract void requestDataStart();


    protected void setFooterType(int type) {
        switch (type) {
            case TYPE_NORMAL:
            case TYPE_LOADING:
                mFooterText.setText(getResources().getString(R.string.footer_type_loading));
                mFooterProgressBar.setVisibility(View.VISIBLE);
                break;
            case TYPE_NET_ERROR:
                mFooterText.setText(getResources().getString(R.string.footer_type_net_error));
                mFooterProgressBar.setVisibility(View.GONE);
                break;
            case TYPE_ERROR:
                mFooterText.setText(getResources().getString(R.string.footer_type_error));
                mFooterProgressBar.setVisibility(View.GONE);
                break;
            case TYPE_NO_MORE:
                mFooterText.setText(getResources().getString(R.string.footer_type_not_more));
                mFooterProgressBar.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public abstract void onItemClick(AdapterView<?> parent, View view, int position, long id);

}
