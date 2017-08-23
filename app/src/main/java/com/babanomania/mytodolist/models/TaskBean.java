package com.babanomania.mytodolist.models;

/**
 * Created by Shouvik on 23/08/2017.
 */

public class TaskBean {

    private int _id;
    private String title, description, date;

    public TaskBean( String pTitle, String pDescription, String pDate ){
        this.title = pTitle;
        this.description = pDescription;
        this.date = pDate;
    }

    public TaskBean(int _id, String pTitle, String pDescription, String pDate ) {
        this._id = _id;
        this.title = pTitle;
        this.description = pDescription;
        this.date = pDate;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
