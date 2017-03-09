package com1032.cw1.ap00798.ap00798_todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com1032.cw1.ap00798.ap00798_todolist.TodoList;
import com1032.cw1.ap00798.ap00798_todolist.TodoListManager;

/**
 * A Singleton class to handle saving and retrieving data from the SQLite database.
 */

public class DBManager {

    public static DBManager managerInstance;
    private static TodoReaderDBHelper dbHelper;

    private Gson gson;

    private DBManager(Context context) {
        dbHelper = new TodoReaderDBHelper(context);
        this.gson = new Gson();
    }

    public static DBManager getManagerInstance(Context context) {
        if (managerInstance == null) {
            managerInstance = new DBManager(context);
        }

        return managerInstance;
    }

    public SQLiteDatabase getDBWritable() {
        return dbHelper.getWritableDatabase();
    }

    public SQLiteDatabase getDBReadable() {
        return dbHelper.getReadableDatabase();
    }

    /**
     * This saves the object to JSON, and this can then
     * be saved in the database as a String.
     *
     * This method uses the google-gson library from https://github.com/google/gson
     * @param serializableObject - An object to be serialised. Must implement Serializable for safety.
     */
    public synchronized void putObject(Serializable serializableObject, String type) {
        // First, serialise object to JSON
        String json = gson.toJson(serializableObject);

        ContentValues newValues = new ContentValues();
        newValues.put(TodoReaderContract.TodoEntry.COLUMN_NAME_SERIALISED, json);
        newValues.put(TodoReaderContract.TodoEntry.COLUMN_NAME_SERIALISED_TYPE, type);

        this.getDBWritable().insert(TodoReaderContract.TodoEntry.TABLE_NAME, null, newValues);
    }

    /**
     * Get all of the TodoList objects from the DB.
     *
     * This method uses the google-gson library from https://github.com/google/gson
     * @return an iterator of all the objects.
     */
    public synchronized Iterator<Serializable> getObjects() {
        String[] projection = new String[] {TodoReaderContract.TodoEntry.COLUMN_NAME_SERIALISED, TodoReaderContract.TodoEntry.COLUMN_NAME_SERIALISED_TYPE};

        Cursor cursor = getDBReadable().query(TodoReaderContract.TodoEntry.TABLE_NAME, projection, null, null, null, null, null, null);

        ArrayList<Serializable> serialisedObjects = new ArrayList<Serializable>();

        while (cursor.moveToNext()) {
            String serialised = cursor.getString(cursor.getColumnIndexOrThrow(TodoReaderContract.TodoEntry.COLUMN_NAME_SERIALISED));
            String objectTypeString = cursor.getString(cursor.getColumnIndexOrThrow(TodoReaderContract.TodoEntry.COLUMN_NAME_SERIALISED_TYPE));
            Type objectType = this.getTypeFromDBSavedString(objectTypeString);

            serialisedObjects.add((Serializable) gson.fromJson(serialised, objectType));
        }

        return serialisedObjects.iterator();
    }

    /**
     * This only supports certain types, as this is only meant to be used with TodoList objects.
     * @return the associated type from a string.
     */
    private Type getTypeFromDBSavedString(String string) {
        switch (string) {
            case "TodoList":
                return TodoList.class;
            case "TodoListManager":
                return TodoListManager.class;
            default:
                return Serializable.class;
        }
    }





}
