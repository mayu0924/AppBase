package com.appheader.base.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.appheader.base.R;
import com.appheader.base.common.utils.TimeUtil;
import com.appheader.base.ui.dialog.timepicker.DatePicker;
import com.appheader.base.ui.dialog.timepicker.DatePicker.OnDateChangedListener;

import java.util.Calendar;

/**
 * 底部弹出时间选择器
 * @author mayu
 *
 */
public class DatePickerDialog extends Dialog implements OnClickListener {
	private Context ctx;
	private long date;
	private DatePicker mDatePicker;
	private Calendar mDate = Calendar.getInstance();
	private OnDateSetListener mOnDateSetListener;

	private TextView title;
	
	public DatePickerDialog(Context context) {
		super(context, R.style.CommonDialog);
		this.ctx = context;
	}
	
	public DatePickerDialog(Context context, long date) {
		super(context, R.style.CommonDialog);
		this.ctx = context;
		this.date = date;
	}
	
	public void setDate(long date){
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
		mDatePicker = new DatePicker(ctx);
		LayoutParams lparams_hours = new LayoutParams(width,
				LayoutParams.WRAP_CONTENT);
		mDatePicker.setLayoutParams(lparams_hours);
		layout.addView(mDatePicker);
		mDatePicker.setDateChangedListener(new OnDateChangedListener() {
			@Override
			public void onDateChanged(DatePicker view, int year, int month, int day) {
				mDate.set(Calendar.YEAR, year);
				mDate.set(Calendar.MONTH, month - 1);
				mDate.set(Calendar.DAY_OF_MONTH, day);
				updateTitle(mDate.getTimeInMillis());
			}
		});

		mDatePicker.setTime(date);
		updateTitle(date);
	}

	public interface OnDateSetListener {
		void OnDateSet(Dialog dialog, long date);
	}

	private void updateTitle(long date) {
//		int flag = DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY;
//		title.setText(DateUtils.formatDateTime(this.getContext(), date, flag));
		title.setText(TimeUtil.generateTimeStr4(date));
	}

	public void setOnDateListener(OnDateSetListener callBack) {
		mOnDateSetListener = callBack;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commit:
			if (mOnDateSetListener != null) {
				mOnDateSetListener.OnDateSet(this, mDate.getTimeInMillis());
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
