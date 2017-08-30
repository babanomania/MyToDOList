package com.babanomania.mytodolist.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shouvik on 23/08/2017.
 */

public class TaskBean {

    private int _id;
    private String title, date;
    private List<String> labels = new ArrayList<String>();

    public TaskBean( String pTitle, String pLabels, String pDate ){
        this.title = pTitle;
        setLabels(pLabels);
        this.date = pDate;
    }

    public TaskBean(int _id, String pTitle, List<String> pLabels, String pDate ) {
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

    public void setLabels(String pLabels) {

        StringBuffer allLabels = new StringBuffer();
        if( pLabels.contains( "," ) ){

            String[] labels_all = pLabels.split(",");
            for (String eachLabel : labels_all){

                if( eachLabel != null ){
                    labels.add(eachLabel.trim());
                }

            }

        } else {
            labels.add( pLabels );
        }

    }

    public List<String> getLabels() {
        return labels;
    }

    public String getHashedLabels(){

        StringBuffer hashedLabels = new StringBuffer();
        for( String label : labels ){
            hashedLabels.append( "#" + label + " " );
        }

        return hashedLabels.toString();
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
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
