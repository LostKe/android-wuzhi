package zs.com.wuzhi.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import zs.com.wuzhi.R;
import zs.com.wuzhi.widget.Topbar;

/**
 * Created by zhangshuqing on 16/10/22.
 */
public class TopBarHelper {

    private Context mContext;
    private int layoutId;
    private LayoutInflater mInflater;

    /*base view*/
    private LinearLayout mContentView;

    /*用户定义的view*/
    private View mUserView;

    private Topbar topbar;



    public TopBarHelper(Context context,int layoutId){
        this.mContext=context;
        this.layoutId=layoutId;
        mInflater=LayoutInflater.from(context);
        init();
    }

    private void init() {
        mContentView = new LinearLayout(mContext);
        mContentView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);

        //设置 topBar
        View topbarView=mInflater.inflate(R.layout.topbar,mContentView);
        topbar= (Topbar) topbarView.findViewById(R.id.topbar);

        //内容view
        mUserView = mInflater.inflate(layoutId, null);
        FrameLayout.LayoutParams userParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.addView(mUserView, userParams);



    }





    public Topbar getTopbar(){
        return topbar;
    }


    public View getContentView() {
        return mContentView;
    }

}
