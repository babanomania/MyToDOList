package com.babanomania.mytodolist.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.babanomania.mytodolist.R;
import com.babanomania.mytodolist.core.NewTask_ActionMode_Callback;
import com.babanomania.mytodolist.core.Toolbar_ActionMode_Callback;
import com.babanomania.mytodolist.models.Label;
import com.babanomania.mytodolist.models.Task;
import com.babanomania.mytodolist.persistance.TaskManager;
import com.babanomania.mytodolist.util.EntityUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewTaskActivity extends AppCompatActivity {

    private static TaskManager taskManager = TaskManager.getInstance();

    private Context mContext;
    private static ActionMode mActionMode;

    private DatePickerDialog datePickerDialog;

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mContext = getBaseContext();

        setContentView(R.layout.activity_new_task);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        
        //Set Datepicker
        final EditText edDate = (EditText) findViewById(R.id.newDate);
        edDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        edDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) datePickerDialog.show();
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                                @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                            Calendar newDate = Calendar.getInstance();
                                                            newDate.set(year, monthOfYear, dayOfMonth);
                                                            edDate.setText(dateFormatter.format(newDate.getTime()));

                                    }

                                },

                                    newCalendar.get(Calendar.YEAR),
                                    newCalendar.get(Calendar.MONTH),
                                    newCalendar.get(Calendar.DAY_OF_MONTH)
                            );

        mActionMode = ((AppCompatActivity) this)
                            .startSupportActionMode(
                                    new NewTask_ActionMode_Callback(this, this)
                            );

        mActionMode.setTitle(R.string.title_activity_new_task);

    }

    @Override
    public void onBackPressed() {
       finish();
    }
}
