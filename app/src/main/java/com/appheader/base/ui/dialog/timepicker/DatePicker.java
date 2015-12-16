package com.appheader.base.ui.dialog.timepicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ParseException;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;

import com.appheader.base.R;
import com.appheader.base.ui.dialog.timepicker.NumberPicker.OnValueChangeListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 日期选择控件
 * 
 * @author mayu
 * 
 */
public class DatePicker extends LinearLayout {
	private List<String> years;
	private List<String> months;
	private List<String> days;
	/** 滑动控件 */
	private NumberPicker yearPicker;
	private NumberPicker monthPicker;
	private NumberPicker dayPicker;
	/** 日历 */
	private CalendarUtil calendarUtil = CalendarUtil.getSingleton();
	/** 日历 */
	public Calendar calendar;

	/** 选择监听 */
	private OnDateChangedListener onDateChangedListener;
	/** 刷新界面 */
	private static final int REFRESH_VIEW = 0x001;

	private NumberPicker.OnValueChangeListener mYearChangedListener = new OnValueChangeListener() {
		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
			if (oldVal != newVal) {
				int selectDay = dayPicker.getValue();
				int selectMonth = monthPicker.getValue();
				setDaysValue(newVal, selectMonth);
				// 最后一天【判断当前日是否是在有效天数范围内】
				int lastDay = dayPicker.getMaxValue();
				if (Integer.valueOf(selectDay) > lastDay) {
					dayPicker.setValue(lastDay);
				} else {
					dayPicker.setValue(selectDay);
				}
			}
			Message message = new Message();
			message.what = REFRESH_VIEW;
			handler.sendMessage(message);
		}
	};

	private NumberPicker.OnValueChangeListener mMonthChangedListener = new OnValueChangeListener() {
		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
			if (oldVal != newVal) {
				int selectYear = yearPicker.getValue();
				int selectDay = dayPicker.getValue();
				setDaysValue(selectYear, newVal);
				// 最后一天【判断当前日是否是在有效天数范围内】
				int lastDay = dayPicker.getMaxValue();
				if (Integer.valueOf(selectDay) > lastDay) {
					dayPicker.setValue(lastDay);
				} else {
					dayPicker.setValue(selectDay);
				}
			}
			Message message = new Message();
			message.what = REFRESH_VIEW;
			handler.sendMessage(message);
		}
	};

	private NumberPicker.OnValueChangeListener mDayChangedListener = new OnValueChangeListener() {
		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
			Message message = new Message();
			message.what = REFRESH_VIEW;
			handler.sendMessage(message);
		}
	};

	public DatePicker(Context context) {
		super(context);
		inflate(context, R.layout.picker_date_picker, this);
		// 初始化日历
		calendar = Calendar.getInstance();
		calendarUtil = CalendarUtil.getSingleton();
		// 初始化显示的【年、月】
		years = calendarUtil.getYears();
		months = calendarUtil.getMonths();
		// 获取控件引用
		yearPicker = (NumberPicker) findViewById(R.id.year);
		monthPicker = (NumberPicker) findViewById(R.id.month);
		dayPicker = (NumberPicker) findViewById(R.id.day);
		yearPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
		monthPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
		dayPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
		// 设置滑动监听
		yearPicker.setOnValueChangedListener(mYearChangedListener);
		monthPicker.setOnValueChangedListener(mMonthChangedListener);
		dayPicker.setOnValueChangedListener(mDayChangedListener);
		// 设置【年月显示格式】
		initDisplay();
	}

	/**
	 * 初始化【年、月】显示
	 */
	private void initDisplay(){
		// 设置数据
		yearPicker.setMinValue(Integer.valueOf(years.get(0)));
		yearPicker.setMaxValue(Integer.valueOf(years.get(years.size() - 1)));
		
		monthPicker.setMinValue(Integer
				.valueOf(calendarUtil.getMonths().get(0)));
		monthPicker.setMaxValue(Integer.valueOf(calendarUtil.getMonths().get(
				calendarUtil.getMonths().size() - 1)));
		// 设置显示格式
		String[] mYearDisplayValues = new String[years.size()];
		for (int j = 0; j < mYearDisplayValues.length; j++) {
			mYearDisplayValues[j] = years.get(j)+"年";
		}
		yearPicker.setDisplayedValues(mYearDisplayValues);
		
		String[] mMonthDisplayValues = new String[months.size()];
		for (int j = 0; j < mMonthDisplayValues.length; j++) {
			mMonthDisplayValues[j] = months.get(j)+"月";
		}
		monthPicker.setDisplayedValues(mMonthDisplayValues);
	}

	public void setTime(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		
		setDateValue(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * 设置显示时间
	 * @param year
	 * @param month
	 * @param day
	 */
	public void setDateValue(int year, int month, int day) {
		// 设置默认值为当前----------------------------------------------------------------
		yearPicker.setValue(year);
		monthPicker.setValue(month + 1);
		setDaysValue(year, month);
		dayPicker.setValue(day);
	}
	
	/**
	 *  设置日显示
	 * @param year
	 * @param month
	 * @param day
	 */
	private void setDaysValue(int year, int month){
		// 根据年月设置该月的日期天数--------------------------------------------------------
		days = calendarUtil.getDays(year, month - 1);
		dayPicker.setMinValue(Integer.valueOf(days.get(0)));
		dayPicker.setMaxValue(Integer.valueOf(days.get(days.size() - 1)));
//		String[] mDaysDisplayValues = new String[days.size()];
//		for (int i = 0; i < days.size(); i++) {
//			mDaysDisplayValues[i] = days.get(i) + "日";
//		}
//		dayPicker.setDisplayedValues(mDaysDisplayValues);
	}
	

	/**
	 * 获取时间
	 * 
	 * @return
	 */
	public Date getDate() {
		String timeString = yearPicker.getValue() + "-"
				+ monthPicker.getValue() + "-" + dayPicker.getValue();
		Date date = new Date(
				Long.valueOf(formatToTime(timeString, "yyyy-MM-dd")));
		return date;
	}

	/**
	 * 获取时间戳
	 * 
	 * @return
	 */
	public long getTime() {
		return getDate().getTime();
	}

	public int getYear() {
		return Integer.valueOf(yearPicker.getValue());
	}

	public int getMonth() {
		return Integer.valueOf(monthPicker.getValue());
	}

	public int getDay() {
		return Integer.valueOf(dayPicker.getValue());
	}

	public String getFormaTime() {
		return (yearPicker.getValue() + "-" + monthPicker.getValue() + "-" + dayPicker
				.getValue());
	}

	public String getFormatTime(String format) {
		return getFormatTime(getTime(), format);
	}

	public void setDateChangedListener(
			OnDateChangedListener onDateChangedListener) {
		this.onDateChangedListener = onDateChangedListener;
	}

	/**
	 * 从格式化时间获取time
	 * 
	 * @param time
	 * @param format
	 * @return
	 */
	public static String formatToTime(String time, String format) {
		Date date = null;
		SimpleDateFormat formatTime = new SimpleDateFormat(format, Locale.CHINA);
		try {
			try {
				date = formatTime.parse(time);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return String.valueOf(date.getTime());
	}

	/**
	 * 返回指定格式时间
	 * 
	 * @param time
	 * @param format
	 * @return
	 */
	public static String getFormatTime(long time, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
		return dateFormat.format(time);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				if (onDateChangedListener != null)
					onDateChangedListener.onDateChanged(DatePicker.this,
							yearPicker.getValue(), monthPicker.getValue(),
							dayPicker.getValue());
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 
	 * 日期监听
	 * 
	 * @author mayu
	 * 
	 */
	public interface OnDateChangedListener {

		public void onDateChanged(DatePicker view, int year, int month, int day);
	}

}
