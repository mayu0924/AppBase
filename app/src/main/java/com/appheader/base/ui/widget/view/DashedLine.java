package com.appheader.base.ui.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.appheader.base.R;
import com.appheader.base.common.utils.ImageLoadUtil;

public class DashedLine extends View {
	private Paint paint = null;
	private Path path = null;
	private PathEffect pe = null;

	public DashedLine(Context paramContext) {
		this(paramContext, null);
	}

	public DashedLine(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		// 通过R.styleable.dashedline获得我们在attrs.xml中定义的
		// <declare-styleable name="dashedline"> TypedArray
		TypedArray a = paramContext.obtainStyledAttributes(paramAttributeSet,
				R.styleable.dashedline);
		// 我们在attrs.xml中<declare-styleable name="dashedline">节点下
		// 添加了<attr name="lineColor" format="color" />
		// 表示这个属性名为lineColor类型为color。当用户在布局文件中对它有设定值时
		// 可通过TypedArray获得它的值当用户无设置值是采用默认值0XFF00000
		int lineColor = a
				.getColor(R.styleable.dashedline_lineColor, 0XFF000000);
		a.recycle();
		this.paint = new Paint();
		this.path = new Path();
		this.paint.setStyle(Paint.Style.STROKE);
		this.paint.setColor(lineColor);
		this.paint.setAntiAlias(true);
		this.paint.setStrokeWidth(ImageLoadUtil.dip2px(getContext(), 2.0F));
		float[] arrayOfFloat = new float[4];
		arrayOfFloat[0] = ImageLoadUtil.dip2px(getContext(), 2.0F);
		arrayOfFloat[1] = ImageLoadUtil.dip2px(getContext(), 2.0F);
		arrayOfFloat[2] = ImageLoadUtil.dip2px(getContext(), 2.0F);
		arrayOfFloat[3] = ImageLoadUtil.dip2px(getContext(), 2.0F);
		this.pe = new DashPathEffect(arrayOfFloat, ImageLoadUtil.dip2px(
				getContext(), 1.0F));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		this.path.moveTo(0.0F, 0.0F);
		this.path.lineTo(0.0F, getMeasuredHeight());
		/*this.path.lineTo(getMeasuredWidth(), 0.0F);*/
		this.paint.setPathEffect(this.pe);
		canvas.drawPath(this.path, this.paint);
	}

}
