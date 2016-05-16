package com.appheader.ui_demo.mine;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.appheader.ui_demo.R;

/**
 * 带动画圆形进度条
 * Created by mayu on 16/5/11,上午9:18.
 */
public class Circlebar extends View {
    private Canvas mCanvas;
    private Paint mPaint;
    //分段颜色
    private static final int[] SECTION_COLORS = {Color.parseColor("#49EAFF"), Color.parseColor("#03A9F4")};

    private int mDuration;              // 动画时间
    private int mCircleBgWidth;         // 圆环宽度
    private int mCircleBarWidth;        // 进度条宽度
    private int mColorBg;               // 圆环背景颜色
    private int mColorBar;              // 圆环进度颜色
    private int mColorText;             // 文字颜色
    private float mValue;               // 当前值
    private float mValueMax;            // 最大值

    private int mWidth;                 // 画布宽度
    private int mHeigth;                // 画布高度

    private float pressExtraStrokeWidth;
    private int mRadius;                // 圆环半径

    private RectF mColorWheelRectangle = new RectF();//定义一个矩形,包含矩形的四个单精度浮点坐标

    private int mPercent;               // 当前百分比
    private float mValueNow;            // 当前值
    private float mSweepAnglePer;       // 当前角度



    private BarAnimation anim;

    public Circlebar(Context context) {
        super(context);
        init(null, 0);
    }

    public Circlebar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public Circlebar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr){
        TypedArray a = this.getContext().obtainStyledAttributes(attrs,R.styleable.Circlebar);
        mDuration = a.getInt(R.styleable.Circlebar_circle_duration, 1000);
        mCircleBgWidth = a.getDimensionPixelSize(R.styleable.Circlebar_circle_bg_width, 24);
        mCircleBarWidth = a.getDimensionPixelSize(R.styleable.Circlebar_circle_bar_width, 24);
        mColorBg = a.getColor(R.styleable.Circlebar_color_bg, getResources().getColor(R.color.circlebar_gray));
        mColorBar = a.getColor(R.styleable.Circlebar_color_bar, getResources().getColor(R.color.cyan_500));
        mColorText = a.getColor(R.styleable.Circlebar_color_text, getResources().getColor(R.color.cyan_500));
        mValue = a.getFloat(R.styleable.Circlebar_value, 0);
        mValueMax = a.getFloat(R.styleable.Circlebar_max_value, 0);
        a.recycle();

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);//默认设置画笔为填充模式
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
        anim = new BarAnimation();
        updateValue(mValue);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mCanvas = canvas;
        drawCircleBg();
        drawCircleBar();
        drawText();
    }

    // 绘制背景
    private void drawCircleBg(){
        mPaint.setShader(null);
        mPaint.setColor(mColorBg);
        mPaint.setStrokeWidth(mCircleBgWidth);
        mRadius = mWidth / 2 - mCircleBgWidth;
        mCanvas.drawArc(mColorWheelRectangle, 0, 360, false, mPaint);
    }
    //绘制进度
    private void drawCircleBar(){
        mPaint.setColor(mColorBar);
        mPaint.setStrokeWidth(mCircleBarWidth);
        mRadius = (mWidth - mCircleBgWidth) / 2;
//        LinearGradient shader =new LinearGradient(mRadius*2, 0,mRadius, mRadius, SECTION_COLORS, null,
//                Shader.TileMode.CLAMP);
//        mPaint.setShader(shader);
        mCanvas.drawArc(mColorWheelRectangle, 270, mSweepAnglePer, false, mPaint);
    }
    // 绘制文字
    private void drawText(){
        mPaint.setShader(null);
        mPaint.setStrokeWidth(0);
        mPaint.setColor(mColorText);
        mPaint.setTextSize(mRadius / 2);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mPaint.setShadowLayer(2, 1, 1, Color.parseColor("#00CCFF"));// 设置阴影
        String text = mPercent + "%";
        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);
        mCanvas.drawText(text, mWidth / 2 - bounds.width() / 2, mHeigth/2 + bounds.height()/2, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeigth = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        mWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);

        int min = Math.min(mWidth, mHeigth);// 获取View最短边的长度
        setMeasuredDimension(min, min);// 强制改View为以最短边为长度的正方形
        pressExtraStrokeWidth = Textscale(2, min);// 圆弧离矩形的距离
        mColorWheelRectangle.set(mCircleBgWidth + pressExtraStrokeWidth,
                mCircleBgWidth + pressExtraStrokeWidth, min
                        - mCircleBgWidth/2 - pressExtraStrokeWidth, min
                        - mCircleBgWidth/2 - pressExtraStrokeWidth);// 设置矩形
    }

    /**
     * 根据控件的大小改变绝对位置的比例
     *
     * @param n
     * @param m
     * @return
     */
    public float Textscale(float n, float m) {
        return n / 500 * m;
    }

    public class BarAnimation extends Animation {
        public BarAnimation() {
            super();
        }

        /**
         * 每次系统调用这个方法时， 改变mSweepAnglePer，mPercent，stepnumbernow的值，
         * 然后调用postInvalidate()不停的绘制view。
         */
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                // 进度百分比
                mPercent = (int)(interpolatedTime * mValue * 100f / mValueMax);// 将浮点值四舍五入保留一位小数
                // 每个角度占多少
                mSweepAnglePer = interpolatedTime * mValue * 360 / mValueMax;
                // 当前多少百分比
                mValueNow = (int) (interpolatedTime * mValue);
            } else {
                mPercent = (int)(mValue * 100f / mValueMax);// 将浮点值四舍五入保留一位小数
                mSweepAnglePer = mValue * 360 / mValueMax;
                mValueNow = mValue;
            }
            postInvalidate();
        }
    }
    /**
     * 设置动画时间
     *
     */
    public void setAnimationTime() {
        anim.setDuration(mDuration * (int)mValue / (int)mValueMax);// 按照比例设置动画执行时间
    }

    /**
     * 更新步数和设置一圈动画时间
     *
     */
    public void updateValue(float value) {
        this.mValue = value;
//        anim.setDuration(time);
        setAnimationTime();
        // 设置加速度(先加速再减速)
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        this.startAnimation(anim);
    }

    /**
     * 设置最大值
     * @param maxValue
     */
    public void setMaxValue(float maxValue){
        this.mValueMax = maxValue;
    }

}
