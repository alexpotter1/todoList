package com1032.cw1.ap00798.ap00798_todolist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TodoList implements Serializable {

    private String todoListName;
    private List<String> todoListItems;

    public TodoList(String name) {
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("TodoList name is empty");
        }

        this.todoListName = name;
        this.todoListItems = new ArrayList<String>();
    }

    public void addItemToList(String item) {
        if (item == null || item.equals("")) {
            throw new IllegalArgumentException("Todo List item is null or empty");
        }

        this.todoListItems.add(item);
    }

    public void removeItemFromList(String item) {
        if (item == null || item.equals("")) {
            throw new IllegalArgumentException("Todo list item to remove is null/empty");
        }

        if (this.todoListItems.contains(item)) {
            this.todoListItems.remove(item);
        } else {
            throw new IllegalArgumentException("Todo List does not contain item");
        }
    }

    public String getTodoListItem(int position) {
        return this.todoListItems.get(position);
    }

    public int getItemCount() {
        return this.todoListItems.size();
    }

    public String getTodoListName() {
        return this.todoListName;
    }


}
