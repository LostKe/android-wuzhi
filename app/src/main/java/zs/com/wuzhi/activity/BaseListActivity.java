package zs.com.wuzhi.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.wuzhi.R;
import zs.com.wuzhi.widget.SuperRefreshLayout;

/**
 * Created by zhangshuqing on 16/8/17.
 */
public class BaseListActivity<T> extends BaseToolBarActivity implements SuperRefreshLayout.SuperRefreshLayoutListener,AdapterView.OnItemClickListener {

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

    private ListAdapter mAdapter;

    private ProgressBar mFooterProgressBar;
    private TextView mFooterText;

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

        mFooterView= LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_list_view_footer,null);
        mFooterProgressBar= (ProgressBar) mFooterView.findViewById(R.id.pb_footer);
        mFooterText= (TextView) mFooterView.findViewById(R.id.tv_footer);
        setFooterType(TYPE_LOADING);
        mListView.addFooterView(mFooterView);

       // superRefreshLayout.onLoadComplete(); 刷新完成
    }

    private void initData(){
        mListView.setAdapter(mAdapter);
    }

    @Override
    boolean isBackHomeVisible() {
        return false;
    }

    @Override
    String getToolBarTitle() {
        return null;
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


    }

    @Override
    public void onLoadMore() {

    }


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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

}
