package zs.com.wuzhi.gestureLock.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

/**
 * 画小圆点
 * Created by zhangshuqing on 16/9/24.
 */
public class DotView extends View {

    public enum Mode{
        NORMAL, ACTION;
    }

    public  Mode mCurrentStatus = Mode.NORMAL;
    int mNormalDotColor;//圆点的颜色
    int mActionDotColor;//圆点的颜色
    /**
     * 圆心坐标
     */
    private int mCenterX;
    private int mCenterY;
    private int mRadius;


    private  int mWidth;
    private  int mHeight;
    private Paint mPaint;
    private static final int STROKEWIDTH=2;


    public DotView(Context context) {
        super(context);
    }

    public DotView(Context context,int mNormalDotColor,int mActionDotColor){
        super(context);
        setWillNotDraw(false);
        this.mNormalDotColor=mNormalDotColor;
        this.mActionDotColor=mActionDotColor;
        mPaint=new Paint();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth=MeasureSpec.getSize(widthMeasureSpec);
        mHeight=MeasureSpec.getSize(heightMeasureSpec);
        mWidth = mWidth < mHeight ? mWidth : mHeight;
        mCenterX = mCenterY = (mWidth / 2);
        mRadius =mCenterX-STROKEWIDTH/2;//减去边线的宽度
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.reset();
        mPaint.setAntiAlias(true);
        switch (mCurrentStatus){
            case NORMAL://画空心圆
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(STROKEWIDTH);
                mPaint.setColor(mNormalDotColor);
                break;
            case ACTION://画实心圆
                mPaint.setColor(mActionDotColor);
                mPaint.setStyle(Paint.Style.FILL);
                break;
        }
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
        Log.e("Dot>>>>>>>","mCenterX:"+mCenterX);

    }

    /**
     * 设置当前模式并重绘界面
     *
     * @param mode
     */
    public void setMode(Mode mode) {
        this.mCurrentStatus = mode;
        invalidate();
    }
}
