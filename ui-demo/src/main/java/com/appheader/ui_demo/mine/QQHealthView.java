package com.appheader.ui_demo.mine;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
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

    private Canvas mCanvas;
    private Paint mPaintWhiteBg;
    private Paint mPaintBlueBg;
    private Paint mPaintBlueArc;

    private RectF mRectArc = new RectF();

    private int mWidth;//自定义View宽
    private int mHeight;//自定义View高
    private int mBackgroundCorner;//背景四角的弧度
    private float mRatio;

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
        mRatio = 450.f / 525.f;
        mPaintWhiteBg = new Paint();
        mPaintWhiteBg.setColor(Color.WHITE);
        mPaintWhiteBg.setStyle(Paint.Style.FILL);
        mPaintWhiteBg.setAntiAlias(true);

        mPaintBlueBg = new Paint();
        mPaintBlueBg.setColor(getResources().getColor(R.color.light_blue_500));
        mPaintBlueBg.setStyle(Paint.Style.FILL);
        mPaintBlueBg.setAntiAlias(true);

        mPaintBlueArc = new Paint();
        mPaintBlueArc.setColor(getResources().getColor(R.color.light_blue_500));
        mPaintBlueArc.setStyle(Paint.Style.STROKE);
        mPaintBlueArc.setStrokeWidth(40);
        mPaintBlueArc.setAntiAlias(true);
        mPaintBlueArc.setStrokeCap(Paint.Cap.ROUND);
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
        drawBlueBg(0, mHeight * 85 / 100, mWidth, mHeight, 30);
        drawProgressBar();
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
        Path path = new Path();
        path.moveTo(left, top);

        path.lineTo(right, top);

        path.lineTo(right, bottom - radius);
        path.quadTo(right, bottom, right - radius, bottom);

        path.lineTo(left + radius, bottom);
        path.quadTo(left, bottom, left, bottom - radius);

        mCanvas.drawPath(path, mPaintBlueBg);
    }

    private void drawProgressBar(){

        mCanvas.drawArc(mRectArc, 125, 290, false, mPaintBlueArc);
    }

}
