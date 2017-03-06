package com1032.cw1.ap00798.ap00798_todolist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a Singleton class that keeps track of all of the separate TodoList items.
 * Never instantiate this explicitly, but get the single instance by calling getManagerInstance().
 */
public class TodoListManager implements Serializable {

    private static TodoListManager manager;
    private static List<TodoList> todoLists = new ArrayList<TodoList>();
    private static TodoListAdapter mRecyclerViewAdapter = null;

    private TodoListManager() {}

    public static TodoListManager getManagerInstance() {
        if (manager == null) {
            manager = new TodoListManager();
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

        mRecyclerViewAdapter.updateDataset(todoLists);
    }

    public void removeTodoList(String todoListName) {

        for (TodoList todoList : todoLists) {
            if (todoList.getTodoListName().equals(todoListName)) {
                todoLists.remove(todoList);

                // Stop once we have removed this.
                break;
            }
        }

        if (mRecyclerViewAdapter == null) {
            throw new RuntimeException("Run setupTodoListAdapterForRecyclerView first!");
        }

        mRecyclerViewAdapter.updateDataset(todoLists);
    }

    public void removeAllTodoLists() {
        // Easiest way?
        todoLists = new ArrayList<TodoList>();

        if (mRecyclerViewAdapter == null) {
            throw new RuntimeException("Run setupTodoListAdapterForRecyclerView first!");
        }

        mRecyclerViewAdapter.updateDataset(todoLists);
    }

    public TodoList getTodoList(String todoListName) {
        // O(n) time complexity for this...
        for (TodoList todoList : todoLists) {
            if (todoList.getTodoListName().equals(todoListName)) {
                return todoList;
            }
        }

        // If it's not in the list
        throw new IllegalArgumentException(String.format("Todo List %s not found", todoListName));
    }

    /**
     * Set the adapter for the recycler view. This keeps the todoLists list inside this class.
     * @param mContext - needed for the TodoListAdapter
     * @param recyclerView - needed for the TodoListAdapter
     * @return mRecyclerViewAdapter - the new TodoListAdapter that was created (so the activity can use it)
     */
    public static TodoListAdapter setupTodoListAdapterForRecyclerView(Context mContext, RecyclerView recyclerView) {

        mRecyclerViewAdapter = new TodoListAdapter(mContext, todoLists);
        recyclerView.setAdapter(mRecyclerViewAdapter);

        return mRecyclerViewAdapter;
    }

}
