package com1032.cw1.ap00798.ap00798_todolist.db;

import android.provider.BaseColumns;

public final class TodoReaderContract {

    private TodoReaderContract() {}

    public static class TodoEntry implements BaseColumns {
        public static final String TABLE_NAME = "todo_lists";
        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_SERIALISED = "TodoListSerialised";
        public static final String COLUMN_NAME_SERIALISED_TYPE = "TodoListObjectType";
    }

}
