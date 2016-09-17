package zs.com.wuzhi.util;

import android.text.TextUtils;

/**
 * Created by zhangshuqing on 16/7/19.
 */
public class ConvertUtil {

    public static String getUserId(String imgurl){
        int index=imgurl.lastIndexOf("/")+1;
        String longId=imgurl.substring(index);
        return longId;
    }


    public static String getFileFormat(String fileName) {
        if (TextUtils.isEmpty(fileName))
            return "";

        int point = fileName.lastIndexOf('.');
        return fileName.substring(point+1);
    }

}
