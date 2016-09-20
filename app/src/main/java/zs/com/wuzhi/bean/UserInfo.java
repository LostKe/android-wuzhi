package zs.com.wuzhi.bean;

/**
 * Created by zhangshuqing on 16/8/14.
 */
public class UserInfo {
    private String avatarUrl;
    private String mineUrl;
    private String nickName;
    private String signature;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getMineUrl() {
        return mineUrl;
    }

    public void setMineUrl(String mineUrl) {
        this.mineUrl = mineUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
