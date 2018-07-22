package com.will.picviewer.decoder.bean;

import java.io.Serializable;

public class TitleObject implements Serializable{

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
}
