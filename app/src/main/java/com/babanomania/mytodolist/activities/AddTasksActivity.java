package com.babanomania.mytodolist.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.babanomania.mytodolist.R;
import com.babanomania.mytodolist.models.TaskBean;
import com.babanomania.mytodolist.persistance.DatabaseHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddTasksActivity extends AppCompatActivity {

    private Pattern pattern;
    private Matcher matcher;
    private static final String DATE_PATTERN = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        final EditText edTitle = (EditText) findViewById(R.id.newtitle);
        if( edTitle.getText() == null || edTitle.getText().toString() == null || edTitle.getText().toString().length() <= 0 ){
            edTitle.setError( "title is mandatory" );
        }

        final EditText edDesc = (EditText) findViewById(R.id.newdesc);
        if( edDesc.getText() == null || edDesc.getText().toString() == null || edDesc.getText().toString().length() <= 0 ){
            edDesc.setError( "description is mandatory" );
        }

        final EditText edDate = (EditText) findViewById(R.id.newDate);
        if( edDate.getText() == null || edDate.getText().toString() == null || edDate.getText().toString().length() <= 0 ){
            edDate.setError( "date is mandatory" );
        }

        pattern = Pattern.compile(DATE_PATTERN);
        matcher = pattern.matcher(edDate.getText().toString());
        if(!matcher.matches()){
            matcher.reset();
            edDate.setError( "date should be in format dd/mm/yyyy" );
        }

        Button btnSave = (Button) findViewById(R.id.save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText edTitle = (EditText) findViewById(R.id.newtitle);
                final EditText edDesc = (EditText) findViewById(R.id.newdesc);
                final EditText edDate = (EditText) findViewById(R.id.newDate);

                DatabaseHandler db = new DatabaseHandler(view.getContext());
                db.addTask(new TaskBean( edTitle.getText().toString(),
                                         edDesc.getText().toString() ,
                                         edDate.getText().toString() ));
                finish();

            }
        });


    }

}
