package com.appheader.base.common.utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

/**
 * 二维码生成工具类
 * 
 * @author alaowan
 * 
 */
public class QRCodeUtil {

	// 生成QR图
	public static Bitmap createImage(String text, int size, int margin) {
		try {
			int qrWidth = size;
			int qrHeight = qrWidth;
			// 需要引入core包
			QRCodeWriter writer = new QRCodeWriter();

			if (text == null || "".equals(text) || text.length() < 1) {
				return null;
			}

			// 把输入的文本转为二维码
			BitMatrix martix = writer.encode(text, BarcodeFormat.QR_CODE, qrWidth, qrHeight);

			System.out.println("w:" + martix.getWidth() + "h:" + martix.getHeight());

			Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			hints.put(EncodeHintType.MARGIN, margin);
			BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, qrWidth, qrHeight, hints);
			int[] pixels = new int[qrWidth * qrHeight];
			for (int y = 0; y < qrHeight; y++) {
				for (int x = 0; x < qrWidth; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * qrWidth + x] = 0xff000000;
					} else {
						pixels[y * qrWidth + x] = 0xffffffff;
					}

				}
			}

			Bitmap bitmap = Bitmap.createBitmap(qrWidth, qrHeight, Bitmap.Config.ARGB_8888);

			bitmap.setPixels(pixels, 0, qrWidth, 0, 0, qrWidth, qrHeight);

			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}
}
