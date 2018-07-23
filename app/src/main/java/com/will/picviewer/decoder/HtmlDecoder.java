package com.will.picviewer.decoder;

import android.util.Log;

import com.will.picviewer.decoder.bean.PicObject;
import com.will.picviewer.decoder.bean.ArticleObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HtmlDecoder {
    private static HtmlDecoder mInstance;

    public static HtmlDecoder getInstance(){
        if(mInstance == null){
            synchronized (HtmlDecoder.class){
                if(mInstance == null){
                    mInstance = new HtmlDecoder();
                }
            }
        }
        return mInstance;
    }

    private HtmlDecoder(){

    }

    public List<ArticleObject> decodeTitleFromHtml(String listHtml){
        Document document = Jsoup.parse(listHtml);
        Elements items = document.select("table#ajaxtable").select("tr.tr3.t_one.tac");
        ArrayList<ArticleObject> list = new ArrayList<>();
        ArticleObject title;
        for (int i=0; i<items.size();i++){
            Element item = items.get(i);
            if(!item.toString().contains("P]")){
                continue;
            }

            title = new ArticleObject();

            Elements titleElement = item.select("h3").select("a");
            if(titleElement.size() > 0){
                Element a = titleElement.get(0);
                title.setTitle(a.text());
                title.setLink(a.attr("href"));
            }
            Elements authorElement = item.select("a.bl");
            if(authorElement.size() > 0){
                Element a = authorElement.get(0);
                title.setAuthor(a.text());
            }
            Elements timeElement = item.select("div.f12");
            if(timeElement.size() > 0){
                Element div = timeElement.get(0);
                String time = div.text();
                if(time.equals("Top-marks")){
                    title.setStickTop(true);
                }else{
                    title.setTime(time);
                }
            }
            list.add(title);
        }
        return list;
    }
    public List<PicObject> decodePicFormHtml(String html){
        Document document = Jsoup.parse(html);
        List<PicObject> list = new ArrayList<>();
        PicObject object;
        String title = document.body().getElementsByTag("h4").text();
        Elements picElements = document.select("div").select("input").select("[data-src]");
        Log.e("elements size",picElements.size()+"");
        for(Element item : picElements){
            object = new PicObject();
            object.setTitle(title);
            object.setLink(item.attr("data-src"));
            list.add(object);
        }
        return list;
    }
}
