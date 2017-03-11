package com1032.cw1.ap00798.ap00798_todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com1032.cw1.ap00798.ap00798_todolist.TodoList;

/**
 * A Singleton class to handle saving and retrieving data from the SQLite database.
 * The database connections aren't very optimised for performance.
 */

public class DBManager {

    private static DBManager managerInstance;
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

        Log.v("TEST1234", json);

        ContentValues newValues = new ContentValues();
        newValues.put(TodoReaderContract.TodoEntry.COLUMN_NAME_SERIALISED, json);
        newValues.put(TodoReaderContract.TodoEntry.COLUMN_NAME_SERIALISED_TYPE, type);

        Log.v("TEST1234", newValues.toString());

        // If the table was dropped by a Delete All action, recreate it by calling the DB Helper onCreate()
        dbHelper.onCreate(this.getDBWritable());

        this.getDBWritable().insertOrThrow(TodoReaderContract.TodoEntry.TABLE_NAME, "", newValues);
    }

    /**
     * Get all of the TodoList objects from the DB.
     *
     * This method uses the google-gson library from https://github.com/google/gson
     * @return an iterator of all the objects.
     */
    public synchronized Iterator<Serializable> getObjects() {

        /* If Delete All was invoked, this getObjects() method would fail (as the table was dropped),
           so call onCreate to make a new table if it doesn't exist.
         */
        dbHelper.onCreate(this.getDBWritable());

        String[] projection = new String[] {TodoReaderContract.TodoEntry.COLUMN_NAME_SERIALISED, TodoReaderContract.TodoEntry.COLUMN_NAME_SERIALISED_TYPE};

        Cursor cursor = getDBReadable().query(TodoReaderContract.TodoEntry.TABLE_NAME, projection, null, null, null, null, null, null);

        List<Serializable> serialisedObjects = new ArrayList<Serializable>();

        while (cursor.moveToNext()) {
            String serialised = cursor.getString(cursor.getColumnIndexOrThrow(TodoReaderContract.TodoEntry.COLUMN_NAME_SERIALISED));
            String objectTypeString = cursor.getString(cursor.getColumnIndexOrThrow(TodoReaderContract.TodoEntry.COLUMN_NAME_SERIALISED_TYPE));
            Type objectType = this.getTypeFromDBSavedString(objectTypeString);

            serialisedObjects.add((Serializable) gson.fromJson(serialised, objectType));
        }

        return serialisedObjects.iterator();
    }

    /**
     * This deletes a record from the database.
     * Used when user clicks the delete button on a todolist card.
     *
     * This method uses the google-gson library from https://github.com/google/gson
     * @param serializableObject - An object to be serialised. Must implement Serializable for safety.
     */
    public synchronized void deleteObject(Serializable serializableObject) {
        // Serialise to JSON to compare with JSON strings in DB
        String json = gson.toJson(serializableObject);

        String selection = TodoReaderContract.TodoEntry.COLUMN_NAME_SERIALISED + " LIKE ?";
        String[] preparedArgs = new String[] { json };

        this.getDBWritable().delete(TodoReaderContract.TodoEntry.TABLE_NAME, selection, preparedArgs);
    }

    /**
     * Deletes all of the DB records.
     * Call this when the user clicks on the 'Delete All' button.
     */
    public synchronized void deleteAllObjects() {
        // Get rid of everything in the table
        this.getDBWritable().execSQL("DROP TABLE " + TodoReaderContract.TodoEntry.TABLE_NAME);
    }

    /**
     * This only supports certain types, as this is only meant to be used with TodoList objects.
     * @return the associated type from a string.
     */
    private Type getTypeFromDBSavedString(String string) {
        switch (string) {
            case "TodoList":
                return TodoList.class;
            default:
                return Serializable.class;
        }
    }

    public void closeDBConnection() {
        dbHelper.close();
    }
}
