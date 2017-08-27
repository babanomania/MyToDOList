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
//
//        DatabaseHandler db = new DatabaseHandler(context);
//        taskList.clear();
//        taskList.addAll( db.getAllTasks() );

        return taskList;
    }

    public List<TaskBean> getTaskListOnly(Context context) {
        return taskList;
    }

    public void addTask(TaskBean task, Context context){

        taskList.add(task);
//
//        DatabaseHandler db = new DatabaseHandler(context);
//        db.addTask(task);

        if( mAdapter != null ){
            mAdapter.notifyItemInserted( taskList.size() - 1 );
            //mAdapter.notifyDataSetChanged();
        }
    }

    public void addTask(Context context, List<TaskBean> tasks){

        taskList.addAll(tasks);
//
//        DatabaseHandler db = new DatabaseHandler(context);
//        db.addTask(taskList);

        if( mAdapter != null ){
            mAdapter.notifyDataSetChanged();
        }
    }

    public TaskBean get(int position){
        return taskList.get(position);
    }

    public void deleteTask( int position, Context context){
        taskList.remove(position);

        if( mAdapter != null ){
            mAdapter.notifyDataSetChanged();
        }

    }

    public void deleteAllTasks( Context context){
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

        tasksList.add(new TaskBean("Mad Max: Fury Road", "Action & Adventure", "2015"));
        tasksList.add(new TaskBean("Inside Out", "Animation, Kids & Family", "2015"));
        tasksList.add(new TaskBean("Star Wars: Episode VII - The Force Awakens", "Action", "2015"));
        tasksList.add(new TaskBean("Shaun the Sheep", "Animation", "2015"));
        tasksList.add(new TaskBean("The Martian", "Science Fiction & Fantasy", "2015"));
//        tasksList.add(new TaskBean("Mission: Impossible Rogue Nation", "Action", "2015"));
//        tasksList.add(new TaskBean("Up", "Animation", "2009"));
//        tasksList.add(new TaskBean("Star Trek", "Science Fiction", "2009"));
//        tasksList.add(new TaskBean("The LEGO Movie", "Animation", "2014"));
//        tasksList.add(new TaskBean("Iron Man", "Action & Adventure", "2008"));
//        tasksList.add(new TaskBean("Aliens", "Science Fiction", "1986"));
//        tasksList.add(new TaskBean("Chicken Run", "Animation", "2000"));
//        tasksList.add(new TaskBean("Back to the Future", "Science Fiction", "1985"));
//        tasksList.add(new TaskBean("Raiders of the Lost Ark", "Action & Adventure", "1981"));
//        tasksList.add(new TaskBean("Goldfinger", "Action & Adventure", "1965"));
//        tasksList.add(new TaskBean("Guardians of the Galaxy", "Science Fiction & Fantasy", "2014"));

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

        for( TaskBean selTask : selectedTasks )
            taskList.remove( taskList.indexOf(selTask) );

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
