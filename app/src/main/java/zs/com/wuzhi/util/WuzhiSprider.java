package zs.com.wuzhi.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import zs.com.wuzhi.bean.Item;

/**
 * 爬虫程序
 * Created by zhangshuqing on 16/7/19.
 */
public class WuzhiSprider {

    public static final String URL_NOW="https://wuzhi.me/last";



    /**
     * 获取此刻页面中的图片链接
     * @return
     */
    public static List<Item> getNowImg(){
        List<Item> urls=new ArrayList<Item>();
        try{
            Document document = Jsoup.connect(URL_NOW).get();
            Elements elements=document.select("table[class=user_list]");
            for (Element element: elements){
                Elements img_elements=element.select("img");
                for(Element img_node:img_elements){
                    String imgurl=img_node.attr("src");
                    String tag=img_node.attr("alt");
                    urls.add(new Item(imgurl,tag));
                }
            }
        }catch (Exception e){
            e.printStackTrace();;

        }
        return urls;

    }

}
