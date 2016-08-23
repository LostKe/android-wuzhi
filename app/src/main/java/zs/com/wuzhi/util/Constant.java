package zs.com.wuzhi.util;

/**
 * Created by zhangshuqing on 16/7/30.
 */
public class Constant {



    public static final String SET_COOKIE="Set-Cookie";
    public static final String USER_NAME="user_name";
    public static final String PASS_WORD="pass_word";


    public static final String COOKIE="cookie";


    public static final String IMG_HEAD="https://wuzhi.me/small_avatar/%s.jpg";


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
    public static final String cookie_format="%s;_platform=mobile";

    /**
     * 主页
     */
    public static final String MAIN = "https://wuzhi.me";

    /**
     * 设置 昵称和签名  POST 请求
     */
    public static final String ACCOUNT_PROFILE = "https://wuzhi.me/account/profile";
    public static final String ACCOUNT_PROFILE_NAME = "name";
    public static final String ACCOUNT_PROFILE_SIGNATURE = "signature";


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
    public static final String NOTE_CRSF_TOKEN = "csrf_token";






    ////////////////////////
    public static final String USER_ID="user_id";
    public static final String NICKNAME="nickname";
    public static final String SIGNATURE="signature";
    public static final String TITLE="title";



    public static final String SUB_CLASS="sub_class";
    public static final String CONTENT="content";
    public static final String ACTION_TYPE="action_type";


    public static final String NEXT_ACTIVITY="next_activity";
    public static final String SETTING_BUNDLE="setting_bundle";


    public static final String PAGE_START="page_start";
    public static final String PAGE_END="page_end";
    public static final String DIARY_ID="diary_id";
    public static final String DIARY_CURRENT="diary_current";







}
