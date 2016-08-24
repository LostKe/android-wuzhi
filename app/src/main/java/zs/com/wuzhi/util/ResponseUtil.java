package zs.com.wuzhi.util;

import android.content.Context;

import com.alibaba.fastjson.JSON;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import zs.com.wuzhi.bean.Diary;
import zs.com.wuzhi.bean.DiarySummary;
import zs.com.wuzhi.bean.PageBean;
import zs.com.wuzhi.bean.UserInfo;
import zs.com.wuzhi.db.DBHelper;

/**
 * Created by zhangshuqing on 16/8/13.
 */
public class ResponseUtil {

    private static final String  NEXT_PAGE="以前的";
    private static final String  LAST_PAGE="最近的";

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

    public static UserInfo getUserInfo(String content ) {
        UserInfo userInfo = new UserInfo();
        try {
            Document document = parseDocument(content);
            Elements elements_name = document.select("input[name=name]");
            String user_name=elements_name.get(0).attr("value");
            userInfo.setNickName(user_name);
            Elements elements_signature= document.select("input[name=signature]");
            String signature=elements_signature.get(0).attr("value");
            userInfo.setSignature(signature);
            Elements elements_url = document.select("div[class=header]");
            String url=elements_url.get(0).child(1).attr("href");
            userInfo.setMineUrl(url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }


    public static String getImgUrl(String content ) {
        String img_url = "";
        try {
            Document document = parseDocument(content);
            Elements elements = document.select("span[class=img_shadow]");
            Element img_element = elements.get(0).child(0);
            img_url = img_element.attr("src");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return img_url;
    }


    /**
     * 分析[我的日记]分页查询的结果
     *
     * 提取摘要，详细内容存入数据库
     *
     * @param content
     */
    public static PageBean getUserDiary(String content, Context context) {
        PageBean<DiarySummary> pageBean=new PageBean<DiarySummary>();
        List<DiarySummary> result=new ArrayList<DiarySummary>();
        String nextPageUrl="";
        String lastPageUrl="";
        try {
            Document document = parseDocument(content);
            Elements page_index_elements=document.select("a[class=page_item]");
            if(page_index_elements!=null){//存在分页的情况
                int page_index_size=page_index_elements.size();
                if(page_index_size==1){
                    //当前页要么是第一页，要么是最后一页
                    String tag= page_index_elements.get(0).html();
                    String href=page_index_elements.get(0).attr("href");
                    if(tag.contains(LAST_PAGE)){
                        lastPageUrl=href;
                        nextPageUrl=Constant.PAGE_END;
                    }else if (tag.contains(NEXT_PAGE)){
                        nextPageUrl=href;
                        lastPageUrl=Constant.PAGE_START;

                    }
                }else if(page_index_size==2){
                    //当前页既不是第一页也不是最后一页
                    nextPageUrl=page_index_elements.get(0).attr("href");
                    lastPageUrl=page_index_elements.get(1).attr("href");
                }
            }
            Elements index_day_elements = document.select("div[class=index_day]");


            for (Element e : index_day_elements) {

                DiarySummary diarySummary=new DiarySummary();
                //获取某一天的日期
                String currentDay = e.child(0).html();
                diarySummary.setCurrentDay(currentDay);
                Element days = e.select("div[class=days]").get(0);
                Elements diary_time_elements = days.children();
                int len = diary_time_elements.size();
                List<Diary> diaryList=new ArrayList<Diary>();
                for (int i = 0; i < len; i = (i + 2)) {
                    Diary diary=new Diary();
                    String time = diary_time_elements.get(i).html();
                    String diary_content = diary_time_elements.get(i + 1).html();
                    diary.setTime(time);
                    diary.setContent(diary_content);
                    diaryList.add(diary);
                }
                //dayDiary.setDiarys(diaryList);
                diarySummary.setSummary(diaryList.get(0).getContent());
                diarySummary.setTime(diaryList.get(0).getTime());
                diarySummary.setNextPage(nextPageUrl);
                //将日记数据存入数据库中  主键为 page+日期
                DBHelper db=new DBHelper(context);
                db.insertDiary(nextPageUrl+currentDay, JSON.toJSONString(diaryList));
                result.add(diarySummary);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        pageBean.setItems(result);
        pageBean.setNextPage(nextPageUrl);
        pageBean.setLastPage(lastPageUrl);
        return pageBean;
    }

}
