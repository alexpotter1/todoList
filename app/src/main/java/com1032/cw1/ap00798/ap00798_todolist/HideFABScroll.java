package com1032.cw1.ap00798.ap00798_todolist;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * This class handles the hiding of the FAB when the RecyclerView is scrolled.
 */
public class HideFABScroll extends FloatingActionButton.Behavior {
    // FloatingActionButton.Behavior requires context and attributeSet so pass to superclass constructor
    public HideFABScroll(Context context, AttributeSet attributeSet) {
        super();
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxUsed, int dyUsed, int dxUnused, int dyUnused) {
        // If the user has started scrolling down
        if (dyUsed > 0) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            int fab_marginBottom = layoutParams.bottomMargin;
            child.animate().translationY(child.getHeight() + fab_marginBottom).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        } else if (dyUsed < 0) {
            // User scrolling back up, so set the FAB to default position, back in view
            child.animate().translationY(0).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        }

        // Do nothing if user doesn't scroll
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }
}