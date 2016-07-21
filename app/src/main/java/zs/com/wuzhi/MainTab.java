package zs.com.wuzhi;

import zs.com.wuzhi.fragment.FragmentAdd;
import zs.com.wuzhi.fragment.FragmentMe;
import zs.com.wuzhi.fragment.FragmentNow;

/**
 * Created by zhangshuqing on 16/7/18.
 */
public enum MainTab {

    NOW(1,"此刻",R.drawable.tab_icon_discover, FragmentNow.class),
    ADD(2,"日记",R.drawable.tab_icon_add, FragmentAdd.class),
    ME(3,"我的",R.drawable.tab_icon_me, FragmentMe.class);

    private int index;
    private String tag;
    private Class clazz;
    private int resId;



    MainTab(int index,String tag,int resId,Class clazz){
        this.index=index;
        this.tag=tag;
        this.clazz=clazz;
        this.resId=resId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
