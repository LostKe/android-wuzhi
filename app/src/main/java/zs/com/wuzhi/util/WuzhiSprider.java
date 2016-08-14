package zs.com.wuzhi.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import zs.com.wuzhi.bean.Diary;
import zs.com.wuzhi.bean.Item;
import zs.com.wuzhi.bean.PersonDiary;

/**
 * 爬虫程序
 * Created by zhangshuqing on 16/7/19.
 */
public class WuzhiSprider {

    public static final String URL_NOW = "https://wuzhi.me/last";
    public static final String URL_DIARY = "https://wuzhi.me/u/%s";



    /**
     * 获取此刻页面中的图片链接
     *
     * @return
     */
    public static List<Item> getNowImg() {
        List<Item> urls = new ArrayList<Item>();
        try {
            Document document = Jsoup.connect(URL_NOW).cookie("_platform","mobile").get();
            document.outputSettings(new Document.OutputSettings().prettyPrint(false));
            Elements elements=document.select("table[class=user_list]");
            for (Element root : elements) {
                Elements a_elements=root.select("a[class=img_shadow]");
                for (Element aElement : a_elements) {
                    String href=aElement.attr("href");
                    String src=aElement.child(0).attr("src");
                    String tag=aElement.child(0).attr("alt");
                    urls.add(new Item(src,tag,href));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            ;

        }
        return urls;
    }

    /**
     * 根据用户id 获取用户日记
     *
     * @param id
     * @return
     */
    public static PersonDiary getPersonDiaryById(String id) {
        PersonDiary personDiary=new PersonDiary();
        try {
            String _url=String.format(URL_DIARY,id);
            Document document = Jsoup.connect(_url).cookie("_platform","mobile").get();
            document.outputSettings(new Document.OutputSettings().prettyPrint(false));
            Elements elements=document.select("div[class=date_line]");
            String date=elements.get(0).select("span[class=span-10]").get(0).html();
            date=date.replaceAll("&nbsp;","");
            personDiary.setCurrent(date);
            String flower=elements.get(0).select("span[class=span-3 last]").get(0).html();
            String flower_count=flower.split(">")[1];
            personDiary.setFlower(flower_count);
            Elements user_name_node=document.select("div[class=note_username]");
            String user_name=user_name_node.get(0).html();
            personDiary.setUserName(user_name);
            Elements note_each=document.select("div[class=note_each]");
            List<Diary> diaryList=new ArrayList<Diary>();
            for (Element element : note_each) {
                Diary diary=new Diary();
                String time=element.child(0).html();
                String content=element.child(1).html();
                diary.setContent(content);
                diary.setTime(time);
                diaryList.add(diary);
            }
            personDiary.setDiaryList(diaryList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return personDiary;
    }

}
