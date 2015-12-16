package com.appheader.base.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.appheader.base.R;
import com.appheader.base.sdk.umeng.ShareContent;
import com.appheader.base.sdk.umeng.UMSdkManager;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

/**
 * 分享对话框
 * @author mayu
 *
 */
@SuppressLint("InflateParams")
public class ShareDialog extends Dialog implements View.OnClickListener {
	private UMSdkManager mUMSdkManager;
	private Context mContext;
	private OnShareListener mListener;

	private ShareContent mShareContent;

	public ShareDialog(Context context, ShareContent shareContent,
			OnShareListener listener) {
		super(context, R.style.CommonDialog);
		this.mContext = context;
		this.mListener = listener;
		this.mShareContent = shareContent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window = this.getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.setWindowAnimations(R.style.dialogstyle);
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		int width = dm.widthPixels;
		LayoutParams params = new LayoutParams(width,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		setCancelable(true);
		setCanceledOnTouchOutside(false);

		LayoutInflater li = (LayoutInflater) mContext
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View view = li.inflate(R.layout.dialog_share, null);
		setContentView(view, params);

		mUMSdkManager = UMSdkManager.init((Activity) mContext,
				UMServiceFactory.getUMSocialService(UMSdkManager.SHARE));
		mUMSdkManager.setShareContent((Activity) mContext, mShareContent);

		findViewById(R.id.btnWX).setOnClickListener(this);
		findViewById(R.id.btnFriend).setOnClickListener(this);

		findViewById(R.id.cancel).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnWX:
			share(SHARE_MEDIA.WEIXIN);
			break;
		case R.id.btnFriend:
			share(SHARE_MEDIA.WEIXIN_CIRCLE);
			break;
		case R.id.cancel:
			mListener.result(false);
			dismiss();
			break;
		default:
			break;
		}
	}

	private void share(SHARE_MEDIA media) {
		if(!UMSdkManager.isWXAppInstalledAndSupported(mContext)){
			Toast.makeText(mContext, "您还没有安装微信", Toast.LENGTH_SHORT).show();
			return;
		}
		mUMSdkManager.share((Activity) mContext, media, new SnsPostListener() {
			@Override
			public void onStart() {

			}

			@Override
			public void onComplete(SHARE_MEDIA share_media, int stCode,
					SocializeEntity socializeEntity) {
				if (stCode == 200) {
					mListener.result(true);
				} else {
					mListener.result(false);
				}
			}
		});
	}

	public interface OnShareListener {
		void result(boolean result);
	}
	
	@Override
	public void show() {
		super.show();
	}
}
