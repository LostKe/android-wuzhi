package zs.com.wuzhi.bean;

import java.util.List;

/**
 * 每天的日记
 * Created by zhangshuqing on 16/8/17.
 */
public class DayDiary {
    //当前日期
    private String currentDay;

    //当前日期所包含的日记(一天之内可能写了多份日记)
    private List<Diary> diarys;

    public String getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(String currentDay) {
        this.currentDay = currentDay;
    }

    public List<Diary> getDiarys() {
        return diarys;
    }

    public void setDiarys(List<Diary> diarys) {
        this.diarys = diarys;
    }
}
