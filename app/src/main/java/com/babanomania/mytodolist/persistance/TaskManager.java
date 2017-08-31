package com.babanomania.mytodolist.persistance;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.babanomania.mytodolist.models.DaoMaster;
import com.babanomania.mytodolist.models.DaoSession;
import com.babanomania.mytodolist.models.Label;
import com.babanomania.mytodolist.models.LabelDao;
import com.babanomania.mytodolist.models.LabelTaskMap;
import com.babanomania.mytodolist.models.LabelTaskMapDao;
import com.babanomania.mytodolist.models.Task;
import com.babanomania.mytodolist.models.TaskDao;
import com.babanomania.mytodolist.util.DateUtil;
import com.babanomania.mytodolist.util.EntityUtil;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shouvik on 23/08/2017.
 */

public class TaskManager {

    private List<Task> taskList = new ArrayList<Task>();
    private static TaskManager instance = new TaskManager();
    private RecyclerView.Adapter mAdapter;

    private static List<Task> selectedTasks = new ArrayList<Task>();

    private DatabaseHandler dbHandler = new DatabaseHandler();

    public static enum FilterByCal {
        all,
        today,
        this_week,
        this_month;
    }

    public static TaskManager getInstance() {
        return instance;
    }

    public List<Task> getTaskList(Context context) {

        synchronized (this) {
            taskList.clear();
            taskList.addAll(dbHandler.getTasks(context));
        }

        return taskList;
    }

    public List<Task> filterData(Context context, FilterByCal filterType) {

        synchronized (this) {
            taskList.clear();

            if( filterType.equals( FilterByCal.all ) ) {
                taskList.addAll(dbHandler.getTasks(context));

            } else if( filterType.equals( FilterByCal.today ) ){
                taskList.addAll(dbHandler.getTasksByDate(
                                            context,
                                            DateUtil.getTodaysDate()
                                ));

            } else if( filterType.equals( FilterByCal.this_week ) ){
                taskList.addAll(dbHandler.getTasksByDate(
                                            context,
                                            DateUtil.getThisWeekendDate()
                                ));

            } else if( filterType.equals( FilterByCal.this_month ) ){
                taskList.addAll(dbHandler.getTasksByDate(
                                            context,
                                            DateUtil.getThisMonthEndDate()
                                ));
            }
        }

        return taskList;
    }

    public List<Task> getTaskListOnly(Context context) {
        return taskList;
    }

    public void addTask(Task task, List<Label> labels, Context context){

        taskList.add(task);
        dbHandler.addTask( task, labels, context );

        if( mAdapter != null ){
            mAdapter.notifyItemInserted( taskList.size() - 1 );
        }
    }

    public void addTask(Context context, Map<Task, List<Label>>  tasks){

        taskList.addAll(tasks.keySet());
        dbHandler.addTask( context, tasks );

        if( mAdapter != null ){
            mAdapter.notifyDataSetChanged();
        }
    }

    public Task get(int position){
        return taskList.get(position);
    }

    public void deleteTask( int position, Context context){

        dbHandler.deleteTask( context, taskList.get(position) );
        taskList.remove(position);

        if( mAdapter != null ){
            mAdapter.notifyDataSetChanged();
        }

    }

    public void deleteAllTasks( Context context){

        taskList.clear();
        dbHandler.deleteAll(context);

        if( mAdapter != null ){
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setAdapter(RecyclerView.Adapter pAdapter) {
        this.mAdapter = pAdapter;
    }

    public void addSampleData(Context context) {

        Map<Task, List<Label>>  tasks = new HashMap<Task, List<Label>>();

        Task task1 = new Task("Create an todo app", EntityUtil.getDateFromString("17/08/2017") );
        List<Label> labels1 = new ArrayList<Label>();
        labels1.add(new Label("todo"));
        tasks.put(task1, labels1);

        Task task2 = new Task("Tell others to use this todo app", EntityUtil.getDateFromString("28/08/2017") );
        List<Label> labels2 = new ArrayList<Label>();
        labels2.add(new Label("todo"));
        tasks.put(task2, labels2);

        Task task3 = new Task("Make plans for a mind control device", EntityUtil.getDateFromString("01/01/2021") );
        List<Label> labels3 = EntityUtil.getLabelsFromString("EvilPlan, Future");
        tasks.put(task3, labels3);

        Task task4 = new Task("Create the mind control device", EntityUtil.getDateFromString("31/12/2021") );
        List<Label> labels4 = EntityUtil.getLabelsFromString("EvilPlan, Future");
        tasks.put(task4, labels4);

        Task task5 = new Task("Use the mind control device to rule the world", EntityUtil.getDateFromString("01/01/2022") );
        List<Label> labels5 = EntityUtil.getLabelsFromString("EvilPlan, RuleTheWorld");
        tasks.put(task5, labels5);

        addTask(context, tasks);
    }

    public void toggleSelection(int position){

        Task taskBeanSel = taskList.get(position);
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

        for( Task selTask : selectedTasks ) {
            dbHandler.deleteTask(context, selTask);
            taskList.remove(taskList.indexOf(selTask));
        }

        int selectedSize = selectedTasks.size();
        selectedTasks.clear();

        Toast.makeText( context, selectedSize + " item deleted.", Toast.LENGTH_SHORT).show();//Show Toast
    }

    public boolean isSelected( Task someBean ){
        return selectedTasks.contains(someBean);
    }

    public void clearSelection(){
        selectedTasks.clear();
    }

    public List<Label> getLabels(Context context) {
        return dbHandler.getLabels(context);
    }
}
