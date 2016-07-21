package zs.com.wuzhi.bean;

import java.io.Serializable;

/**
 * Created by zhangshuqing on 16/7/18.
 */
public class Item implements Serializable {
    private String tag;
    private String url;

    public Item(String url, String tag) {
        this.tag = tag;
        this.url = url;
    }

    public Item() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
