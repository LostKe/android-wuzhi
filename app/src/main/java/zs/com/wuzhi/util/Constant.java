package zs.com.wuzhi.util;

/**
 * Created by zhangshuqing on 16/7/30.
 */
public class Constant {

    /**
     * 请求头
     */
    public static final String header="content-type:application/x-www-form-urlencoded";


    /**
     * 根据该字段取得response 的token
     */
    public static final String token_name="_token";

    /**
     * 通过token参数来组装
     */
    public static final String cookie_format="_token=%s;_platform=mobile";

    /**
     * 主页
     */
    public static final String MAIN = "https://wuzhi.me/";

    /**
     * 设置 昵称和签名  POST 请求
     */
    public static final String ACCOUNT_PROFILE = "https://wuzhi.me/account/profile";
    public static final String ACCOUNT_PROFILE_NAME = "name";
    public static final String account_profile_signature = "signature";


    /**
     * 设置隐私url
     */
    public static final String ACCOUNT_PRIVACY = "https://wuzhi.me/account/privacy";
    //0 大家可见，1.自己可见
    public static final String ACCOUNT_PRIVACY_TYPE = "type";
    public static final String ACCOUNT_PRIVACY_TYPE_ALL = "0";
    public static final String ACCOUNT_PRIVACY_TYPE_SELF = "1";


    /**
     * 登录 POST
     */
    public static final String LOGIN = "https://wuzhi.me/login/";
    public static final String LOGIN_EMAIL = "email";
    public static final String LOGIN_PASSOWRD = "password";

    /**
     * 写日记 POST
     */
    public static final String NOTE_ADD = "https://wuzhi.me/note/add";

    public static final String NOTE_ADD_CONTENT = "content";



    ////////////////////////
    public static final String TITLE="title";
    public static final String SUB_CLASS="sub_class";
    public static final String CONTENT="content";



}
