package com1032.cw1.ap00798.ap00798_todolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AddNewTodoListActivity extends AppCompatActivity {

    private Button mDoneButton;
    private EditText mEditTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_todo_list);

        mDoneButton = (Button) this.findViewById(R.id.add_new_list_DoneButton);
        mEditTextField = (EditText) this.findViewById(R.id.add_new_list_EditText);

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Let the user know they have to actually type something...
                if (mEditTextField.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.add_new_list_emptyEditText_snackbar, Toast.LENGTH_SHORT).show();
                }



            }
        });

    }
}
