package com.babanomania.mytodolist.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.util.SparseBooleanArray;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.babanomania.mytodolist.persistance.DatabaseHandler;
import com.babanomania.mytodolist.persistance.TaskManager;
import com.babanomania.mytodolist.recycleViewUtil.ClickListener;
import com.babanomania.mytodolist.recycleViewUtil.DividerItemDecoration;
import com.babanomania.mytodolist.R;
import com.babanomania.mytodolist.recycleViewUtil.RecyclerTouchListener;
import com.babanomania.mytodolist.recycleViewUtil.TaskAdapter;
import com.babanomania.mytodolist.models.TaskBean;
import com.babanomania.mytodolist.recycleViewUtil.Toolbar_ActionMode_Callback;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static TaskManager taskManager = TaskManager.getInstance();
    private static ActionMode mActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_todo_list);

        mAdapter = new TaskAdapter(taskManager.getTaskList(getBaseContext()));
        taskManager.setAdapter(mAdapter);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {
//                TaskBean task = taskManager.get(position);
//                Toast.makeText(getApplicationContext(), task.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                if (mActionMode != null)
                    onListItemSelect(position);

            }

            @Override
            public void onLongClick(View view, int position) {
                //Select item on long click
                onListItemSelect(position);

                mAdapter.notifyDataSetChanged();
            }
        }));

        //mAdapter.notifyDataSetChanged();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddTasksActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_remove_all) {
            taskManager.deleteAllTasks(getBaseContext());
            return true;

        } else if ( id == R.id.action_add_test_data ) {

            taskManager.addSampleData(getBaseContext());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //List item select method
    private void onListItemSelect(int position) {

        taskManager.toggleSelection(position);//Toggle the selection
        boolean hasCheckedItems = taskManager.getSelectedCount() > 0;//Check if any items are already selected or not

        if (hasCheckedItems && mActionMode == null)
            // there are some selected items, start the actionMode
            mActionMode = ((AppCompatActivity) this)
                                .startSupportActionMode(
                                        new Toolbar_ActionMode_Callback(this, mAdapter,
                                                                        taskManager.getTaskListOnly(getBaseContext()), false)
                                );

        else if (!hasCheckedItems && mActionMode != null)
            // there no selected items, finish the actionMode
            mActionMode.finish();

        if (mActionMode != null) {
            //set action mode title on item selection
            mActionMode.setTitle(String.valueOf(taskManager.getSelectedCount()) + " selected");
        }


    }

    //Set action mode null after use
    public static void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
    }

    //Delete selected rows
    public static void deleteRows(Context context ) {

        taskManager.deleteSelectedRows(context);
        mActionMode.finish();//Finish action mode after use
    }

}
