package zs.com.wuzhi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangshuqing on 16/8/4.
 */
public class PersonDiary implements Serializable{
    private String current;
    private String userName;
    private String flower;

    private List<Diary> diaryList;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Diary> getDiaryList() {
        return diaryList;
    }

    public void setDiaryList(List<Diary> diaryList) {
        this.diaryList = diaryList;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getFlower() {
        return flower;
    }

    public void setFlower(String flower) {
        this.flower = flower;
    }
}
