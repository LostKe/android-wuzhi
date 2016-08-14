package zs.com.wuzhi.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cz.msebera.android.httpclient.Header;
import zs.com.wuzhi.bean.UserInfo;

/**
 * Created by zhangshuqing on 16/8/13.
 */
public class ResponseUtil {

    public static Document parseDocument(String html) {
        return Jsoup.parse(html);
    }

    public static String getLoginMessage(String content) {
        Document document = parseDocument(content);
        Elements elements = document.select("div[class=err]");
        return elements.get(0).html();
    }

    public static String getCookie(Header[] headers) {
        String result = "";
        for (int i = 0; i < headers.length; i++) {
            Header header = headers[i];
            String header_name = header.getName();
            if (Constant.SET_COOKIE.equalsIgnoreCase(header_name)) {
                result = header.getValue();
                String[] temp = result.split(";");
                result = temp[0];
                break;
            }
        }
        return result;
    }

    public static String getCsrfToken(String content) {
        Document document = parseDocument(content);
        Elements elements = document.select("input[name=csrf_token]");
        return elements.get(0).attr("value");
    }

    public static int getPrivacyStatu(String content) {
        int result = 1;
        try {
            Document document = parseDocument(content);
            Elements elements = document.select("option[selected]");
            String v = elements.get(0).attr("value");
            result = Integer.parseInt(v);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String getMineUrl(String content) {
        String result = "";
        try {
            Document document = parseDocument(content);
            Elements elements = document.select("div[class=header]");
            Element element = elements.get(0);
            String v = element.child(1).attr("href");
            return Constant.MAIN + v;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    public static UserInfo getUserInfo(String content) {
        UserInfo userInfo = new UserInfo();
        try {
            Document document = parseDocument(content);
            Elements elements = document.select("span[class=img_shadow]");
            Element img_element = elements.get(0).child(0);
            String img_url = img_element.attr("src");
            String nickname = img_element.attr("alt");
            userInfo.setImgUrl(img_url);
            userInfo.setNickName(nickname);
            Elements quote_element = document.select("span[class=quote_text]");
            String signature=quote_element.get(0).html();
            signature=signature.replaceAll("\"","");
            userInfo.setSignature(signature);
        } catch (Exception e) {
            e.printStackTrace();
            userInfo = null;
        }
        return userInfo;
    }
}
