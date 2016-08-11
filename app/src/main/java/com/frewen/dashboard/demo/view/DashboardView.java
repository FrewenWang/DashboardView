package com.frewen.dashboard.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.frewen.dashboard.demo.R;


public class DashboardView extends View {
    private static final String TAG = "MySportView";

    private int mInWeakCircleColor;
    private int mInNormalColor;
    private int mInHighCircleColor;

    private int mInCircleColor;

    private int mOutCircleColor;
    private int mTextSize;
    private int mTextColor;

    private int mTargetPercent;
    private Paint mPaint;
    private Paint mCirclePaint;
    private int mStartSweepValue;
    private float mCurrentAngle;
    private int mCurrentPercent;
    private Rect mBound;
    private String mTextInfo;
    private RectF mCircleRect;

    public DashboardView(Context context) {
        this(context, null);
    }

    public DashboardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.i(TAG, "Log: MySportView MySportView() Called");
        //获取我们自定义的样式属性
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MySportView, defStyleAttr, 0);

        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.MySportView_titleColor:
                    // 默认颜色设置为黑色
                    mTextColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.MySportView_titleSize:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    mTextSize = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.MySportView_outCircleColor:
                    // 默认颜色设置为黑色
                    mOutCircleColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.MySportView_inWeakColor:
                    // 默认颜色设置为黑色
                    mInWeakCircleColor = array.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.MySportView_inNormalColor:
                    // 默认颜色设置为黑色
                    mInNormalColor = array.getColor(attr, Color.YELLOW);
                    break;
                case R.styleable.MySportView_inHighColor:
                    // 默认颜色设置为黑色
                    mInHighCircleColor = array.getColor(attr, Color.RED);
                    break;
            }
        }
        array.recycle();
        initView();
    }

    private void initView() {
        Log.i(TAG, "Log: MySportView initView() Called");
        //创建画笔
        mPaint = new Paint();
        mCirclePaint = new Paint();
        //设置是否抗锯齿
        mPaint.setAntiAlias(true);
        //圆环开始角度 -90° 正北方向
        mStartSweepValue = -90;
        //当前角度
        mCurrentAngle = 0;
        //当前百分比
        mCurrentPercent = 0;
        mBound = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "Log: MySportView onDraw() Called");
        //设置外圆的颜色
        mPaint.setColor(mOutCircleColor);
        //设置外圆为空心
        mPaint.setStyle(Paint.Style.STROKE);

        //画外圆
        canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2, mPaint);

        mPaint.setColor(mTextColor);
        //设置字体大小
        mPaint.setTextSize(mTextSize);
        //获取字体的内容
        mTextInfo = String.valueOf(mCurrentPercent);
        //获取当前字体的宽高范围（字体内容，从第0个字符开始，结束，矩形范围）
        mPaint.getTextBounds(mTextInfo, 0, mTextInfo.length(), mBound);

        //绘制字体
        canvas.drawText(mTextInfo, getWidth() / 2 - mBound.width() / 2, getWidth() / 2 + mBound.height() / 2, mPaint);
        //再绘制分这个字符
        //设置字体大小
        mPaint.setTextSize(mTextSize / 3);
        //绘制字体
        canvas.drawText("分", getWidth() * 3 / 5, getWidth() / 3, mPaint);

        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        //设置圆弧的宽度
        mCirclePaint.setStrokeWidth(10);
        //设置圆弧的颜色
        switchInCircleColor();


        //圆弧范围
        mCircleRect = new RectF(20, 20, getWidth() - 20, getWidth() - 20);

        //绘制圆弧()
        canvas.drawArc(mCircleRect, mStartSweepValue, mCurrentAngle, false, mCirclePaint);

        //判断当前百分比是否小于设置目标的百分比
        if (mCurrentPercent < mTargetPercent && mCurrentAngle <= 360) {
            //当前百分比+1
            mCurrentPercent += 1;
            //当前角度+360
            mCurrentAngle += 3.6;
            //每100ms重画一次
            postInvalidateDelayed(100);
        }

    }

    private void switchInCircleColor() {

        if (mCurrentPercent >= 0 && mCurrentPercent <= 30) {
            mCirclePaint.setColor(mInWeakCircleColor);
        } else if (mCurrentPercent <= 60) {
            mCirclePaint.setColor(mInNormalColor);
        } else if (mCurrentPercent <= 100) {
            mCirclePaint.setColor(mInHighCircleColor);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, "Log: MySportView onMeasure() Called");

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        Log.d(TAG, "Log: MySportView onMeasure widthSize==" + widthSize);
        Log.d(TAG, "Log: MySportView onMeasure heightSize==" + heightSize);

        int width;
        int height;

        //如果布局里面设置的是固定值,这里取布局里面的固定值;如果设置的是match_parent,则取父布局的大小
        if (widthMode == MeasureSpec.EXACTLY) {

            width = widthSize;
        } else {
            //如果布局里面没有设置固定值,这里取字体的宽度
            width = widthSize * 1 / 2;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = heightSize * 1 / 2;
        }
        setMeasuredDimension(width, height);
    }

    public void setTargetPercent(int mTargetPercent) {
        //这是当前的百分比
        mCurrentPercent = 0;
        //当前的角度
        mCurrentAngle = 0;
        this.mTargetPercent = mTargetPercent;
        postInvalidate();
    }

    public int getTargetPercent() {
        return mTargetPercent;
    }

    public void increaseNumber(int size) {
        setOffsetsPercent(Math.abs(size));
    }

    public void decreaseNumber(int size) {
        setOffsetsPercent(-Math.abs(size));
    }


    private void setOffsetsPercent(int size) {
        Log.i(TAG, "Log: MySportView setOffsetsPercent===size===" + size);
        if ((mTargetPercent >= 100 && size > 0)) {
            Toast.makeText(getContext(), "已经达到100分", Toast.LENGTH_LONG).show();
        } else if (mTargetPercent <= 0 && size < 0) {
            Toast.makeText(getContext(), "已经达到0分", Toast.LENGTH_LONG).show();
        } else {
            mTargetPercent += size;
            postInvalidate();
        }
    }

}
