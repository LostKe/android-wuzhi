package zs.com.wuzhi.util;

/**
 * Created by zhangshuqing on 16/7/19.
 */
public class ConvertUtil {

    public static String getUserId(String imgurl){
        int index=imgurl.lastIndexOf("/")+1;
        String longId=imgurl.substring(index);
        return longId;
    }


    


}
