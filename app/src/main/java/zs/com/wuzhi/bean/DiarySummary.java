package zs.com.wuzhi.bean;

/**
 * 日记摘要
 * Created by zhangshuqing on 16/8/23.
 */
public class DiarySummary {

    /**
     * 当前日期
     */
    private String currentDay;
    /**
     * 摘要内容
     */
    private String summary;
    private String nextPage;
    /**
     * 摘要时间
     */
    private String time;

    public String getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(String currentDay) {
        this.currentDay = currentDay;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }


    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
