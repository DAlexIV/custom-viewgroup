package ru.yandex.yamblz.ui.viewgroups;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dalexiv on 7/25/16.
 */

public class OneMeasurePassLayout extends ViewGroup {
    private Rect tempRectForLayouting = new Rect();
    private int widthSpaceTaken;
    private int largestHeight;

    public OneMeasurePassLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        widthSpaceTaken = 0;
        boolean hasMatchParentChild = false;
        int maxPermittedSize = MeasureSpec.getSize(widthMeasureSpec);
        int matchParentPlace = -1;

        // Iterate through all children, measuring them and computing our dimensions
        // from their size.
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                // Measure the child.
                int widthSpecs = MeasureSpec.makeMeasureSpec(maxPermittedSize - widthSpaceTaken,
                        MeasureSpec.AT_MOST);
                measureChild(child, widthSpecs, heightMeasureSpec);


                // Update our size information based on the layout params.
                final LayoutParams lp = child.getLayoutParams();
                if (lp.width == LayoutParams.WRAP_CONTENT)
                    widthSpaceTaken += child.getMeasuredWidth();
                else if (lp.width == LayoutParams.MATCH_PARENT) {
                    // Check if there were views with match_parent option
                    if (hasMatchParentChild) {
                        throw new IllegalStateException("Provide no more than one view with" +
                                " match_parent width in OneMeasurePassLayout");
                    } else {
                        hasMatchParentChild = true;
                    }
                } else widthSpaceTaken += lp.width;

                largestHeight = Math.max(child.getMeasuredHeight(), largestHeight);
            }
        }

        // Report our final dimensions.
        setMeasuredDimension(resolveSizeAndState(hasMatchParentChild
                        ? getMeasuredWidth() : widthSpaceTaken, widthMeasureSpec, 0),
                resolveSizeAndState(largestHeight, heightMeasureSpec, 0)

        );
    }


    public OneMeasurePassLayout(Context context, AttributeSet attrs) {
        super(context);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();

        // These are the far left and right edges in which we are performing layout.
        int leftPos = getPaddingLeft();
        int rightPos = right - left - getPaddingRight();
        int overallWidth = rightPos - leftPos;

        // These are current edges of the view
        int curLeft = leftPos;

        // These are the top and bottom edges in which we are performing layout.
        final int parentTop = getPaddingTop();
        final int parentBottom = bottom - top - getPaddingBottom();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = child.getLayoutParams();

                final int width = child.getMeasuredWidth();

                if (lp.width == LayoutParams.MATCH_PARENT) {
                    tempRectForLayouting.left = curLeft;
                    curLeft += overallWidth - widthSpaceTaken;
                    tempRectForLayouting.right = curLeft;
                } else {
                    tempRectForLayouting.left = curLeft;
                    curLeft += width;
                    tempRectForLayouting.right = curLeft;
                }

                // How to get margins?
                tempRectForLayouting.top = parentTop;
                tempRectForLayouting.bottom = parentBottom;

                // Place the child.
                child.layout(tempRectForLayouting.left, tempRectForLayouting.top,
                        tempRectForLayouting.right, tempRectForLayouting.bottom);
            }
        }
    }
}
