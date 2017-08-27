package com.babanomania.mytodolist.recycleViewUtil;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.babanomania.mytodolist.R;
import com.babanomania.mytodolist.activities.MainActivity;
import com.babanomania.mytodolist.models.TaskBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shouvik on 27/08/2017.
 */

public class Toolbar_ActionMode_Callback implements ActionMode.Callback {

    private Context context;
    private TaskAdapter recyclerView_adapter;
    private List<TaskBean> message_models;
    private boolean isListViewFragment;

    public Toolbar_ActionMode_Callback(Context context, TaskAdapter recyclerView_adapter,
                                       List<TaskBean> message_models, boolean isListViewFragment) {
        this.context = context;
        this.recyclerView_adapter = recyclerView_adapter;
        this.message_models = message_models;
        this.isListViewFragment = isListViewFragment;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_multi_select, menu);         //Inflate the menu over action mode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_delete:
                MainActivity.deleteRows( context );         //delete selected rows
                mode.finish();                             //Finish action mode

                break;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        //When action mode destroyed remove selected selections and set action mode to null
        //First check current fragment action mode
        recyclerView_adapter.removeSelection();         // remove selection
        MainActivity.setNullToActionMode();       //Set action mode null
    }
}