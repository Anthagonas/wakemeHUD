package com.example.anthagonas.wakemehud;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by vtrjd on 08/05/2017.
 */

public class ListViewNonScrollable extends ListView {
    public ListViewNonScrollable(Context context) {
        super(context);
    }
    public ListViewNonScrollable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ListViewNonScrollable(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMeasureSpec_custom = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }
}
