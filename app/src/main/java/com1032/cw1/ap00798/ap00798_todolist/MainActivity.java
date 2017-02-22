package com1032.cw1.ap00798.ap00798_todolist;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewGroupCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton bigFab;
    private LinearLayout fabSubmenuElement_add;

    private boolean isFabOpen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewGroup view = (ViewGroup) this.findViewById(android.R.id.content);

        bigFab = (FloatingActionButton) this.findViewById(R.id.fab);
        fabSubmenuElement_add = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_fab, view, true).findViewById(R.id.fabAddTaskItem);
        bigFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFabOpen) {
                    closeFabSubmenu();
                } else {
                    openFabSubmenu();
                }
            }
        });

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
        fabSubmenuElement_add.setVisibility(View.VISIBLE);
        Drawable bigFabCloseIcon = (Drawable) ContextCompat.getDrawable(this, R.drawable.ic_close_black_24dp);

        // Icon is black, so set it to white here (white = 0xffffffff)
        bigFabCloseIcon.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.SRC_ATOP);

        bigFab.setImageDrawable(bigFabCloseIcon);
        isFabOpen = true;
    }

    private void closeFabSubmenu() {
        fabSubmenuElement_add.setVisibility(View.INVISIBLE);
        Drawable bigFabMenuIcon = (Drawable) ContextCompat.getDrawable(this, R.drawable.ic_menu_black_24dp);

        // Again, this vector is black so set to white
        bigFabMenuIcon.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.SRC_ATOP);

        bigFab.setImageDrawable(bigFabMenuIcon);
        isFabOpen = false;
    }
}
