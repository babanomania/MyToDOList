package com.babanomania.mytodolist.models;

/**
 * Created by Shouvik on 23/08/2017.
 */

public class TaskBean {

    private int _id;
    private String title, labels, date;

    public TaskBean( String pTitle, String pLabels, String pDate ){
        this.title = pTitle;
        setLabels(pLabels);
        this.date = pDate;
    }

    public TaskBean(int _id, String pTitle, String pLabels, String pDate ) {
        this._id = _id;
        this.title = pTitle;
        setLabels(pLabels);
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

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {

        StringBuffer allLabels = new StringBuffer();
        if( labels.contains( "," ) ){

            String[] labels_all = labels.split(",");
            for (String eachLabel : labels_all){

                if( eachLabel != null ){
                    allLabels.append( "#" + eachLabel.trim() + "  ");
                }

            }

        } else {
            allLabels.append( "#" + labels );
        }

        this.labels = allLabels.toString();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return this.title + " - " + this.getLabels() + " - " + this.getDate();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return toString().equals(obj.toString());
    }
}
