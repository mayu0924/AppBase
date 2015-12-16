package com.appheader.base.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.appheader.base.R;

public class CommonDialog extends Dialog {
	private TextView mDialogTitle, mDialogContents;
	private Button mDialogCancel, mDialogOk;
	private View.OnClickListener mClick;
	private String mTitle="",mContents="";
	private	String mCancel="取消",mOk = "确定";
	public CommonDialog(Context context,String title,String contents,View.OnClickListener click) {
		super(context, R.style.CommonDialog);
		mClick = click;
		mTitle = title;
		mContents = contents;
	}
	public CommonDialog(Context context,String title,String cancel,String ok,String contents,View.OnClickListener click) {
		super(context, R.style.CommonDialog);
		mClick = click;
		mTitle = title;
		mContents = contents;
		mCancel = cancel;
		mOk = ok;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_common);
		mDialogTitle = (TextView) findViewById(R.id.dialog_title);
		mDialogContents = (TextView) findViewById(R.id.dialog_contents);
		mDialogCancel = (Button) findViewById(R.id.dialog_cancel);
		mDialogOk = (Button) findViewById(R.id.dialog_ok);
		mDialogCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cancel();
			}
		});
		mDialogCancel.setText(mCancel);
		mDialogOk.setText(mOk);
		mDialogOk.setOnClickListener(mClick);
		mDialogTitle.setText(mTitle);
		mDialogContents.setText(mContents);
	}
}
