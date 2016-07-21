package zs.com.wuzhi.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 正方形组件
 *
 * Created by zhangshuqing on 16/7/18.
 */
public class SquareLayout extends RelativeLayout {
    public SquareLayout(Context context) {
        super(context);
    }

    public SquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * 控制高和宽相等
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childWith= getMeasuredWidth();
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWith, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
