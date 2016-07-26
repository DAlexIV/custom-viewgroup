package ru.yandex.yamblz.ui.viewgroups;

import android.util.Log;

/**
 * Created by dalexiv on 7/22/16.
 */

public interface IRequestLayoutProfiler {
    int getMeasuredTimes();
    String getLayoutName();
    void doRequestLayout();
    default void printIntoLog() {
        Log.i(getLayoutName(), "Measured " + getMeasuredTimes());
    }

    default void printIntoLogOnRequestLayout() {
        Log.i(getLayoutName(), "Measured with requestLayout " + getMeasuredTimes());
    }
}
