package zs.com.wuzhi.gestureLock.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.view.View;

import zs.com.wuzhi.gestureLock.util.ConvertUtil;

public class GestureLockView extends View {
	private static final String TAG = "GestureLockView";
	/**
	 * GestureLockView的三种状态
	 */
	public enum Mode{
		STATUS_NO_FINGER, STATUS_FINGER_ON, STATUS_FINGER_UP_SUCCESS,STATUS_FINGER_UP_ERROR;
	}

	/**
	 * GestureLockView的当前状态
	 */
	private Mode mCurrentStatus = Mode.STATUS_NO_FINGER;

	/**
	 * 宽度
	 */
	private int mWidth;
	/**
	 * 高度
	 */
	private int mHeight;
	/**
	 * 外圆半径
	 */
	private int mRadius;

	/**
	 * 圆心坐标
	 */
	private int mCenterX;
	private int mCenterY;
	private Paint mPaint;


	private int mArrowDegree = -1;

	//画线工具
	private Path mArrowPath;
	/**
	 * 内圆的半径 = mInnerCircleRadiusRate * mRadus
	 *
	 */
	private float mInnerCircleRadiusRate = 0.3F;

	/**
	 * 四个颜色，可由用户自定义，初始化时由GestureLockViewGroup传入
	 */
	private int mColorCustom;//普通状态下颜色
	private int mColorMove;//成功状态下颜色
	private int mColorError;//错误状态下颜色

	private float mLineCustomSize;//普通状态下线条的size
	private float mLineMoveSize;//手触摸后线条的size

	public GestureLockView(Context context , int mColorCustom , int mColorMove , int mColorError,float mLineCustomSize,float mLineMoveSize) {
		super(context);
		this.mColorCustom = mColorCustom;
		this.mColorMove = mColorMove;
		this.mColorError = mColorError;
		this.mLineCustomSize= ConvertUtil.dip2px(context,mLineCustomSize);
		this.mLineMoveSize=ConvertUtil.dip2px(context,mLineMoveSize);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mArrowPath = new Path();

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		mWidth = MeasureSpec.getSize(widthMeasureSpec);
		mHeight = MeasureSpec.getSize(heightMeasureSpec);

		// 取长和宽中的小值
		mWidth = mWidth < mHeight ? mWidth : mHeight;
		mCenterX = mCenterY = mWidth / 2;
		switch (mCurrentStatus){
			case STATUS_FINGER_ON:
			case STATUS_FINGER_UP_SUCCESS:
			case STATUS_FINGER_UP_ERROR:
				mRadius = (int)(mCenterX-mLineMoveSize/2);
				break;
			case STATUS_NO_FINGER:
				mRadius =(int)(mCenterX- mLineCustomSize/2);
				break;
		}


	}

	@Override
	protected void onDraw(Canvas canvas) {

		switch (mCurrentStatus) {
		case STATUS_FINGER_ON:
		case STATUS_FINGER_UP_SUCCESS:
			// 绘制外圆
			mPaint.setColor(mColorMove);
			mPaint.setStyle(Style.STROKE);
			mPaint.setStrokeWidth(mLineMoveSize);//加粗
			canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
			// 绘制内圆
			mPaint.setStyle(Style.FILL);
			canvas.drawCircle(mCenterX, mCenterY, mRadius * mInnerCircleRadiusRate, mPaint);
			drawArrow(canvas);
			break;

		case  STATUS_NO_FINGER://初始状态
			// 绘制外圆
			mPaint.setStyle(Style.STROKE);
			mPaint.setColor(mColorCustom);
			mPaint.setStrokeWidth(mLineCustomSize);
			canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
			break;
		case STATUS_FINGER_UP_ERROR://手势密码输入错误状态
			// 绘制外圆
			mPaint.setColor(mColorError);
			mPaint.setStyle(Style.STROKE);
			mPaint.setStrokeWidth(mLineMoveSize);
			canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
			// 绘制内圆
			mPaint.setStyle(Style.FILL);
			canvas.drawCircle(mCenterX, mCenterY, mRadius * mInnerCircleRadiusRate, mPaint);
			drawArrow(canvas);
			break;

		}

		// 绘制三角形，初始时是个默认箭头朝上的一个等腰三角形，用户绘制结束后，根据由两个GestureLockView决定需要旋转多少度
		mArrowPath.moveTo(mCenterX, mCenterY/3);//等腰三角形顶点
		mArrowPath.lineTo(mCenterX+(mRadius * mInnerCircleRadiusRate/2), mCenterY/2);//从顶点 到 x,y画一条
		mArrowPath.lineTo(mCenterX-(mRadius * mInnerCircleRadiusRate/2), mCenterY/2);//从顶点 到 x,y画一条
		mArrowPath.close();//将绘制的图形封闭
		mArrowPath.setFillType(Path.FillType.WINDING);//填充三角形内部

	}

	/**
	 * 绘制箭头
	 * @param canvas
	 */
	private void drawArrow(Canvas canvas) {
		if (mArrowDegree != -1) {
			mPaint.setStyle(Style.FILL);
			canvas.save();
			canvas.rotate(mArrowDegree, mCenterX, mCenterY);
			canvas.drawPath(mArrowPath, mPaint);
			canvas.restore();
		}

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

	public void setArrowDegree(int degree) {
		this.mArrowDegree = degree;
	}

	public int getArrowDegree() {
		return this.mArrowDegree;
	}
}
