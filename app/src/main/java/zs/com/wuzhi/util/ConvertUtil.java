package zs.com.wuzhi.util;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by zhangshuqing on 16/7/19.
 */
public class ConvertUtil {

    public static String getUserId(String url){
        int index=url.lastIndexOf("/")+1;
        String longId=url.substring(index);
        return longId;
    }

    public static String convertUserId(String imgurl){
        int index=imgurl.lastIndexOf("/")+1;
        String longId=imgurl.substring(index);
        String[] array=longId.split("\\.");
        return array[0];
    }


    public static String getFileFormat(String fileName) {
        if (TextUtils.isEmpty(fileName))
            return "";

        int point = fileName.lastIndexOf('.');
        return fileName.substring(point+1);
    }


    public static  boolean compare(List<Integer> a, List<Integer> b) {
        if(a.size() != b.size()){
            return false;
        }

        for(int i=0;i<a.size();i++){
            if(!a.get(i).equals(b.get(i))){
                return false;
            }

        }
        return true;
    }

    public static  String listToString(List<Integer> list){
        StringBuilder sb=new StringBuilder();

        for (int i=0;i<list.size();i++){
            if(i==list.size()-1){
                sb.append(String.valueOf(list.get(i)));
            }else{
                sb.append(String.valueOf(list.get(i))+",");
            }

        }
        return sb.toString();
    }

    public static int[] stringToArray(String s){
        String[] arry=s.split(",");
        int[] result=new int[arry.length];
        for (int i=0;i<arry.length;i++){
            result[i]=Integer.valueOf(arry[i]);
        }
        return result;
    }

}
