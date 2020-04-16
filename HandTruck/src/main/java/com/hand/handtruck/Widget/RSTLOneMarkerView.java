package com.hand.handtruck.Widget;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.hand.handtruck.R;

public class RSTLOneMarkerView extends MarkerView {
    private TextView mContentTv;
    private String weight;
    private MPPointF mOffset;

    public RSTLOneMarkerView(Context context, int layoutResource, String wei) {
        super(context, layoutResource);
        mContentTv = (TextView) findViewById(R.id.tv_wei);
        weight=wei;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        mContentTv.setText("速度:"+weight);
    }

    @Override
    public MPPointF getOffset() {
        if (mOffset == null) {
            mOffset = new MPPointF();
        }
        mOffset.x=-getWidth()/2;
        mOffset.y=-getHeight();
        return mOffset;
    }
}