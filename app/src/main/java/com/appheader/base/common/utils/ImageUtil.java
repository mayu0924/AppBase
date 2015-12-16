package com.appheader.base.common.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import com.appheader.base.application.GlobalVars;
import com.appheader.base.common.files.ResourceFileManager;
import com.appheader.base.common.user.CurrentUserManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片工具类，主要功能就是压缩图片
 */
public class ImageUtil {

	/**
	 * 压缩图片，返回压缩后图片的路劲
	 * @param srcPath -原图文件路径
	 * @param outputFile -图片输出文件
	 * @param quality -图片质量
	 * @return
	 */
	public static String compressImage(String srcPath, String outputFile, int quality) {
		File srcFile = new File(srcPath);
		Bitmap resultBitmap = null;
		int pictureDegree = readPictureDegree(srcPath);
		// 图片被旋转过
		if (pictureDegree != 0) {
			// 文件大小小于200k，不压缩
			if (srcFile.length() < 200 * 1024) {
				// 将图片旋转回原方向
				resultBitmap = rotaingImageView(pictureDegree, BitmapFactory.decodeFile(srcPath));
			} else {
				Bitmap smallBitmap = getSmallBitmap(srcPath);
				resultBitmap = rotaingImageView(pictureDegree, smallBitmap);
			}
		} else {
			if (srcFile.length() < 200 * 1024) {
				return srcPath;
			} else {
				resultBitmap = getSmallBitmap(srcPath);
			}
		}

		// 压缩后生成新文件
		File newFile = new File(outputFile);
		// File newFile = new File(src.substring(0, src.lastIndexOf("/") + 1) +
		// String.valueOf(System.currentTimeMillis()) + ".jpg");
		try {
			resultBitmap.compress(Bitmap.CompressFormat.JPEG, quality, new FileOutputStream(newFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return newFile.getAbsolutePath();
	}

	// 计算图片的缩放值
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	// 根据路径获得图片并压缩，返回bitmap用于显示
	private static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	private static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 旋转图片
	 * 
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	private static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}
	
	/**
	 * 根据uri得到文件的绝对路径
	 * 
	 * @param uri
	 * @return
	 */
	public static String getAbsoluteImagePath(Uri uri) {
		String path = null;
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = GlobalVars.getContext().getContentResolver().query(uri, projection, null, null, null);
		if (cursor != null) {
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			path = cursor.getString(column_index);
			cursor.close();
		}
		return path;
	}

	/**
	 * 将Uri的流写入指定文件
	 * 
	 * @param uri
	 * @param destFilePath
	 */
	public static String copyFileFromUri(Uri uri, String destFilePath) {
		ContentResolver cr = GlobalVars.getContext().getContentResolver();
		try {
			org.apache.commons.io.FileUtils.copyInputStreamToFile(cr.openInputStream(uri), new File(destFilePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return destFilePath;
	}

	public static String copyFileFromUri(Uri uri) {
		String userIamgeDir;
		if(CurrentUserManager.getCurrentUser() != null){
			userIamgeDir = ResourceFileManager.getUserImageDir(CurrentUserManager.getCurrentUser().getUid());
		} else {
			userIamgeDir = ResourceFileManager.getUserImageDir("cjt_temp");
		}
		String destFilePath = userIamgeDir + File.separator + System.currentTimeMillis() + ".jpg";
		return copyFileFromUri(uri, destFilePath);
	}
}
