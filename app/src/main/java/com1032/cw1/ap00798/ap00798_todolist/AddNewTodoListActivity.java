package com1032.cw1.ap00798.ap00798_todolist;

import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewTodoListActivity extends AppCompatActivity {

    private Button mDoneButton;
    private EditText mEditTextField;
    private TodoListManager todoListManagerInstance = TodoListManager.getManagerInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_todo_list);

        mDoneButton = (Button) this.findViewById(R.id.add_new_list_DoneButton);
        mEditTextField = (EditText) this.findViewById(R.id.add_new_list_EditText);

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String listName = mEditTextField.getText().toString();
                TodoListAdapter mRecyclerViewAdapter = todoListManagerInstance.getAdapter();
                TodoList existingTodoList = todoListManagerInstance.getTodoList(listName);

                // Let the user know they have to actually type something...

                /*
                 Snackbar was introduced in Marshmallow (API 23), so display this if the device supports it,
                 else just display a toast.
                  */
                if (listName.equals("")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // Will execute on devices running Marshmallow or newer
                        Snackbar.make(view, R.string.add_new_list_emptyEditText_snackbar, Snackbar.LENGTH_LONG).show();
                    } else {
                        // Will execute on devices running older versions than Marshmallow
                        Toast.makeText(AddNewTodoListActivity.this, R.string.add_new_list_emptyEditText_snackbar, Toast.LENGTH_SHORT).show();
                    }
                } else if (existingTodoList != null) {
                    // Check if a list already exists with the same name - if it's not null, it must already exist

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // Will execute on devices running Marshmallow or newer
                        Snackbar.make(view, R.string.add_new_list_nameTaken_snackbar, Snackbar.LENGTH_LONG).show();
                    } else {
                        // Will execute on devices running older versions than Marshmallow
                        Toast.makeText(AddNewTodoListActivity.this, R.string.add_new_list_nameTaken_snackbar, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Make a new list, with no items, save to DB
                    todoListManagerInstance.createNewTodoList(listName);
                    mRecyclerViewAdapter.notifyDataSetChanged();

                    finish();
                }


            }
        });

    }
}
