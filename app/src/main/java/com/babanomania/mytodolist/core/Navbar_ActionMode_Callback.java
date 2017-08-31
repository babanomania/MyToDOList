package com.babanomania.mytodolist.core;

import android.content.Context;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.babanomania.mytodolist.R;
import com.babanomania.mytodolist.activities.MainActivity;
import com.babanomania.mytodolist.models.Task;

import java.util.List;

/**
 * Created by Shouvik on 27/08/2017.
 */

public class Navbar_ActionMode_Callback implements ActionMode.Callback {

    private Context context;
    private MainAdapter recyclerView_adapter;
    private List<Task> message_models;
    private boolean isListViewFragment;

    public Navbar_ActionMode_Callback(Context context, MainAdapter recyclerView_adapter,
                                      List<Task> message_models, boolean isListViewFragment) {
        this.context = context;
        this.recyclerView_adapter = recyclerView_adapter;
        this.message_models = message_models;
        this.isListViewFragment = isListViewFragment;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        recyclerView_adapter.selectAllTasks(context);
        MainActivity.setNullToActionMode();       //Set action mode null
    }
}