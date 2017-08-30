package com.babanomania.mytodolist.persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.babanomania.mytodolist.models.TaskBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shouvik on 23/08/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MyTODOList";

    private static final String TABLE_TASKS = "T_TD_TASKS";
    private static final String TABLE_LABELS = "T_TD_LABELS";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DATE = "date";

    private static final String KEY_TASK_ID = "task_id";
    private static final String KEY_LABEL = "label";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_TITLE + " TEXT, "
                + KEY_DATE + " TEXT )";

        String CREATE_LABELS_TABLE = "CREATE TABLE " + TABLE_LABELS + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_TASK_ID + " INTEGER, "
                + KEY_LABEL + " TEXT )";

        Log.d("MYTODDOLIST", CREATE_TASKS_TABLE );
        Log.d("MYTODDOLIST", CREATE_LABELS_TABLE );
        db.execSQL(CREATE_TASKS_TABLE);
        db.execSQL(CREATE_LABELS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LABELS);

        // Create tables again
        onCreate(db);
    }

    public void addTask(TaskBean task) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, task.getTitle());
        values.put(KEY_DATE, task.getDate());

        // Inserting Row
        long taskId = db.insert(TABLE_TASKS, null, values);
        int iTaskId = Long.valueOf(taskId).intValue();

        List<String> labels = task.getLabels();
        List<Integer> labelsIds = addLabels(db, iTaskId, labels);

        db.close(); // Closing database connection

    }

    public void addTask( List<TaskBean> taskList) {

        SQLiteDatabase db = this.getWritableDatabase();

        for (TaskBean task: taskList) {

            ContentValues values = new ContentValues();
            values.put(KEY_TITLE, task.getTitle());
            values.put(KEY_DATE, task.getDate());

            // Inserting Row
            long taskId = db.insert(TABLE_TASKS, null, values);
            int iTaskId = Long.valueOf(taskId).intValue();

            List<String> labels = task.getLabels();
            List<Integer> labelsIds = addLabels(db, iTaskId, labels);
        }

        db.close(); // Closing database connection

    }

    public List<Integer> addLabels( SQLiteDatabase db, int taskId, List<String> pLabels ){

        List<Integer> labelIds = new ArrayList<Integer>();

        for (String label: pLabels) {
            ContentValues values = new ContentValues();
            values.put(KEY_TASK_ID, taskId);
            values.put(KEY_LABEL, label);

            long labelId = db.insert( TABLE_LABELS, null, values );
            labelIds.add( Long.valueOf(labelId).intValue() );
        }

        return labelIds;
    }

    public List<String> getLabels( SQLiteDatabase db, int taskId ){

        List<String> labels = new ArrayList<String>();

        Cursor cursorLabels = db.query(TABLE_LABELS,
                new String[] { KEY_LABEL }, KEY_TASK_ID + "=?",
                new String[] { String.valueOf(taskId) }, null, null, null, null);

        if (cursorLabels.moveToFirst()) {
            do {

                labels.add( cursorLabels.getString(0) );

            } while (cursorLabels.moveToNext());
        }

        cursorLabels.close();
        return labels;
    }

    public TaskBean getTask(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASKS,
                new String[] { KEY_ID, KEY_TITLE, KEY_DATE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        int taskId = Integer.parseInt(cursor.getString(0));
        String taskTitle = cursor.getString(1);
        String taskDate = cursor.getString(2);
        List<String> labels = getLabels( db, taskId );

        cursor.close();

        TaskBean task = new TaskBean( taskId, taskTitle, labels, taskDate);

        // return contact
        return task;
    }

    public List<TaskBean> getAllTasks() {
        List<TaskBean> tasksList = new ArrayList<TaskBean>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                int taskId = Integer.parseInt(cursor.getString(0));
                String taskTitle = cursor.getString(1);
                String taskDate = cursor.getString(2);
                List<String> labels = getLabels( db, taskId );

                TaskBean task = new TaskBean( taskId, taskTitle, labels, taskDate);

                // Adding contact to list
                tasksList.add(task);

            } while (cursor.moveToNext());
        }

        cursor.close();

        // return contact list
        return tasksList;
    }

    public void deleteTask(TaskBean task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_ID + " = ?",
                new String[] { String.valueOf(task.get_id()) });

        db.close();
    }

    public void deleteAllTasks(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, null, null);

        db.close();
    }

}
