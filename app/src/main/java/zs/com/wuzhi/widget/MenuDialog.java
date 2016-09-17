package zs.com.wuzhi.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import zs.com.wuzhi.R;

/**
 * Created by zhangshuqing on 16/9/15.
 */
public class MenuDialog extends Dialog implements View.OnClickListener {


    private MenuDialog(Context context, boolean flag,
                       OnCancelListener listener) {
        super(context, flag, listener);
    }




    public MenuDialog(Context context) {
        this(context, R.style.dialog_bottom);
    }

    @SuppressLint("InflateParams")
    private MenuDialog(Context context, int defStyle) {
        super(context, defStyle);
        View view = getLayoutInflater().inflate(R.layout.menu_dialog, null);
        view.findViewById(R.id.menu1).setOnClickListener(this);
        view.findViewById(R.id.menu2).setOnClickListener(this);
        view.findViewById(R.id.menu3).setOnClickListener(this);
        super.setContentView(view);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setGravity(Gravity.BOTTOM);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);
    }

    public void setOnMenuClickListener(OnMenuClickListener lis) {
        mListener = lis;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onClick((TextView) v);
        }
    }


    public interface OnMenuClickListener {
        void onClick(TextView menuItem);
    }

    private OnMenuClickListener mListener;



}
