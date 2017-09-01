package com.babanomania.mytodolist.core;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.babanomania.mytodolist.R;
import com.babanomania.mytodolist.activities.MainActivity;
import com.babanomania.mytodolist.models.Label;
import com.babanomania.mytodolist.models.Task;
import com.babanomania.mytodolist.persistance.TaskManager;
import com.babanomania.mytodolist.util.EntityUtil;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shouvik on 27/08/2017.
 */

public class NewTask_ActionMode_Callback implements ActionMode.Callback {

    private Context context;
    private AppCompatActivity mActivity;

    private static final String DATE_PATTERN = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";

    public NewTask_ActionMode_Callback(Context context, AppCompatActivity activity) {
        this.context = context;
        this.mActivity = activity;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_new_task, menu);         //Inflate the menu over action mode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save:

                if( !validateInput() ) break;

                final EditText edTitle = (EditText) mActivity.findViewById(R.id.newtitle);
                final EditText edLabels = (EditText) mActivity.findViewById(R.id.newlabels);
                final EditText edDate = (EditText) mActivity.findViewById(R.id.newDate);

                Task newTask = new Task(    edTitle.getText().toString(),
                                            EntityUtil.getDateFromString( edDate.getText().toString() ) );

                List<Label> labels = EntityUtil.getLabelsFromString(edLabels.getText().toString());

                TaskManager.getInstance().addTask( newTask, labels, context );

                mActivity.finish();                             //Finish action mode
                break;
        }
        return false;
    }

    public boolean validateInput(){

        final EditText edTitle = (EditText) mActivity.findViewById(R.id.newtitle);
        if( edTitle.getText() == null || edTitle.getText().toString() == null || edTitle.getText().toString().length() <= 0 ){
            edTitle.setError( "title is mandatory" );
            return false;
        }

        final EditText edDate = (EditText) mActivity.findViewById(R.id.newDate);
        if( edDate.getText() == null || edDate.getText().toString() == null || edDate.getText().toString().length() <= 0 ){
            edDate.setError( "target date is mandatory" );
            return false;
        }

        Pattern pattern = Pattern.compile(DATE_PATTERN);
        Matcher matcher = pattern.matcher(edDate.getText().toString());

        if(!matcher.matches()){
            matcher.reset();
            edDate.setError( "target date should be in format dd/mm/yyyy" );
            return false;
        }

        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mActivity.finish();
    }
}