package com.advance_native;

import android.app.Activity;
import android.view.View;

public class NavigationBarHider {
    private Activity mActivity;

    public NavigationBarHider(Activity activity) {
        mActivity = activity;
    }

    public void hideNavigationBar() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Hide the navigation bar
                View decorView = mActivity.getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
                decorView.setSystemUiVisibility(uiOptions);
            }
        });
    }
}
