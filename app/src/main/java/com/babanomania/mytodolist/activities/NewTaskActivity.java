package com.babanomania.mytodolist.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.babanomania.mytodolist.R;
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

    private Pattern pattern;
    private Matcher matcher;
    private static final String DATE_PATTERN = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";
    private static TaskManager taskManager = TaskManager.getInstance();

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        final EditText edTitle = (EditText) findViewById(R.id.newtitle);
        if( edTitle.getText() == null || edTitle.getText().toString() == null || edTitle.getText().toString().length() <= 0 ){
            edTitle.setError( "title is mandatory" );
        }

        final EditText edLabels = (EditText) findViewById(R.id.newlabels);
        if( edLabels.getText() == null || edLabels.getText().toString() == null || edLabels.getText().toString().length() <= 0 ){
            edLabels.setError( "Labels are mandatory" );
        }

        final EditText edDate = (EditText) findViewById(R.id.newDate);
        if( edDate.getText() == null || edDate.getText().toString() == null || edDate.getText().toString().length() <= 0 ){
            edDate.setError( "target date is mandatory" );
        }

        pattern = Pattern.compile(DATE_PATTERN);
        matcher = pattern.matcher(edDate.getText().toString());
        if(!matcher.matches()){
            matcher.reset();
            edDate.setError( "target date should be in format dd/mm/yyyy" );
        }

        Button btnSave = (Button) findViewById(R.id.save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText edTitle = (EditText) findViewById(R.id.newtitle);
                final EditText edDate = (EditText) findViewById(R.id.newDate);
                Task newTask = new Task( edTitle.getText().toString(),
                                         EntityUtil.getDateFromString( edDate.getText().toString() ) );

                final EditText edLabel = (EditText) findViewById(R.id.newlabels);
                List<Label> labels = EntityUtil.getLabelsFromString(edLabel.getText().toString());

                taskManager.addTask( newTask, labels, view.getContext() );

                finish();

            }
        });

        edDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                datePickerDialog.show();
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

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

}
