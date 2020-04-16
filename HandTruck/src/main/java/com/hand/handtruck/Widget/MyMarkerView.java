package com.hand.handtruck.Widget;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.hand.handtruck.R;

public class MyMarkerView extends MarkerView {
    private final TextView mSpeedTv;
    private TextView mContentTv;
    private String weight,mSpeed;
    private MPPointF mOffset;

    public MyMarkerView(Context context, int layoutResource,String wei,String speed) {
        super(context, layoutResource);
        mContentTv = (TextView) findViewById(R.id.tv_wei);
        mSpeedTv = (TextView) findViewById(R.id.tv_speed);
        weight=wei;
        mSpeed=speed;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        mContentTv.setText("重量:"+weight);
        mSpeedTv.setText("速度:"+mSpeed);
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