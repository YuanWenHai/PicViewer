package com.will.picviewer.decoder.bean;

import android.util.Log;

import java.io.Serializable;

public class ArticleObject implements Serializable{

    private static final String DIVIDER = "&";
    private String title = "null";
    private String link;
    private boolean isStickTop;
    private String time = "";
    private String author;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isStickTop() {
        return isStickTop;
    }

    public void setStickTop(boolean stickTop) {
        isStickTop = stickTop;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(getTitle()).append(DIVIDER)
                .append(getLink()).append(DIVIDER)
                .append(isStickTop()).append(DIVIDER)
                .append(getTime()).append(DIVIDER)
                .append(getAuthor());
        return builder.toString();
    }
    public static ArticleObject getArticleObjectFromString(String s){
        ArticleObject object = new ArticleObject();
        try{
            String[] attrs = s.split(DIVIDER);
            object.setTitle(attrs[0]);
            object.setLink(attrs[1]);
            object.setStickTop(Boolean.valueOf(attrs[2]));
            object.setTime(attrs[3]);
            object.setAuthor(attrs[4]);
        }catch ( Exception e){
            e.printStackTrace();
            Log.e("error on","getArticleFromString");
        }
       return object;
    }
}
