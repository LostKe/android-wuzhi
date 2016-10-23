package zs.com.wuzhi.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import zs.com.wuzhi.R;

/**
 * 自定义view-顶部菜单栏
 * Created by zhangshuqing on 16/10/21.
 */
public class Topbar extends RelativeLayout {
    @android.support.annotation.IdRes
    public static final int LEFT_TEXT = 1;

    @android.support.annotation.IdRes
    public static final int LEFT_IMG = 2;

    @android.support.annotation.IdRes
    public static final int RIGHT_TEXT = 3;

    @android.support.annotation.IdRes
    public static final int RIGHT_IMG = 4;

    int mTextColor;
    int mTextSize;
    Drawable mLeftDrable;
    Drawable mRightDrable;

    String mLeftStr;
    String mRightStr;

    TextView mLeftTv;
    TextView mRightTv;



    ImageView leftImage;
    ImageView rightImage;


    public Topbar(Context context) {
        super(context);
    }

    public Topbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Topbar);
        mTextColor = array.getInt(R.styleable.Topbar_text_color, R.color.white);
        mTextSize = array.getInt(R.styleable.Topbar_text_size, 15);
        mLeftDrable = array.getDrawable(R.styleable.Topbar_left_button);
        mRightDrable = array.getDrawable(R.styleable.Topbar_right_button);
        mLeftStr = array.getString(R.styleable.Topbar_left_text);
        mRightStr = array.getString(R.styleable.Topbar_right_text);
        array.recycle();

        mLeftTv = new TextView(context);
        mLeftTv.setText(mLeftStr);
        mLeftTv.setTextColor(mTextColor);
        mLeftTv.setTextSize(mTextSize);
        mLeftTv.setGravity(Gravity.CENTER);

        mRightTv = new TextView(context);
        mRightTv.setText(mRightStr);
        mRightTv.setTextColor(mTextColor);
        mRightTv.setTextSize(mTextSize);
        mRightTv.setGravity(Gravity.CENTER);



        leftImage = new ImageView(context);
        leftImage.setImageDrawable(mLeftDrable);
        leftImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onLeftButtonClick();
                }

            }
        });

        rightImage = new ImageView(context);
        rightImage.setImageDrawable(mRightDrable);
        rightImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRightButtonClick();
                }

            }
        });





        LayoutParams mleftButtonParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        mleftButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);

        leftImage.setId(LEFT_IMG);
        addView(leftImage, mleftButtonParams);


        LayoutParams mleftTvParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        mleftTvParams.addRule(RelativeLayout.RIGHT_OF, LEFT_IMG);
        addView(mLeftTv, mleftTvParams);


        LayoutParams mRightButtonParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        mRightButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        rightImage.setId(RIGHT_IMG);
        addView(rightImage, mRightButtonParams);
        //rightImage.setVisibility(View.INVISIBLE);


        LayoutParams mRightTvParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        mRightTvParams.addRule(RelativeLayout.LEFT_OF, RIGHT_IMG);
        addView(mRightTv, mRightTvParams);
    }

    public Topbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    private TopBarClickListener listener;

    public interface TopBarClickListener {
        void onLeftButtonClick();

        void onRightButtonClick();
    }


    public void setTopBarclickListener(TopBarClickListener listener) {
        this.listener = listener;
    }

    public void setVisible(int id, boolean visible) {
        int vis = visible ? View.VISIBLE : View.INVISIBLE;
        switch (id) {
            case LEFT_TEXT:
                mLeftTv.setVisibility(vis);
                break;
            case LEFT_IMG:
                leftImage.setVisibility(vis);
                break;
            case RIGHT_TEXT:
                mRightTv.setVisibility(vis);
                break;
            case RIGHT_IMG:
                rightImage.setVisibility(vis);
                break;
        }
    }
}
