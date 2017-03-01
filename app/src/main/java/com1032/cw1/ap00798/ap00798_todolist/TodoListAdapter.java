package com1032.cw1.ap00798.ap00798_todolist;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {

    private List<TodoList> todoLists;
    private Context context;

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

    public TodoListAdapter(Context context, List<TodoList> todoLists) {
        this.todoLists = todoLists;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(TodoListAdapter.ViewHolder viewHolder, int position) {
        TodoList todoListItem = this.todoLists.get(position);

        viewHolder.listName.setText(todoListItem.getTodoListName());

        // Grammar is important ;)
        if (todoListItem.getItemCount() >= 2) {
            // Use the getString method from the context to allow for a substitution to be made in the string
            viewHolder.listItemCount.setText(this.context.getString(R.string.list_items_2_plus, todoListItem.getItemCount()));
        } else if (todoListItem.getItemCount() == 0) {
            viewHolder.listItemCount.setText(R.string.list_items_0);
        } else {
            viewHolder.listItemCount.setText(this.context.getString(R.string.list_items_1, todoListItem.getItemCount()));
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
