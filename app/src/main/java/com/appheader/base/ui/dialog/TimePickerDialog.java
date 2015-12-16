package com.appheader.base.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.appheader.base.R;
import com.appheader.base.ui.dialog.timepicker.DateTimePicker;
import com.appheader.base.ui.dialog.timepicker.DateTimePicker.OnDateTimeChangedListener;

import java.util.Calendar;

/**
 * 底部弹出时间选择器
 * @author mayu
 *
 */
public class TimePickerDialog extends Dialog implements OnClickListener {
	private Context ctx;
	private long date;
	private DateTimePicker mDateTimePicker;
	private Calendar mDate = Calendar.getInstance();
	private OnDateTimeSetListener mOnDateTimeSetListener;

	private TextView title;
	
	public TimePickerDialog(Context context, long date) {
		super(context, R.style.CommonDialog);
		this.ctx = context;
		this.date = date;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window = this.getWindow();
		window.setGravity(Gravity.BOTTOM); 
		window.setWindowAnimations(R.style.dialogstyle); 
		this.setCancelable(true);
		
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) ctx).getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;

		setContentView(R.layout.picker_date_select_wheel);
		findViewById(R.id.commit).setOnClickListener(this);
		findViewById(R.id.cancel).setOnClickListener(this);
		title = (TextView) findViewById(R.id.selectDate);
		LinearLayout layout = (LinearLayout) findViewById(R.id.date_selelct_layout);
		mDateTimePicker = new DateTimePicker(ctx);
		LayoutParams lparams_hours = new LayoutParams(width,
				LayoutParams.WRAP_CONTENT);
		mDateTimePicker.setLayoutParams(lparams_hours);
		layout.addView(mDateTimePicker);
		mDateTimePicker
				.setOnDateTimeChangedListener(new OnDateTimeChangedListener() {
					@Override
					public void onDateTimeChanged(DateTimePicker view,
							int year, int month, int day, int hour, int minute) {
						mDate.set(Calendar.YEAR, year);
						mDate.set(Calendar.MONTH, month);
						mDate.set(Calendar.DAY_OF_MONTH, day);
						mDate.set(Calendar.HOUR_OF_DAY, hour);
						mDate.set(Calendar.MINUTE, minute);
						mDate.set(Calendar.SECOND, 0);
						updateTitle(mDate.getTimeInMillis());
					}
				});

		mDate.setTimeInMillis(date);
		updateTitle(mDate.getTimeInMillis());
	}

	public interface OnDateTimeSetListener {
		void OnDateTimeSet(Dialog dialog, long date);
	}

	private void updateTitle(long date) {
		int flag = DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE
				| DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_TIME;
		title.setText(DateUtils.formatDateTime(this.getContext(), date, flag));
	}

	public void setOnDateTimeSetListener(OnDateTimeSetListener callBack) {
		mOnDateTimeSetListener = callBack;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commit:
			if (mOnDateTimeSetListener != null) {
				mOnDateTimeSetListener.OnDateTimeSet(this,
						mDate.getTimeInMillis());
			}
			dismiss();
			break;
		case R.id.cancel:
			dismiss();
			break;
		default:
			break;
		}
	}
}
