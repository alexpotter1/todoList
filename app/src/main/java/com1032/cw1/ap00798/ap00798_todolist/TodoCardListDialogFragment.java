package com1032.cw1.ap00798.ap00798_todolist;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     TodoCardListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 * <p>You activity (or fragment) needs to implement {@link TodoCardListDialogFragment.Listener}.</p>
 */
public class TodoCardListDialogFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    private Listener mListener;
    private static TodoList parentList;
    private static int itemCount = 0;

    public static void setParentTodoList(TodoList todoList) {
        parentList = todoList;
    }

    // TODO: Customize parameters
    public static TodoCardListDialogFragment newInstance(int listItemCount) {
        itemCount = listItemCount;

        final TodoCardListDialogFragment fragment = new TodoCardListDialogFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_ITEM_COUNT, itemCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todocard_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new TodoCardAdapter(getArguments().getInt(ARG_ITEM_COUNT)));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        void onTodoCardClicked(int position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final CardView cv;
        final TextView taskName;
        final ImageButton taskDelete;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            // TODO: Customize the item layout
            super(inflater.inflate(R.layout.fragment_todocard_list_dialog_item, parent, false));
            cv = (CardView) itemView.findViewById(R.id.todoCardExpanded_item);
            taskName = (TextView) itemView.findViewById(R.id.todoListExpanded_taskName);
            taskDelete = (ImageButton) itemView.findViewById(R.id.todoListExpanded_delete);

            taskDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Delete Task");
                    builder.setMessage("Are you sure you want to delete the task?");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Remove task from parent list
                            parentList.removeItemFromList(taskName.getText().toString());
                            TodoCardAdapter adapter = new TodoCardAdapter(itemCount);
                            adapter.notifyDataSetChanged();

                            // Also update the parent list (show item count)
                            TodoListAdapter parentTodoListAdapter = TodoListManager.getManagerInstance(getContext()).getAdapter();
                            parentTodoListAdapter.notifyDataSetChanged();
                        }
                    });

                    builder.create().show();
                }
            });
        }
    }

    private class TodoCardAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final int mItemCount;

        TodoCardAdapter(int itemCount) {
            mItemCount = itemCount;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            String todoListExpandedItem = parentList.getTodoListItem(position);
            holder.taskName.setText(todoListExpandedItem);

        }

        @Override
        public int getItemCount() {
            return mItemCount;
        }

    }

}
