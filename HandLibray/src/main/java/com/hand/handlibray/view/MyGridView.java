package com.hand.handlibray.view;

import android.widget.GridView;
/**
 * Created by wcf on 2018/3/8 0026.
 * describe:解决scrollview嵌套GridView显示不全
 */

public class MyGridView extends GridView {
	public MyGridView(android.content.Context context, android.util.AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 设置不滚动
	 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}