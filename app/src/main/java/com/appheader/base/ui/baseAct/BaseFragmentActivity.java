package com.appheader.base.ui.baseAct;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.appheader.base.common.user.CurrentUserManager;
import com.appheader.base.common.user.UserInfo;
import com.appheader.base.common.utils.SystemBarTintManager;
import com.appheader.base.sdk.glide.GlideManager;
import com.appheader.base.ui.dialog.CommonDialog;

/**
 * 
 * @author mayu
 *
 */
public class BaseFragmentActivity extends FragmentActivity {
	private SystemBarTintManager tintManager;
	private CommonDialog mLogin = null;
	public boolean ALIVE;

	protected GlideManager mGlideManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ALIVE = true;
		mGlideManager = new GlideManager(this);
		ActivityManager.getInstance().pushActivity(this);
	}

	protected void setStatuBarColor(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(Color.BLACK);
	}
	
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
	
    public void showUnLoginTip(){
		if(mLogin == null){
			mLogin = new CommonDialog(this, "提示", "您还未登录，是否现在登录？", new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO ...
					startActivity(new Intent(BaseFragmentActivity.this,Void.class));
					mLogin.cancel();
				}
			});
		}
		if(!mLogin.isShowing()){
			mLogin.show();
		}
	}
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityManager.getInstance().popActivity(this);
		ALIVE = false;
	}

	protected UserInfo getCurrentUser() {
		return CurrentUserManager.getCurrentUser();
	}
}
