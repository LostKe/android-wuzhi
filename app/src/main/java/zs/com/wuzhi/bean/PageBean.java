package zs.com.wuzhi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangshuqing on 16/8/21.
 */
public class PageBean<T> implements Serializable {

    private List<T> items;
    private String nextPage;
    private String lastPage;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public String getLastPage() {
        return lastPage;
    }

    public void setLastPage(String lastPage) {
        this.lastPage = lastPage;
    }
}
