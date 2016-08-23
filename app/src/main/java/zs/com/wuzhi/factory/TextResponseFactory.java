package zs.com.wuzhi.factory;

import android.content.Context;

import zs.com.wuzhi.bean.PageBean;
import zs.com.wuzhi.util.ResponseUtil;

/**
 * Created by zhangshuqing on 16/8/21.
 */
public class TextResponseFactory {

    public static TextResponseFactory getDefaultFactory(){
        return new TextResponseFactory();
    }

    public enum Type {
        DAY_DIARY;
    }

    public PageBean convertToPageBaen(String content, Type type, Context context){
        PageBean pageBean=null;
        switch (type){
            case DAY_DIARY:
                pageBean= ResponseUtil.getUserDiary(content,context);
                break;
        }
        return pageBean;
    }

}
