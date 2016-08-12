package zs.com.wuzhi.widget;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by zhangshuqing on 16/8/11.
 */
public class LoadingDialog extends Dialog {


    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
