package com.hand.handtruck.Widget;
import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class MyXFormatter  implements IAxisValueFormatter {

    private String[] mValues;

    public MyXFormatter(String[] values) {
        this.mValues = values;
    }
    private static final String TAG = "MyXFormatter";

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        Log.d(TAG, "----->getFormattedValue: "+value);
        int index=(int)value;
;        if(0==index){
          return   mValues[0];
        }else if(1==index){
           return mValues[1];
        }else{
            return "";
        }
    }
}