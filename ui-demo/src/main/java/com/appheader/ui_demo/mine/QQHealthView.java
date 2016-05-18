package com.appheader.ui_demo.mine;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.appheader.ui_demo.R;

/**
 * 仿QQ健康界面
 * Created by mayu on 16/5/11,下午4:49.
 */
public class QQHealthView extends View {
    private static String TAG = "QQHealthView";

    private int mMaxSteps = 15000;

    private int[] mSteps = {5000, 17000, 8000, 12000, 8320, 4000, 13890};
    private String[] mDays = {"01日", "02日", "03日", "04日", "05日", "06日", "07日"};
    private Canvas mCanvas;
    private Paint mPaintWhiteBg;
    private Paint mPaintBlueLight;
    private Paint mPaintBlueBg;
    private Paint mPaintBlueArc;
    private Paint mPaintGrayArc;
    private Paint mPaintDashLine;
    private Paint mPaintDate;
    private Paint mPaintPlaceBlue;
    private Paint mPaintStepBlue;

    private RectF mRectArc = new RectF();

    private int mWidth;//自定义View宽
    private int mHeight;//自定义View高
    private int mBackgroundCorner;//背景四角的弧度
    private float mRatio;

    private int mStep = 0;
    private float mPercent = 0.0001f;

    public QQHealthView(Context context) {
        super(context);
        init(null, 0);
    }

    public QQHealthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public QQHealthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @TargetApi(21)
    public QQHealthView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr){
        // 下面这句是关闭硬件加速，防止某些4.0的设备虚线显示为实线的问题
        // 可以在AndroidManifest.xml时的Application标签加上android:hardwareAccelerated=”false”,
        // 这样整件应用都关闭了硬件加速，虚线可以正常显示，但是，关闭硬件加速对性能有些影响，
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //自定义View的宽高比例
        mRatio = 450.f / 575.f;
        mPaintWhiteBg = new Paint();
        mPaintWhiteBg.setColor(Color.WHITE);
        mPaintWhiteBg.setStyle(Paint.Style.FILL);
        mPaintWhiteBg.setAntiAlias(true);

        mPaintBlueBg = new Paint();
        mPaintBlueBg.setColor(getResources().getColor(R.color.light_blue_500));
        mPaintBlueBg.setStyle(Paint.Style.FILL);
        mPaintBlueBg.setAntiAlias(true);

        mPaintBlueLight = new Paint();
        mPaintBlueLight.setColor(getResources().getColor(R.color.light_blue));
        mPaintBlueLight.setStyle(Paint.Style.FILL);
        mPaintBlueLight.setAntiAlias(true);

        mPaintBlueArc = new Paint();
        mPaintBlueArc.setColor(getResources().getColor(R.color.light_blue_500));
        mPaintBlueArc.setStyle(Paint.Style.STROKE);
        mPaintBlueArc.setStrokeCap(Paint.Cap.ROUND);
        mPaintBlueArc.setStrokeWidth(40);
        mPaintBlueArc.setAntiAlias(true);

        mPaintGrayArc = new Paint();
        mPaintGrayArc.setColor(getResources().getColor(R.color.gray_btn_bg_color));
        mPaintGrayArc.setStyle(Paint.Style.STROKE);
        mPaintGrayArc.setStrokeCap(Paint.Cap.ROUND);
        mPaintGrayArc.setStrokeWidth(40);
        mPaintGrayArc.setAntiAlias(true);

        mPaintDashLine = new Paint();
        mPaintDashLine.setColor(getResources().getColor(R.color.gray_btn_bg_pressed_color));
        mPaintDashLine.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintDashLine.setStrokeWidth(2.f);
        mPaintDashLine.setAntiAlias(true);

        mPaintDate = new Paint();
        mPaintDate.setColor(getResources().getColor(R.color.gray_btn_bg_pressed_color));
        mPaintDate.setStyle(Paint.Style.FILL);
        mPaintDate.setStrokeWidth(0.5f);
        mPaintDate.setAntiAlias(true);
        mPaintDate.setTextSize(30.f);

        mPaintPlaceBlue = new Paint();
        mPaintPlaceBlue.setColor(getResources().getColor(R.color.light_blue_500));
        mPaintPlaceBlue.setStyle(Paint.Style.FILL);
        mPaintPlaceBlue.setStrokeWidth(2.f);
        mPaintPlaceBlue.setAntiAlias(true);
        mPaintPlaceBlue.setTextSize(60.f);

        mPaintStepBlue = new Paint();
        mPaintStepBlue.setColor(getResources().getColor(R.color.light_blue_500));
        mPaintStepBlue.setStyle(Paint.Style.FILL);
        mPaintStepBlue.setStrokeWidth(2.f);
        mPaintStepBlue.setAntiAlias(true);
        mPaintStepBlue.setTypeface(Typeface.DEFAULT_BOLD);
        mPaintStepBlue.setTextSize(100.f);
        mPaintStepBlue.setShadowLayer(2, 1, 1, Color.parseColor("#00CCFF"));// 设置阴影

//        setAnimation();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int defaultWidth = Integer.MAX_VALUE;
        int width;
        int height;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        /**
         * wrap_parent -> MeasureSpec.AT_MOST
         * match_parent -> MeasureSpec.EXACTLY
         * 具体值 -> MeasureSpec.EXACTLY
         */
        if(widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
        } else {
            width = defaultWidth;
        }
        int defaultHeight = (int)(mWidth / mRatio);
        height = defaultHeight;
        setMeasuredDimension(width, height);
        Log.i(TAG, "width:" + mWidth + "| height:" + mHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        int r = mHeight / 4;
        int left = 0+(mWidth-r*2)/2;
        int top = 80;
        int right = mWidth - (mWidth-r*2)/2;
        int bottom = 0+mHeight/2+80;
        mRectArc.set(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mCanvas = canvas;
        drawWhiteBg(0, 0, mWidth, mHeight, 30);
        drawBlueBg(0, mHeight * 87 / 100, mWidth, mHeight, 30);
        drawProgressBar();
        drawDashLine();
        drawStep();
        drawPlace();
        drawCurrSteps();
    }


    private void setAnimation(){
        //加入动画
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator anim0 = ValueAnimator.ofInt(0, mMaxSteps);
        anim0.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mStep = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mPercent = (float)valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animatorSet.setDuration(1500);
        animatorSet.playTogether(anim0, anim);
        animatorSet.start();
    }

    /**
     * 绘制白色背景
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param radius
     */
    private void drawWhiteBg(int left, int top, int right, int bottom, int radius){
        Path path = new Path();
        path.moveTo(left, top);

        path.lineTo(right - radius, top);
        path.quadTo(right, top, right, top + radius);

        path.lineTo(right, bottom - radius);
        path.quadTo(right, bottom, right - radius, bottom);

        path.lineTo(left + radius, bottom);
        path.quadTo(left, bottom, left, bottom - radius);

        path.lineTo(left, top + radius);
        path.quadTo(left, top, left + radius, top);

        mCanvas.drawPath(path, mPaintWhiteBg);

        mCanvas.drawArc(mRectArc, 120, 300, false, mPaintGrayArc);
    }

    /**
     * 绘制蓝色背景
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param radius
     */
    private void drawBlueBg(int left, int top, int right, int bottom, int radius){

        Path path0 = new Path();
        path0.moveTo(left, top - 50);
        path0.quadTo(left + mWidth / 4.f, top + 10, left + mWidth * 2 / 3.f, top);
        path0.quadTo(left + mWidth * 3 / 4.f, top + 50, right, top-30);
        path0.lineTo(right, bottom - radius);
        path0.quadTo(right, bottom, right - radius, bottom);
        path0.lineTo(left + radius, bottom);
        path0.quadTo(left, bottom, left, bottom - radius);
        mCanvas.drawPath(path0, mPaintBlueLight);

        Path path2 = new Path();
        path2.moveTo(left, top + 20);
        path2.quadTo(left + mWidth * 1 / 3.f, top - 100, right, top+20);
        path2.lineTo(right, bottom - radius);
        path2.quadTo(right, bottom, right - radius, bottom);
        path2.lineTo(left + radius, bottom);
        path2.quadTo(left, bottom, left, bottom - radius);
        mPaintBlueLight.setColor(getResources().getColor(R.color.light_blue2));
        mCanvas.drawPath(path2, mPaintBlueLight);

        Path path1 = new Path();
        path1.moveTo(left, top );
        path1.quadTo(left + mWidth / 4.f, top + 40, left + mWidth * 1 / 4.f, top + 30);
        path1.quadTo(left + mWidth * 2 / 5.f, top - 20, right, top - 40);
        path1.lineTo(right, bottom - radius);
        path1.quadTo(right, bottom, right - radius, bottom);
        path1.lineTo(left + radius, bottom);
        path1.quadTo(left, bottom, left, bottom - radius);
        mPaintBlueLight.setColor(getResources().getColor(R.color.light_blue3));
        mCanvas.drawPath(path1, mPaintBlueLight);

        Path path = new Path();
        path.moveTo(left, top);
        path.lineTo(right, top);
        path.lineTo(right, bottom - radius);
        path.quadTo(right, bottom, right - radius, bottom);
        path.lineTo(left + radius, bottom);
        path.quadTo(left, bottom, left, bottom - radius);
        mCanvas.drawPath(path, mPaintBlueBg);
    }

    /**
     * 绘制蓝色弧形
     */
    private void drawProgressBar(){
        mCanvas.drawArc(mRectArc, 120, 300 * mPercent, false, mPaintBlueArc);
    }

    /**
     * 绘制虚线
     */
    private void drawDashLine(){
        Path path = new Path();
        path.moveTo(50, mHeight * 0.68f);
        path.lineTo(mWidth - 50,mHeight * 0.68f);
        PathEffect effects = new DashPathEffect(new float[]{15,10,15,10},1);
        mPaintDashLine.setPathEffect(effects);
        mCanvas.drawPath(path, mPaintDashLine);
    }

    /**
     * 绘制步数记录
     */
    private void drawStep(){
        float perStep = mHeight * (0.75f - 0.68f) / 10000;
        float distance = (mWidth - 100) / 7;
        int stepSum = 0;
        for (int i = 0; i < 7; i++) {
            stepSum += mSteps[i];
            // 绘制步数
            Path path = new Path();
            path.moveTo(50 + (i + 0.5f) * distance, mHeight * 0.75f);
            path.lineTo(50 + (i + 0.5f) * distance,mHeight * 0.75f - perStep * mSteps[i]);
            mCanvas.drawPath(path, mPaintBlueArc);

            mPaintDate.setTextSize(26.f);
            // 绘制日期
            Rect bounds = new Rect();
            mPaintDate.getTextBounds(mDays[i], 0, mDays[i].length(), bounds);
            mCanvas.drawText(mDays[i], 50 + (i + 0.5f) * distance - bounds.width() / 2, mHeight * 0.79f + bounds.height()/2, mPaintDate);
        }

        mCanvas.drawText("最近7天", 50, mHeight * 0.60f, mPaintDate);

        Rect bounds = new Rect();
        String text = "平均" + stepSum / 7 + "步/天";
        mPaintDate.setTextSize(30.f);
        mPaintDate.getTextBounds(text, 0, text.length(), bounds);
        mCanvas.drawText(text, mWidth - bounds.width() - 50, mHeight * 0.60f, mPaintDate);
    }

    /**
     * 绘制名次
     */
    private void drawPlace(){
        mPaintDate.setTextSize(40.f);
        mPaintDate.setStrokeWidth(2.f);
        Rect bounds1 = new Rect();
        String text1 = "第";
        mPaintDate.getTextBounds(text1, 0, text1.length(), bounds1);

        mCanvas.drawText(text1, mWidth / 2 - mHeight / 8 + bounds1.width(), mHeight * 0.57f, mPaintDate);
        mCanvas.drawText("名", mWidth / 2 + mHeight / 8 - 2*bounds1.width(), mHeight * 0.57f, mPaintDate);

        Rect bounds2 = new Rect();
        String text2 = "29";
        mPaintPlaceBlue.getTextBounds(text2, 0, text2.length(), bounds2);
        mCanvas.drawText(text2, mWidth / 2 - bounds2.width()/2, mHeight * 0.57f, mPaintPlaceBlue);
    }

    /**
     * 绘制当前步数
     */
    private void drawCurrSteps(){
        Rect bounds = new Rect();
        String text = mStep + "";
        mPaintStepBlue.getTextBounds(text, 0, text.length(), bounds);
        mCanvas.drawText(text, mWidth / 2 - bounds.width()/2, mHeight / 4 + 80 + bounds.height() / 2, mPaintStepBlue);

        Rect bounds2 = new Rect();
        String text2 = "截止22:25已走";
        mPaintDate.getTextBounds(text2, 0, text2.length(), bounds2);
        mCanvas.drawText(text2, mWidth / 2 - bounds2.width()/2, mHeight / 4 + 80 - bounds.height() / 2 - bounds2.height(), mPaintDate);

        Rect bounds3 = new Rect();
        String text3 = "好友平均3478步";
        mPaintDate.getTextBounds(text3, 0, text3.length(), bounds3);
        mCanvas.drawText(text3, mWidth / 2 - bounds3.width()/2, mHeight / 4 + 80 + bounds.height() + bounds3.height(), mPaintDate);

    }

    public void setSteps(int progress){
        if(progress > 0){
            mPercent = (float) progress / mMaxSteps;
        } else {
            mPercent = 0.0001f;
        }
        mStep = progress;
        invalidate();
    }
}
