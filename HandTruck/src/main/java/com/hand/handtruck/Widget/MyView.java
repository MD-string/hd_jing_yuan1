package com.hand.handtruck.Widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.hand.handtruck.R;
import com.hand.handtruck.utils.CommonUtils;

import java.util.ArrayList;

/*
 * 扇形比例图
 */
public class MyView extends View {
    private int[] mColors = {R.color.main_green, R.color.main_hui};
    private Paint paint;    //画笔
    private ArrayList<ViewData> viewDatas=new ArrayList<>();    //数据集
    private int w;          //View宽高
    private int h;
    private RectF rectF;    //矩形
    private Paint paint1;
    private RectF rectF1;
    private Paint paint2;
    private RectF rectF2;
    private float x;
    private float y;
    Context mContext;

    public MyView(Context context) {
        super(context);
        this.mContext=context;
        initPaint();    //设置画笔
    }

    //设置数据
    public void setData(ArrayList<ViewData> viewDatas) {
        this.viewDatas = viewDatas;
        initData();     //设置数据的百分度和角度
        invalidate();   //刷新UI
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        initPaint();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        initPaint();
    }

    //初始化画笔
    private void initPaint() {
        paint = new Paint();
        //设置画笔默认颜色
        paint.setColor(getResources().getColor(R.color.main_green));
        //设置画笔模式：填充
        paint.setStyle(Paint.Style.FILL);

        paint1 = new Paint();
        paint1.setStrokeWidth(CommonUtils.dip2px(mContext, 2f));
        paint1.setStyle(Paint.Style.STROKE);

        paint2 = new Paint();
        paint2.setStyle(Paint.Style.FILL);
        //
        paint.setTextSize(25f);
        //初始化区域
        rectF = new RectF();
        rectF1 = new RectF();
        rectF2 = new RectF();
    }

    //确定View大小
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;     //获取宽高
        this.h = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(w / 2, h / 2);             //将画布坐标原点移到中心位置
        float currentStartAngle = 0;                //起始角度
        float r = CommonUtils.dip2px(mContext, 70f);     //饼状图半径(取宽高里最小的值)
        float r1 =CommonUtils.dip2px(mContext, 70f);     //饼状图半径(取宽高里最小的值)
        float r2 =CommonUtils.dip2px(mContext, 70f);     //饼状图半径(取宽高里最小的值)
        rectF.set(-r, -r, r, r);                    //设置将要用来画扇形的矩形的轮廓

        rectF1.set(r1, r1, r1, r1);
        rectF2.set(-r2, -r2, -r2, r2);
        paint1.setColor(getResources().getColor(R.color.main_green));
        paint2.setColor(getResources().getColor(R.color.main_hui));

        if(viewDatas !=null && viewDatas.size() >0){
            canvas.drawArc(rectF1, currentStartAngle, viewDatas.get(0).angle, false, paint1);
            canvas.drawArc(rectF2, viewDatas.get(0).angle, viewDatas.get(1).angle, true, paint2);
            for (int i = 0; i < viewDatas.size(); i++) {
                ViewData viewData = viewDatas.get(i);
                paint.setColor(getResources().getColor(viewData.color));
                //绘制扇形(通过绘制圆弧)
                canvas.drawArc(rectF, currentStartAngle, viewData.angle, true, paint);
                //绘制扇形上文字
                float textAngle = currentStartAngle + viewData.angle / 2;    //计算文字位置角度
                paint.setColor(Color.BLACK);
                if(i==0){
                    x = (float) (r / 2 * Math.cos(textAngle * Math.PI /180));    //计算文字位置坐标
                    y = (float) (r / 2 * Math.sin(textAngle * Math.PI / 180));

                }else if(i==1){
                    x = (float) (r2 / 2 * Math.cos(textAngle * Math.PI / 180));    //计算文字位置坐标
                    y = (float) (r2 / 2 * Math.sin(textAngle * Math.PI /180));
                }
                paint.setColor(Color.WHITE);        //文字颜色
                canvas.drawText(viewData.name, x, y, paint);    //绘制文字

                currentStartAngle += viewData.angle;     //改变起始角度
            }
        }
    }

    private void initData() {
        if (null == viewDatas || viewDatas.size() == 0) {
            return;
        }
        float sumValue = 0;                 //数值和
        for (int i = 0; i < viewDatas.size(); i++) {
            ViewData viewData = viewDatas.get(i);
            sumValue += viewData.value;
            int j = i % mColors.length;     //设置颜色
            viewData.color = mColors[j];
        }

        for (ViewData data : viewDatas) {
            float percentage = data.value / sumValue;    //计算百分比
            float angle = percentage * 360;           //对应的角度
            data.percentage = percentage;
            data.angle = angle;
        }
    }
}