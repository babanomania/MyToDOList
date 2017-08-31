package com.babanomania.mytodolist.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.SubMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.babanomania.mytodolist.core.Navbar_ActionMode_Callback;
import com.babanomania.mytodolist.persistance.TaskManager;
import com.babanomania.mytodolist.core.ClickListener;
import com.babanomania.mytodolist.core.DividerItemDecoration;
import com.babanomania.mytodolist.R;
import com.babanomania.mytodolist.core.RecyclerTouchListener;
import com.babanomania.mytodolist.core.MainAdapter;
import com.babanomania.mytodolist.models.Task;
import com.babanomania.mytodolist.core.Toolbar_ActionMode_Callback;

import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static TaskManager taskManager = TaskManager.getInstance();
    private static ActionMode mActionMode;
    private static ActionMode mActionModeNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_todo_list);
        mAdapter = new MainAdapter(taskManager.getTaskList(getBaseContext()));

        taskManager.setAdapter(mAdapter);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {

                if( mActionMode == null ) {
                    Task task = taskManager.get(position);
                    Toast.makeText(getApplicationContext(), task.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                //Select item on long click
                onListItemSelect(position);

                mAdapter.notifyDataSetChanged();
            }
        }));

        //mAdapter.notifyDataSetChanged();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu navMenu = navigationView.getMenu();
        SubMenu smLabels = navMenu.addSubMenu("Labels");
        Set<String> allLabels = taskManager.getLabels(this);
        for (String aLabel : allLabels){
            smLabels.add(aLabel)
                    .setIcon( R.drawable.ic_menu_label );
        }
        smLabels.add("Create Label")
                .setIcon( R.drawable.ic_menu_plus );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewTaskActivity.class);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        if( ( mActionModeNav == null ) && ( mActionMode == null ) ){
            mActionModeNav.finish();
            mActionModeNav = null;
            taskManager.filterData(this, TaskManager.Filters.today, null);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        String titleActionMode = "";
        if (id == R.id.nav_today) {
            taskManager.filterData(this, TaskManager.Filters.today, null);
            titleActionMode = "Pending Today";

        } else if (id == R.id.nav_week) {
            taskManager.filterData(this, TaskManager.Filters.this_week, null);
            titleActionMode = "Pending This Week";

        } else if (id == R.id.nav_month) {
            taskManager.filterData(this, TaskManager.Filters.this_month, null);
            titleActionMode = "Pending This Month";

        } else {

            String menuTitle = item.getTitle().toString();
            if( "Create Label".equals(menuTitle) ){
                Toast.makeText(getApplicationContext(), "Label Karenge, Label Karenge", Toast.LENGTH_SHORT).show();

            } else {
                taskManager.filterData(this, TaskManager.Filters.LABELS, menuTitle.trim());
                titleActionMode = "#" + menuTitle.trim();
            }
        }

        if( mActionModeNav != null ) {
            mActionModeNav.finish();
            mActionModeNav = null;
        }

        mActionModeNav = ((AppCompatActivity) this)
                            .startSupportActionMode(
                                    new Navbar_ActionMode_Callback(this, mAdapter,
                                            taskManager.getTaskListOnly(getBaseContext()), false)
                            );

        mActionModeNav.setTitle(titleActionMode);

        mAdapter.notifyDataSetChanged();
        item.setChecked(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
