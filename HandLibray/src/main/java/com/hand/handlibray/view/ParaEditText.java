package com.hand.handlibray.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by hand-hitech2 on 2018-05-02.
 */

public class ParaEditText extends MyEditText {
    public ParaEditText(Context paramContext) {
        super(paramContext);
    }

    public ParaEditText(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public ParaEditText(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text.toString().contains(".")) {
            if (text.length() - 1 - text.toString().indexOf(".") > 8) {
                text = text.toString().subSequence(0, text.toString().indexOf(".") + 9);
                this.setText(text);
                this.setSelection(text.length());
            }
        }

        if (text.toString().trim().substring(0).equals(".")) {
            text = "0" + text;
            this.setText(text);
            this.setSelection(2);
        }

        if (text.toString().startsWith("0") && text.toString().trim().length() > 1) {
            if (!text.toString().substring(1, 2).equals(".")) {
                this.setText(text.subSequence(0, 1));
                this.setSelection(1);
                return;
            }
        }
    }
}
