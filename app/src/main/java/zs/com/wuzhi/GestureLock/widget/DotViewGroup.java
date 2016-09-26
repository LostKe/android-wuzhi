package zs.com.wuzhi.gestureLock.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import zs.com.wuzhi.R;
import zs.com.wuzhi.gestureLock.util.ConvertUtil;

/**
 * Created by zhangshuqing on 16/9/24.
 * 绘制解锁图案提示view   画 mCount*mCount 的圆点图案
 */
public class DotViewGroup extends RelativeLayout {


    private int mCount=3;

    private DotView[] dotViews;
    private List<Integer> answer=new ArrayList<Integer>();

    //圆点大小
    private int mDotSize=10;


    private int mDotDistance;

    private int mNormalDotColor=0xFF252222;
    private int mActionDotColor=0xff108ee9;



    public DotViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);//强制调用 onDraw方法
        TypedArray arry= context.obtainStyledAttributes(attrs,R.styleable.DotViewGroup);
        mCount=arry.getInteger(R.styleable.DotViewGroup_count,mCount);
        mDotSize= ConvertUtil.dip2px(context,arry.getInteger(R.styleable.DotViewGroup_dotSize,mDotSize));
        mNormalDotColor=arry.getColor(R.styleable.DotViewGroup_normal_color,mNormalDotColor);
        mActionDotColor=arry.getColor(R.styleable.DotViewGroup_action_color,mActionDotColor);
        arry.recycle();
        mDotDistance=mDotSize/5;//圆点之间的间隔
        dotViews=new DotView[mCount*mCount ];
        addView();//添加子View
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }


    private void addView(){
        //绘制n*n的 的宫格
        for (int i=0;i<dotViews.length;i++){
            dotViews[i]=new DotView(getContext(),mNormalDotColor,mActionDotColor);
            dotViews[i].setId(i+1);
            LayoutParams lockerParams = new LayoutParams(mDotSize, mDotSize);//设置view的宽高
            // 不是每行的第一个，则设置位置为前一个的右边
            if (i % mCount != 0) {
                lockerParams.addRule(RelativeLayout.RIGHT_OF, dotViews[i - 1].getId());
            }
            // 从第二行开始，设置为上一行同一位置View的下面
            if (i > mCount - 1) {
                lockerParams.addRule(RelativeLayout.BELOW, dotViews[i - mCount].getId());
            }
            //设置右下左上的边距
            int rightMargin = mDotDistance;
            int bottomMargin = mDotDistance;
            int leftMagin = 0;
            int topMargin = 0;

            lockerParams.setMargins(leftMagin, topMargin, rightMargin, bottomMargin);
            dotViews[i].setMode(DotView.Mode.NORMAL);
            addView(dotViews[i], lockerParams);
        }
    }




    /**
     * 重新绘制
     * @param list
     */
    public void setPath(List<Integer> list){
        answer=list;
        for (Integer v:list){
            dotViews[v-1].setMode(DotView.Mode.ACTION);
        }
    }

    public void reset(){
        for (DotView d:dotViews){
            d.setMode(DotView.Mode.NORMAL);
        }
    }
}
