package ru.yandex.yamblz.ui.viewgroups;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by dalexiv on 7/22/16.
 *
 * Just testing how many times onMeasure is being called
 */
public class LoggingRelativeLayout extends RelativeLayout implements IRequestLayoutProfiler {
    private static final String TAG = LoggingRelativeLayout.class.getSimpleName();
    private int numberOfMeasures = 0;
    private boolean isProfiling = false;

    public LoggingRelativeLayout(Context context) {
        super(context);
    }

    public LoggingRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ++numberOfMeasures;

        if (isProfiling) {
            printIntoLogOnRequestLayout();
        }
        else {
            printIntoLog();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public int getMeasuredTimes() {
        return numberOfMeasures;
    }

    @Override
    public void doRequestLayout() {
        numberOfMeasures = 0;
        isProfiling = true;
        requestLayout();
    }

    @Override
    public String getLayoutName() {
        return TAG;
    }
}
