package com.babanomania.mytodolist.util;

import android.util.Log;

import com.babanomania.mytodolist.models.Label;
import com.babanomania.mytodolist.models.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Shouvik on 31/08/2017.
 */

public class EntityUtil {

    private static DateFormat df = new SimpleDateFormat("dd/mm/yyyy");

    public static List<Label> getLabelsFromString( String pLabels){

        List<Label> allLabels = new ArrayList<Label>();

        if( pLabels.contains( "," ) ){

            String[] labels_all = pLabels.split(",");
            for (String eachLabel : labels_all){

                if( eachLabel != null ){
                    Label labelObj = new Label(eachLabel);
                    allLabels.add(labelObj);
                }

            }

        } else {
            Label labelObj = new Label(pLabels);
            allLabels.add(labelObj);
        }

        return allLabels;
    }

    public static String getHashedLabels( Task pTask ){

        StringBuffer hashedLabels = new StringBuffer();
        if( pTask.getCachedLabels() == null ){
            return null;

        } else {
            for (Label label : pTask.getCachedLabels()) {
                hashedLabels.append("#" + label.getLabel().trim() + " ");
            }
        }

        return hashedLabels.toString();
    }

    public static String getStringFromDate( Date pDate ) {
        try {
            return df.format(pDate);

        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static Date getDateFromString( String pDate ) {
        try {
            return df.parse(pDate);

        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

}
