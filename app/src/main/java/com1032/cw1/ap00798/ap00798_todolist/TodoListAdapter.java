package com1032.cw1.ap00798.ap00798_todolist;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Build;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {

    private List<TodoList> todoLists;
    private Context context;
    private TodoListManager todoListManagerInstance;
    private BottomSheetBehavior bsb = null;

    // State of each CardView (and subsequent items) that is displayed in the Recycler View
    protected static class ViewHolder extends RecyclerView.ViewHolder {

        protected CardView cv;
        protected TextView listName;
        protected TextView listItemCount;
        protected ImageButton deleteTodoListButton;
        protected ImageButton addTaskToListButton;

        public ViewHolder(View view) {
            super(view);

            this.cv = (CardView) view.findViewById(R.id.todo_cardView);
            this.listName = (TextView) view.findViewById(R.id.todoCard_ListName);
            this.listItemCount = (TextView) view.findViewById(R.id.todoCard_ListItemCount);
            this.deleteTodoListButton = (ImageButton) view.findViewById(R.id.todoCard_Delete);
            this.addTaskToListButton = (ImageButton) view.findViewById(R.id.todoCard_addTask);
        }
    }

    public TodoListAdapter(Context context, List<TodoList> todoLists) {
        this.todoLists = todoLists;
        this.context = context;
        this.todoListManagerInstance = TodoListManager.getManagerInstance(context);
    }

    @Override
    public void onBindViewHolder(final TodoListAdapter.ViewHolder viewHolder, int position) {
        final TodoList todoList = this.todoLists.get(position);

        viewHolder.listName.setText(todoList.getTodoListName());

        // Set click listener for the delete button on the CardView
        viewHolder.deleteTodoListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Collapse the expanded info card, if it exists
                if (bsb != null) {
                    bsb.setState(BottomSheetBehavior.STATE_HIDDEN);
                }

                // Remove the todoList via its manager, this will update this adapter as well
                todoListManagerInstance.removeTodoList(viewHolder.listName.getText().toString());
                notifyDataSetChanged();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Will execute on devices running Marshmallow or newer
                    Snackbar.make(view, R.string.deleted_list_snackbar, Snackbar.LENGTH_SHORT).show();
                } else {
                    // Will execute on devices running older versions than Marshmallow
                    Toast.makeText(context, R.string.deleted_list_snackbar, Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewHolder.addTaskToListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final EditText taskNameInput = new EditText(context);
                taskNameInput.setHint(R.string.todoList_addTask_name);
                builder.setTitle(R.string.todoList_addTask_title);
                builder.setMessage(R.string.todoList_addTask_message);
                builder.setView(taskNameInput);
                builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!(taskNameInput.getText().toString().equals(""))) {
                            todoList.addItemToList(taskNameInput.getText().toString()); 
                        }
                    }
                });

                builder.create().show();
            }
        });

        // Set click listener for the actual todoList card
        viewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                // Only display expanded fragment if there's actually tasks in the list
                if (todoList.getItemCount() > 0) {
                    TodoCardListDialogFragment.setParentTodoList(todoList);
                    TodoCardListDialogFragment.newInstance(todoList.getItemCount()).show(activity.getSupportFragmentManager(), "expanded");
                }

            }
        });

        // Grammar is important ;)
        if (todoList.getItemCount() >= 2) {
            // Use the getString method from the context to allow for a substitution to be made in the string
            viewHolder.listItemCount.setText(this.context.getString(R.string.list_items_2_plus, todoList.getItemCount()));
        } else if (todoList.getItemCount() == 0) {
            viewHolder.listItemCount.setText(R.string.list_items_0);
        } else {
            viewHolder.listItemCount.setText(this.context.getString(R.string.list_items_1, todoList.getItemCount()));
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


    public void updateDataset(List<TodoList> newTodoList) {
        this.todoLists = newTodoList;
    }
}
