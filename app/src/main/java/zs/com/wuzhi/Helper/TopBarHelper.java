package zs.com.wuzhi.Helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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
    private FrameLayout mContentView;

    /*用户定义的view*/
    private View mUserView;

    private Topbar topbar;

    /*
   * 两个属性
   * 1、toolbar是否悬浮在窗口之上
   * 2、toolbar的高度获取
   * */
    private static int[] ATTRS = {
            R.attr.windowActionBarOverlay,
            R.attr.actionBarSize
    };

    public TopBarHelper(Context context,int layoutId){
        this.mContext=context;
        this.layoutId=layoutId;
        mInflater=LayoutInflater.from(context);
        init();
    }

    private void init() {
         /*直接创建一个帧布局，作为视图容器的父容器*/
        mContentView = new FrameLayout(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);

        //内容view
        mUserView = mInflater.inflate(layoutId, null);
        FrameLayout.LayoutParams userParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(ATTRS);
        /*获取主题中定义的悬浮标志*/
        boolean overly = typedArray.getBoolean(0, false);
        /*获取主题中定义的toolbar的高度*/
        int toolBarSize = (int) mContext.getResources().getDimension(R.dimen.abc_action_bar_default_height_material);
        typedArray.recycle();

        /*如果是悬浮状态，则不需要设置间距*/
        userParams.topMargin = overly ? 0 : toolBarSize;
        mContentView.addView(mUserView, userParams);

        //设置 topBar
        View topbarView=mInflater.inflate(R.layout.topbar,mContentView);
        topbar= (Topbar) topbarView.findViewById(R.id.topbar);

    }





    public Topbar getTopbar(){
        return topbar;
    }


    public FrameLayout getContentView() {
        return mContentView;
    }

}
