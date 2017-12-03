package zs.com.wuzhi.bean;

/**
 * Created by zhangshuqing on 2017/12/3.
 */
public class User {

    private String userName;
    private String phone;

    // 0:初始 1：成功  2:失败
    private int state;


    private boolean select=true;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
