package zs.com.wuzhi.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.loopj.android.http.TextHttpResponseHandler;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import zs.com.wuzhi.R;
import zs.com.wuzhi.adapter.BaseListAdapter;
import zs.com.wuzhi.adapter.FollowAdapter;
import zs.com.wuzhi.bean.UserInfo;
import zs.com.wuzhi.db.DBHelper;
import zs.com.wuzhi.util.ResponseUtil;
import zs.com.wuzhi.util.WuzhiApi;

/**
 * Created by youx on 2016-09-20.
 */
public class FollowActivity extends BaseToolBarActivity implements BaseListAdapter.Callback{

    private final static int QRY_DB=1;
    private final static int QRY_NETWORK=2;

    DBHelper dbHelper;

    ExecutorService service;



    @BindView(R.id.follow_listView)
    protected ListView mListView;

    FollowAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        ButterKnife.bind(this);
        dbHelper=new DBHelper(this);
        service= Executors.newSingleThreadExecutor();
        initView();
        initData();

    }



    private void initView() {
    }

    private void initData() {
        adapter=new FollowAdapter(this);
        mListView.setAdapter(adapter);
        final FollowHandler mHandler=new FollowHandler();
        service.execute(new Runnable() {
            @Override
            public void run() {
                Message message=Message.obtain();
                message.what=QRY_DB;
                List<String> userList=dbHelper.queryFollowAll();
                message.obj=userList;
                mHandler.sendMessage(message);
            }
        });
    }


    @Override
    boolean isBackHomeVisible() {
        return true;
    }

    @Override
    String getToolBarTitle() {
        return "关注的人";
    }

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
    public Context getContext() {
        return this;
    }

    class FollowHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case QRY_DB:
                    //db 中获取到用户Id
                   final List<String> userList= (List<String>) msg.obj;

                    //网络中获取用户具体信息 添加到  adapter中
                    service.execute(new Runnable() {
                        @Override
                        public void run() {
                            for (String id:userList){
                                WuzhiApi.getUserInfo(id,mHandler);
                            }
                        }
                    });
                    break;
                case QRY_NETWORK:
                    break;

            }
        }
    }



    protected TextHttpResponseHandler mHandler=new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            UserInfo userInfo= ResponseUtil.findUserInfo(responseString);
            adapter.addItem(userInfo);
        }
    };
}
