package zs.com.wuzhi.util;

import android.content.Context;
import android.net.ConnectivityManager;

import zs.com.wuzhi.application.AppApplication;

/**
 * Created by zhangshuqing on 16/8/13.
 */
public class TDevice {

    public static boolean hasInternet() {
        boolean flag;
        if (((ConnectivityManager) AppApplication.context().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null)
            flag = true;
        else
            flag = false;
        return flag;
    }
}
