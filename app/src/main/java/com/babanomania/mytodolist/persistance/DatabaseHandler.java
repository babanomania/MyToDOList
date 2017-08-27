package com.babanomania.mytodolist.persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.babanomania.mytodolist.models.TaskBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shouvik on 23/08/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MyTODOListDB";
    private static final String TABLE_TASKS = "T_TD_TASKS";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESC = "desc";
    private static final String KEY_DATE = "date";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + " ("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_TITLE + " TEXT, "
                + KEY_DESC+ " TEXT, "
                + KEY_DATE + " TEXT )";

        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);

        // Create tables again
        onCreate(db);
    }

    public void addTask(TaskBean task) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, task.getTitle());
        values.put(KEY_DESC, task.getDescription());
        values.put(KEY_DATE, task.getDate());

        // Inserting Row
        db.insert(TABLE_TASKS, null, values);
        db.close(); // Closing database connection

    }

    public void addTask( List<TaskBean> taskList) {

        SQLiteDatabase db = this.getWritableDatabase();

        for (TaskBean task: taskList) {

            ContentValues values = new ContentValues();
            values.put(KEY_TITLE, task.getTitle());
            values.put(KEY_DESC, task.getDescription());
            values.put(KEY_DATE, task.getDate());

            // Inserting Row
            db.insert(TABLE_TASKS, null, values);

        }

        db.close(); // Closing database connection

    }

    public TaskBean getTask(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASKS,
                new String[] { KEY_ID, KEY_TITLE, KEY_DESC, KEY_DATE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        TaskBean task = new TaskBean(
                            Integer.parseInt(cursor.getString(0)),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3));

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

                TaskBean task = new TaskBean(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3));

                // Adding contact to list
                tasksList.add(task);

            } while (cursor.moveToNext());
        }

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
