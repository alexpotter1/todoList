package com1032.cw1.ap00798.ap00798_todolist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com1032.cw1.ap00798.ap00798_todolist.db.DBManager;

/**
 * This is a Singleton class that keeps track of all of the separate TodoList items.
 * Never instantiate this explicitly, but get the single instance by calling getManagerInstance().
 */
public class TodoListManager implements Serializable {

    private static TodoListManager manager;
    private List<TodoList> todoLists = new ArrayList<TodoList>();
    private TodoListAdapter mRecyclerViewAdapter = null;
    private DBManager dbManager;

    private TodoListManager(Context context) {
        this.dbManager = DBManager.getManagerInstance(context);
    }

    protected static TodoListManager getManagerInstance(Context context) {
        if (manager == null) {
            manager = new TodoListManager(context);
        }

        return manager;
    }

    public void createNewTodoList(String todoListName) {
        // Restrict TodoLists to only have unique names
        boolean nameAlreadyTaken = false;

        for (TodoList todoList : todoLists) {
            if (todoList.getTodoListName().equals(todoListName)) {
                nameAlreadyTaken = true;
            }
        }

        if (nameAlreadyTaken) {
            throw new IllegalArgumentException("Multiple todo lists with the same name are not allowed!");
        }

        // If the name is acceptable...
        todoLists.add(new TodoList(todoListName));

        if (mRecyclerViewAdapter == null) {
            throw new RuntimeException("Run setupTodoListAdapterForRecyclerView first!");
        }

        // Update the adapter so that it can show the new list
        mRecyclerViewAdapter.updateDataset(todoLists);

        // Handle DB sync
        // Args: (Serializable object, String objectType)
        this.dbManager.putObject(this.getTodoList(todoListName), "TodoList");
    }

    public void addTodoListFromDB(@NotNull TodoList list) {
        todoLists.add(list);
    }

    public void removeTodoList(String todoListName) {

        TodoList removed = null;

        for (TodoList todoList : todoLists) {
            if (todoList.getTodoListName().equals(todoListName)) {
                removed = todoList;
                todoLists.remove(todoList);

                // Stop once we have removed this.
                break;
            }
        }

        if (mRecyclerViewAdapter == null) {
            throw new RuntimeException("Run setupTodoListAdapterForRecyclerView first!");
        }

        mRecyclerViewAdapter.updateDataset(todoLists);

        // Handle DB sync
        if (removed != null) {
            this.dbManager.deleteObject(removed);
        }
    }

    public void removeAllTodoLists() {
        // Easiest way?
        todoLists = new ArrayList<TodoList>();

        if (mRecyclerViewAdapter == null) {
            throw new RuntimeException("Run setupTodoListAdapterForRecyclerView first!");
        }

        // Refresh the data set for the recycler view so that it can reflect changes
        mRecyclerViewAdapter.updateDataset(todoLists);

        // Handle DB sync
        this.dbManager.deleteAllObjects();
    }

    public TodoList getTodoList(String todoListName) {
        // O(n) time complexity for this...
        for (TodoList todoList : todoLists) {
            if (todoList.getTodoListName().equals(todoListName)) {
                return todoList;
            }
        }

        // If it's not in the list
        return null;
    }

    public TodoList getTodoList(int position) {
        return todoLists.get(position);
    }

    /**
     * Call this only when the application is going to close.
     */
    public void saveAllTodoLists() {
        for (TodoList list : this.todoLists) {
            dbManager.putObject(list, "TodoList");
        }

        dbManager.closeDBConnection();
    }

    /**
     * Obtain the adapter for the TodoList recycler view.
     * This may return null if the adapter has not been set (method below)
     * @return mRecyclerViewAdapter
     */
    protected TodoListAdapter getAdapter() {
        return this.mRecyclerViewAdapter;
    }

    /**
     * Set the adapter for the recycler view. This keeps the todoLists list inside this class.
     * @param mContext - needed for the TodoListAdapter
     * @param recyclerView - needed for the TodoListAdapter
     * @return mRecyclerViewAdapter - the new TodoListAdapter that was created (so the activity can use it)
     */
    protected TodoListAdapter setupTodoListAdapterForRecyclerView(Context mContext, @NotNull RecyclerView recyclerView) {

        mRecyclerViewAdapter = new TodoListAdapter(mContext, todoLists);
        recyclerView.setAdapter(mRecyclerViewAdapter);

        return mRecyclerViewAdapter;
    }

}
