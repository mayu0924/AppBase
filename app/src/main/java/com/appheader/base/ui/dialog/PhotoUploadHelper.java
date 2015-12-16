package com.appheader.base.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.appheader.base.R;
import com.appheader.base.common.files.ResourceFileManager;
import com.appheader.base.common.imgCrop.CropImage;
import com.appheader.base.common.network.UploadUtil;
import com.appheader.base.common.network.entity.UrlConstants;
import com.appheader.base.common.user.CurrentUserManager;
import com.appheader.base.common.utils.ImageUtil;
import com.appheader.base.common.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 图片选择裁剪并上传
 * 
 * @author mayu
 * 
 */
public class PhotoUploadHelper implements OnClickListener {

	private Activity mActivity;

	private UploadCallback mCallback;

	// 返回从图库选择的图片uri
	private static final int PHOTO_GALLERY_ACTIVITY_REQUEST = 1;
	// 返回从相机拍照的图片uri
	private static final int CAMERA_CAPTURE_ACTIVITY_REQUEST = 2;
	// 返回裁剪后的图片uri
	private static final int PHOTO_PICKED_WITH_DATA = 3;

	// 图片暂存目录
	private String mImgCachePath;
	// 相机拍照后存放的图片文件全路径
	private File mCameraPhotoFile;
	private Dialog mDialogSelectUpload;

	// 是否需要
	private boolean mNeedCrop;
	// 裁剪比例
	private int aspect_x, aspect_y;
	// 是否需要压缩图片
	private boolean mNeedCompress;

	public PhotoUploadHelper(Activity act, UploadCallback mCallback) {
		this.mCallback = mCallback;
		this.mActivity = act;
		if(CurrentUserManager.getCurrentUser() != null){
			mImgCachePath = ResourceFileManager.getUserImageDir(CurrentUserManager.getCurrentUser().getUid());
		} else {
			mImgCachePath = ResourceFileManager.getUserImageDir("cjt_temp");
		}
	}

	public void doActResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PHOTO_GALLERY_ACTIVITY_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				Uri uri = data.getData();
				String sourceFilePath = ImageUtil.copyFileFromUri(uri);
				File sourceFile = new File(sourceFilePath);
				if (mNeedCrop)
					doCropPhoto(Uri.fromFile(sourceFile), aspect_x, aspect_y);
				else
					uploadFile(mNeedCompress ? compressImage(sourceFile.getAbsolutePath()) : sourceFile.getAbsolutePath());
				mDialogSelectUpload.dismiss();
			}
			break;
		case CAMERA_CAPTURE_ACTIVITY_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				if (mNeedCrop)
					doCropPhoto(Uri.fromFile(mCameraPhotoFile), aspect_x, aspect_y);
				else
					uploadFile(mNeedCompress ? compressImage(mCameraPhotoFile.getAbsolutePath()) : mCameraPhotoFile.getAbsolutePath());
			}
			break;
		case PHOTO_PICKED_WITH_DATA:
			if (resultCode == Activity.RESULT_OK) {
				String path = data.getStringExtra(CropImage.IMAGE_PATH);
				if (path == null) {
					return;
				}
				path = mNeedCompress ? compressImage(path) : path;
				uploadFile(path);
				mDialogSelectUpload.dismiss();
			}
			break;

		default:
			break;
		}
	}

	private void uploadFile(String filePath) {
		(new UploadPhotoTask(filePath, UrlConstants.UPLOAD_FILE_RESOURSE + "&fileType=image")).execute();
	}

	private String compressImage(String sourceFilePath) {
		File mCompressedFile = new File(mImgCachePath + File.separator + System.currentTimeMillis() + ".jpg");
		return ImageUtil.compressImage(sourceFilePath, mCompressedFile.getAbsolutePath(), 80);
	}

	/**
	 * 点击选择上传图片对话框
	 * 
	 * @param needCrop
	 *            -是否需要裁减
	 * @param needCompress
	 *            - 是否需要压缩图片
	 * @return
	 */
	@SuppressLint("InflateParams")
	public Dialog showUploadSelectDialog(String title, boolean needCrop, int aspect_x, int aspect_y, boolean needCompress) {
		mNeedCrop = needCrop;
		mNeedCompress = needCompress;
		this.aspect_x = aspect_x;
		this.aspect_y = aspect_y;
		if (mDialogSelectUpload == null) {
			mDialogSelectUpload = new Dialog(mActivity, R.style.CommonDialog);
			LayoutInflater in = LayoutInflater.from(mActivity);
			View viewDialog = in.inflate(R.layout.pub_dialog_upload_photo, null);
			viewDialog.findViewById(R.id.item_photo).setOnClickListener(this);
			viewDialog.findViewById(R.id.item_camera).setOnClickListener(this);
			((TextView)viewDialog.findViewById(R.id.title)).setText(title);
			mDialogSelectUpload.setContentView(viewDialog);
			mDialogSelectUpload.setCanceledOnTouchOutside(true);
		}
		mDialogSelectUpload.show();
		return mDialogSelectUpload;
	}

	/**
	 * 打开图库选择图片
	 */
	private void startPhotoGallery() {
		Intent photoGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		mActivity.startActivityForResult(photoGallery, PHOTO_GALLERY_ACTIVITY_REQUEST);
	}

	/**
	 * 启动相机拍照
	 */
	private void startCameraCapture() {
		Intent cameraCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String fileName = System.currentTimeMillis() + ".JPG";
		mCameraPhotoFile = new File(mImgCachePath, fileName);
		Uri uri = Uri.fromFile(mCameraPhotoFile);
		cameraCapture.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		cameraCapture.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		mActivity.startActivityForResult(cameraCapture, CAMERA_CAPTURE_ACTIVITY_REQUEST);
	}

	/**
	 * 启动图库裁剪功能
	 * 
	 * @param data
	 */
	private void doCropPhoto(Uri data, int aspect_x, int aspect_y) {
		Intent intent = new Intent(mActivity, CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, data.getPath());
		intent.putExtra(CropImage.SCALE, true);
		intent.putExtra(CropImage.ASPECT_X, aspect_x);
		intent.putExtra(CropImage.ASPECT_Y, aspect_y);
		mActivity.startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.item_photo:
			mDialogSelectUpload.dismiss();
			startPhotoGallery();
			break;
		case R.id.item_camera:
			mDialogSelectUpload.dismiss();
			startCameraCapture();
			break;

		default:
			break;
		}
	}

	/**
	 * 上传头像
	 * 
	 */
	class UploadPhotoTask extends AsyncTask<Void, Integer, Boolean> {
		private String filePath, requestURL;
		private ProgressDialog pDialog;
		private UploadUtil mUpload;
		private String mResourceId;

		public UploadPhotoTask(String filePath, String requestURL) {
			this.filePath = filePath;
			this.requestURL = requestURL;
		}

		@SuppressLint("InlinedApi")
		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(mActivity, ProgressDialog.THEME_HOLO_LIGHT);
			pDialog.setMessage("正在上传，请稍后...");
			pDialog.setCanceledOnTouchOutside(false);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean success;
			try {
				mUpload = UploadUtil.getInstance();
				String json = mUpload.uploadFile(filePath, requestURL);
				success = (Tools.getJsonString(new JSONObject(json), "success").equals("true"));
				if (success) {
					mResourceId = Tools.getJsonString(new JSONObject(Tools.getJsonString(new JSONObject(json), "data")), "resourceId");
				}
			} catch (Exception e) {
				e.printStackTrace();
				success = false;
			}
			return success;
		}

		@Override
		protected void onPostExecute(Boolean success) {
			pDialog.cancel();
			if (success) {
				Toast.makeText(mActivity, "上传成功", Toast.LENGTH_SHORT).show();
				JSONObject result = new JSONObject();
				try {
					result.put("resourceId", mResourceId);
					result.put("imageUrl", UrlConstants.getResourceUrl(mResourceId));
					result.put("thumbUrl", UrlConstants.getImageThumbUrl(mResourceId));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				mCallback.success(result);
			} else {
				Toast.makeText(mActivity, "上传失败", Toast.LENGTH_SHORT).show();
				mCallback.failed("上传失败");
			}
		}
	}

	/**
	 * 图片上传回调接口
	 * 
	 * @author mayu
	 * 
	 */
	public interface UploadCallback {
		void success(JSONObject obj);

		void failed(String str);
	}
}
