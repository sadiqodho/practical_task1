package com.practical.Task.entities;

import java.util.Date;

/**
 * Created by Abro on 11/30/2020.
 */
public class ObjectEntity implements Comparable<ObjectEntity>{
    private Integer id;
    private String  pictureUrl;
    private String title;
    private String description;
    private String webUrl;
    private Date date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(ObjectEntity o) {
        return  o.getDate().compareTo(getDate());
    }
}
