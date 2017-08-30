package com.babanomania.mytodolist.persistance;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.babanomania.mytodolist.models.TaskBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shouvik on 23/08/2017.
 */

public class TaskManager {

    private List<TaskBean> taskList = new ArrayList<TaskBean>();
    private static TaskManager instance = new TaskManager();
    private RecyclerView.Adapter mAdapter;

    private static List<TaskBean> selectedTasks = new ArrayList<TaskBean>();

    public static TaskManager getInstance() {
        return instance;
    }

    public List<TaskBean> getTaskList(Context context) {

        DatabaseHandler db = new DatabaseHandler(context);
        taskList.clear();
        taskList.addAll( db.getAllTasks() );

        return taskList;
    }

    public List<TaskBean> getTaskListOnly(Context context) {
        return taskList;
    }

    public void addTask(TaskBean task, Context context){

        taskList.add(task);

        DatabaseHandler db = new DatabaseHandler(context);
        db.addTask(task);

        if( mAdapter != null ){
            mAdapter.notifyItemInserted( taskList.size() - 1 );
           // mAdapter.notifyDataSetChanged();
        }
    }

    public void addTask(Context context, List<TaskBean> tasks){

        taskList.addAll(tasks);

        DatabaseHandler db = new DatabaseHandler(context);
        db.addTask(tasks);

        if( mAdapter != null ){
            mAdapter.notifyDataSetChanged();
        }
    }

    public TaskBean get(int position){
        return taskList.get(position);
    }

    public void deleteTask( int position, Context context){

        DatabaseHandler db = new DatabaseHandler(context);
        db.deleteTask( taskList.get(position) );
        taskList.remove(position);

        if( mAdapter != null ){
            mAdapter.notifyDataSetChanged();
        }

    }

    public void deleteAllTasks( Context context){

        DatabaseHandler db = new DatabaseHandler(context);
        db.deleteAllTasks();
        taskList.clear();

        if( mAdapter != null ){
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setAdapter(RecyclerView.Adapter pAdapter) {
        this.mAdapter = pAdapter;
    }

    public void addSampleData(Context context) {

        List<TaskBean> tasksList = new ArrayList<TaskBean>();

        tasksList.add(new TaskBean("Create an todo app", "ToDo", "17/08/2017"));
        tasksList.add(new TaskBean("Tell others to use this todo app", "ToDo", "28/08/2017"));
        tasksList.add(new TaskBean("Make plans for a mind control device", "EvilPlan, Future", "01/01/2021"));
        tasksList.add(new TaskBean("Create the mind control device", "EvilPlan, Future", "31/12/2021"));
        tasksList.add(new TaskBean("Use the mind control device to rule the world", "EvilPlan, RuleTheWorld", "01/01/2022"));

        addTask(context, tasksList);
    }

    public void toggleSelection(int position){

        TaskBean taskBeanSel = taskList.get(position);
        if( selectedTasks.contains(taskBeanSel) ){
            selectedTasks.remove( selectedTasks.indexOf(taskBeanSel) );
            return;

        } else {
            selectedTasks.add(taskBeanSel);
        }

    }

    public int getSelectedCount(){
        return selectedTasks.size();
    }

    public void deleteSelectedRows( Context context ){

        DatabaseHandler db = new DatabaseHandler(context);

        for( TaskBean selTask : selectedTasks ) {
            db.deleteTask(selTask);
            taskList.remove(taskList.indexOf(selTask));
        }

        int selectedSize = selectedTasks.size();
        selectedTasks.clear();

        Toast.makeText( context, selectedSize + " item deleted.", Toast.LENGTH_SHORT).show();//Show Toast
    }

    public boolean isSelected( TaskBean someBean ){
        return selectedTasks.contains(someBean);
    }

    public void clearSelection(){
        selectedTasks.clear();
    }

}
