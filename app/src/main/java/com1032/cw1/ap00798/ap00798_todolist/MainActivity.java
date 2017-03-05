package com1032.cw1.ap00798.ap00798_todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton mBigFab;
    private LinearLayout[] mFabSubmenuElements = new LinearLayout[2];

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private LinearLayoutManager mLLM;

    private TodoListManager todoListManagerInstance;

    private boolean isFabOpen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        todoListManagerInstance = TodoListManager.getManagerInstance();

        ViewGroup mView = (ViewGroup) this.findViewById(android.R.id.content);

        mBigFab = (FloatingActionButton) this.findViewById(R.id.fab);

        // Set up Recycler View and its layout manager, configure
        mRecyclerView = (RecyclerView) this.findViewById(R.id.todoListRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLLM = new LinearLayoutManager(this);
        mLLM.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLLM);


        getLayoutInflater().inflate(R.layout.layout_fab, mView, true);

        mFabSubmenuElements[0] = (LinearLayout) findViewById(R.id.fabAddTaskItem);
        mFabSubmenuElements[1] = (LinearLayout) findViewById(R.id.fabDeleteAllItem);

        // Add task button
        mFabSubmenuElements[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNewTodoListActivity.class);
                startActivity(intent);
            }
        });

        // Delete All button
        mFabSubmenuElements[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.delete_tasks);
                builder.setMessage(R.string.delete_tasks_info);
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            // Delete all tasks, update adapter for new data
                            todoListManagerInstance.removeAllTodoLists();
                            mRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    });
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            // Dismiss dialog without doing anything else
                            dialogInterface.cancel();
                        }
                    });
                builder.setIcon(R.drawable.ic_delete_black_24dp);

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        mBigFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFabOpen) {
                    closeFabSubmenu();
                } else {
                    openFabSubmenu();
                }
            }
        });

        // Set up Recycler View and its layout manager
        mRecyclerView = (RecyclerView) findViewById(R.id.todoListRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        // Set the adapter for the RecyclerView (via the manager)
        mRecyclerViewAdapter = TodoListManager.setupTodoListAdapterForRecyclerView(this, mRecyclerView);

        this.closeFabSubmenu();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openFabSubmenu() {
        for (LinearLayout fabSubmenuElement : this.mFabSubmenuElements) {
            fabSubmenuElement.setVisibility(View.VISIBLE);
        }
        Drawable bigFabCloseIcon = (Drawable) ContextCompat.getDrawable(this, R.drawable.ic_close_black_24dp);

        // Icon is black, so set it to white here (white = 0xffffffff)
        bigFabCloseIcon.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.SRC_ATOP);

        mBigFab.setImageDrawable(bigFabCloseIcon);
        isFabOpen = true;
    }

    private void closeFabSubmenu() {
        for (LinearLayout fabSubmenuElement : this.mFabSubmenuElements) {
            fabSubmenuElement.setVisibility(View.INVISIBLE);
        }
        Drawable bigFabMenuIcon = (Drawable) ContextCompat.getDrawable(this, R.drawable.ic_menu_black_24dp);

        // Again, this vector is black so set to white
        bigFabMenuIcon.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.SRC_ATOP);

        mBigFab.setImageDrawable(bigFabMenuIcon);
        isFabOpen = false;
    }
}
