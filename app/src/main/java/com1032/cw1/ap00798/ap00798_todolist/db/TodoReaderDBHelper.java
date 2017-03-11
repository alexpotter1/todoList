package com1032.cw1.ap00798.ap00798_todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoReaderDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TodoReader.db";

    private static final String DB_CREATE_ENTRIES = new StringBuffer()
            .append("CREATE TABLE IF NOT EXISTS ")
            .append(TodoReaderContract.TodoEntry.TABLE_NAME)
            .append(" (")
            .append(TodoReaderContract.TodoEntry.COLUMN_NAME_ID)
            .append(" INTEGER PRIMARY KEY,")
            .append(TodoReaderContract.TodoEntry.COLUMN_NAME_SERIALISED)
            .append(" TEXT,")
            .append(TodoReaderContract.TodoEntry.COLUMN_NAME_SERIALISED_TYPE)
            .append(" TEXT)")
            .toString();

    private static final String DB_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TodoReaderContract.TodoEntry.TABLE_NAME;

    public TodoReaderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DB_DELETE_ENTRIES);
        this.onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.onUpgrade(db, oldVersion, newVersion);
    }
}
