package com.example.circleview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.circleview.R;

/**
 * Created by Shinelon on 2016/6/30.
 */
public class CustomProgressBar extends View {
    /**
     * 第一圈的颜色
     */
    protected int mFirstColor;
    /**
     * 第二圈的颜色
     */
    protected int mSecondColor;
    /**
     * 速度
     */
    protected int speed;
    /**
     * 圈宽度
     */
    protected int mCircleWidth;
    /**
     * 画笔
     */
    protected Paint paint;
    /**
     * 当前进度
     */
    private int mProgress;
    /**
     * 是否循环下一个
     */
    protected Boolean isNext = false;

    public CustomProgressBar(Context context) {
        this(context, null);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获得属性集合
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, defStyleAttr, 0);
        //获得集合的个数
        int count = ta.getIndexCount();
        //遍历集合
        for (int i = 0; i < count; i++) {
            //获得对应的属性
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.CustomProgressBar_firstColor:
                    mFirstColor = ta.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.CustomProgressBar_secondColor:
                    mSecondColor = ta.getColor(attr, Color.RED);
                    break;
                case R.styleable.CustomProgressBar_speed:
                    speed = ta.getInt(attr, 10);
                    break;
                case R.styleable.CustomProgressBar_circleWidth:
//                    speed=ta.getDimensionPixelSize(attr,20);
                    //转为像素值
                    mCircleWidth = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
                    break;
            }

        }
        ta.recycle();
        paint = new Paint();
        //绘图线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    mProgress++;
                    if (mProgress == 360) {
                        mProgress = 0;
                        if (!isNext)
                            isNext = true;
                        else
                            isNext = false;
                    }
                    postInvalidate();
                    try {
                        Thread.sleep(speed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

    @Override
    protected void onDraw(Canvas canvas)
    {

        int centre = getWidth() / 2; // 获取圆心的x坐标
        int radius = centre - mCircleWidth/2;// 半径
        paint.setStrokeWidth(mCircleWidth); // 设置圆环的宽度
        paint.setAntiAlias(true); // 消除锯齿
        paint.setStyle(Paint.Style.STROKE); // 设置空心
        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius); // 用于定义的圆弧的形状和大小的界限
        if (!isNext)
        {// 第一颜色的圈完整，第二颜色跑
            paint.setColor(mFirstColor); // 设置圆环的颜色
            canvas.drawCircle(centre, centre, radius, paint); // 画出圆环
            paint.setColor(mSecondColor); // 设置圆环的颜色
            canvas.drawArc(oval, -90, mProgress, false, paint); // 根据进度画圆弧
        } else
        {
            paint.setColor(mSecondColor); // 设置圆环的颜色
            canvas.drawCircle(centre, centre, radius, paint); // 画出圆环
            paint.setColor(mFirstColor); // 设置圆环的颜色
            canvas.drawArc(oval, -90, mProgress, false, paint); // 根据进度画圆弧
        }

    }
}
