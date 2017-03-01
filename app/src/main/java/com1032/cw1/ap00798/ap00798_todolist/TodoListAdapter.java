package com1032.cw1.ap00798.ap00798_todolist;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {

    private List<TodoList> todoLists;

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        protected CardView cv;
        protected TextView listName;
        protected TextView listItemCount;

        public ViewHolder(View view) {
            super(view);

            this.cv = (CardView) view.findViewById(R.id.todo_cardView);
            this.listName = (TextView) view.findViewById(R.id.todoCard_ListName);
            this.listItemCount = (TextView) view.findViewById(R.id.todoCard_ListItemCount);
        }
    }

    public TodoListAdapter(List<TodoList> todoLists) {
        this.todoLists = todoLists;
    }

    @Override
    public void onBindViewHolder(TodoListAdapter.ViewHolder viewHolder, int position) {
        TodoList todoListItem = this.todoLists.get(position);

        viewHolder.listName.setText(todoListItem.getTodoListName());

        // Grammar is important ;)
        if (todoListItem.getItemCount() == 0 || todoListItem.getItemCount() > 1) {
            viewHolder.listItemCount.setText(todoListItem.getItemCount() + " items in list");
        } else {
            viewHolder.listItemCount.setText(todoListItem.getItemCount() + " item in list");
        }
    }

    @Override
    public TodoListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todo_list_card, viewGroup, false);
        return new TodoListAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return this.todoLists.size();
    }
}
