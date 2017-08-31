package com.babanomania.mytodolist.persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.babanomania.mytodolist.models.DaoMaster;
import com.babanomania.mytodolist.models.DaoSession;
import com.babanomania.mytodolist.models.Label;
import com.babanomania.mytodolist.models.LabelDao;
import com.babanomania.mytodolist.models.LabelTaskMap;
import com.babanomania.mytodolist.models.LabelTaskMapDao;
import com.babanomania.mytodolist.models.Task;
import com.babanomania.mytodolist.models.TaskDao;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Shouvik on 23/08/2017.
 */

public class DatabaseHandler {

    private DaoSession getDaoSession(Context context){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "mytodo-db");
        Database db = helper.getWritableDb();
        return new DaoMaster(db).newSession();
    }

    public List<Task> getTasks(Context context){

        DaoSession daoSession = getDaoSession(context);
        TaskDao taskDao = daoSession.getTaskDao();
        return taskDao.queryBuilder()
                        .where(TaskDao.Properties.IsCompleted.eq(false))
                        .orderAsc(TaskDao.Properties.Date)
                        .list();
    }

    public List<Task> getTasksByDate(Context context, Date pDate){

        DaoSession daoSession = getDaoSession(context);
        TaskDao taskDao = daoSession.getTaskDao();
        return taskDao.queryBuilder()
                .where( TaskDao.Properties.Date.le(
                        pDate
                ))
                .where(TaskDao.Properties.IsCompleted.eq(false))
                .orderAsc(TaskDao.Properties.Date)
                .list();
    }

    public List<Task> getTasksCompleted(Context context){

        DaoSession daoSession = getDaoSession(context);
        TaskDao taskDao = daoSession.getTaskDao();
        return taskDao.queryBuilder()
                .where( TaskDao.Properties.IsCompleted.eq(true))
                .orderAsc(TaskDao.Properties.Date)
                .list();
    }

    public List<Task> getTasksByLabel(Context context, String pLabel){

        DaoSession daoSession = getDaoSession(context);
        LabelDao labelDao = daoSession.getLabelDao();
        List<Label> labels = labelDao.queryBuilder()
                                        .where(LabelDao.Properties.Label.eq(
                                            pLabel
                                        ))
                                        .list();

        if( labels.isEmpty() ) return new ArrayList<Task>();

        List<Long> labelsIds = new ArrayList<Long>();
        for(Label eachLabel : labels ){
            labelsIds.add(eachLabel.get_id());
        }

        LabelTaskMapDao lblTskMapDao  = daoSession.getLabelTaskMapDao();
        List<LabelTaskMap> lbkTaskMaps = lblTskMapDao.queryBuilder()
                                                        .where(LabelTaskMapDao.Properties.LabelId.in(
                                                                labelsIds
                                                        ))
                                                        .list();
        if( lbkTaskMaps.isEmpty() ) return new ArrayList<Task>();

        List<Long> taskIds = new ArrayList<Long>();
        for(LabelTaskMap eachLabelTaskMap : lbkTaskMaps ){
            taskIds.add(eachLabelTaskMap.getTaskId());
        }

        TaskDao taskDao = daoSession.getTaskDao();
        return taskDao.queryBuilder()
                .where( TaskDao.Properties._id.in(
                        taskIds
                ))
                .where(TaskDao.Properties.IsCompleted.eq(false))
                .orderAsc(TaskDao.Properties.Date)
                .list();
    }

    public void addTask(Task task, List<Label> labels, Context context){

        DaoSession daoSession = getDaoSession(context);
        daoSession.getDatabase().beginTransaction();

        LabelDao labelDao = daoSession.getLabelDao();
        for( Label eachLabel : labels ) {
            labelDao.insertOrReplace(eachLabel);
        }

        TaskDao taskDao = daoSession.getTaskDao();
        taskDao.save(task);

        LabelTaskMapDao labelTaskMapDao = daoSession.getLabelTaskMapDao();
        List<LabelTaskMap> labelTaskMapList = new ArrayList<LabelTaskMap>();

        for( Label eachLabel : labels ){
            LabelTaskMap labelTaskMap = new LabelTaskMap();
            labelTaskMap.setTaskId( task.get_id() );
            labelTaskMap.setLabelId(eachLabel.get_id());
            labelTaskMapList.add(labelTaskMap);
            labelTaskMapDao.insert(labelTaskMap);
        }

        task.setCachedLabels( labels );

        daoSession.getDatabase().setTransactionSuccessful();
        daoSession.getDatabase().endTransaction();
    }

    public void addTask(Context context, Map<Task, List<Label>> tasks){

        DaoSession daoSession = getDaoSession(context);
        daoSession.getDatabase().beginTransaction();

        for ( Task task : tasks.keySet() ) {

            List<Label> labels = tasks.get(task);

            LabelDao labelDao = daoSession.getLabelDao();
            for (Label eachLabel : labels) {
                labelDao.insertOrReplace(eachLabel);
            }

            TaskDao taskDao = daoSession.getTaskDao();
            taskDao.save(task);

            LabelTaskMapDao labelTaskMapDao = daoSession.getLabelTaskMapDao();
            List<LabelTaskMap> labelTaskMapList = new ArrayList<LabelTaskMap>();

            for (Label eachLabel : labels) {
                LabelTaskMap labelTaskMap = new LabelTaskMap();
                labelTaskMap.setTaskId(task.get_id());
                labelTaskMap.setLabelId(eachLabel.get_id());
                labelTaskMapList.add(labelTaskMap);
                labelTaskMapDao.insert(labelTaskMap);
            }

            task.setCachedLabels( labels );
        }

        daoSession.getDatabase().setTransactionSuccessful();
        daoSession.getDatabase().endTransaction();
    }

    public void deleteTask( Context context, Task task ){
        DaoSession daoSession = getDaoSession(context);
        daoSession.getDatabase().beginTransaction();

        daoSession.getLabelTaskMapDao()
                    .queryBuilder()
                    .where(LabelTaskMapDao.Properties.TaskId.eq( task.get_id() ))
                    .buildDelete()
                    .executeDeleteWithoutDetachingEntities();

        daoSession.getTaskDao()
                    .delete(task);

        List<Long> allLabelIds = new ArrayList<Long>();
        for( LabelTaskMap lblTskMap : daoSession.getLabelTaskMapDao().queryBuilder().list() ){
            allLabelIds.add( lblTskMap.get_id() );
        }

        daoSession.getLabelDao()
                    .queryBuilder()
                    .where( LabelDao.Properties._id.notIn(
                            allLabelIds
                    ) )
                    .buildDelete()
                    .executeDeleteWithoutDetachingEntities();

        daoSession.getDatabase().setTransactionSuccessful();
        daoSession.getDatabase().endTransaction();
    }

    public void deleteAll(Context context){

        DaoSession daoSession = getDaoSession(context);
        daoSession.getDatabase().beginTransaction();
        daoSession.getLabelTaskMapDao().deleteAll();
        daoSession.getLabelDao().deleteAll();
        daoSession.getTaskDao().deleteAll();
        daoSession.getDatabase().setTransactionSuccessful();
        daoSession.getDatabase().endTransaction();
    }

    public Set<String> getLabels(Context context){

        DaoSession daoSession = getDaoSession(context);
        LabelDao labelDao = daoSession.getLabelDao();
        List<Label> allLabels =  labelDao.queryBuilder().list();

        Set<String> strLabels = new HashSet<String>();
        for( Label eachLabel : allLabels ){
            strLabels.add(eachLabel.getLabel().trim());
        }

        return strLabels;
    }

    public void updateTask(Context context, Task pTask){
        DaoSession daoSession = getDaoSession(context);
        TaskDao taskDao = daoSession.getTaskDao();
        taskDao.update(pTask);
    }
}
