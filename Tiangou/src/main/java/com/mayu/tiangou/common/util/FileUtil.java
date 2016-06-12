package com.mayu.tiangou.common.util;

import android.content.res.AssetManager;
import android.os.Environment;

import com.mayu.tiangou.app.GlobalVars;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;

/**
 * 
 * @author mayu
 *
 */
public class FileUtil {
	
	private static final String TAG = FileUtil.class.getSimpleName();

	/**
	 * 拷贝文件
	 * @param s
	 * @param t
	 */
    public static void fileChannelCopy(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            FileChannel in = fi.getChannel();//得到对应的文件通道
            FileChannel out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fo != null) fo.close();
                if (fi != null) fi.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 转换文件大小
     * @param fileLen
     * @return
     */
    public static String formatFileSizeToString(long fileLen) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileLen < 1024) {
            fileSizeString = df.format((double) fileLen) + "B";
        } else if (fileLen < 1048576) {
            fileSizeString = df.format((double) fileLen / 1024) + "K";
        } else if (fileLen < 1073741824) {
            fileSizeString = df.format((double) fileLen / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileLen / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 根据路径删除图片
     * @param file
     * @return
     * @throws IOException
     */
    public static boolean deleteFile(File file)throws IOException{
        return file != null && file.delete();
    }

    /***
     * 获取文件扩展名
     * @param filename
     * @return 返回文件扩展名
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }


    /**
     * 读取指定文件的输出
     */
    public static String getFileOutputString(String path) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path), 8192);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append("\n").append(line);
            }
            bufferedReader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
	
    /**
     * 拷贝Assets目录下文件
     * @param resourceFileName
     * @param targetFile
     */
	public static void copyFileFromAssets(String resourceFileName, String targetFile) {
		AssetManager am = GlobalVars.getContext().getAssets();
		try {
			InputStream fis = am.open(resourceFileName);
			OutputStream os = new FileOutputStream(new File(targetFile));

			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = fis.read(buff)) != -1) {
				os.write(buff, 0, len);
			}
			os.flush();
			os.close();
			fis.close();
		} catch (IOException e) {
			L.e(TAG, e.getMessage());
		}
	}

	public static boolean copyFile(File from, File to) {
		if (!from.isFile() || !to.isFile())
			return false;

		InputStream in = null;
		OutputStream out = null;

		try {
			in = new FileInputStream(from);
			out = new FileOutputStream(to);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
		} catch (Exception e) {
			L.e(TAG, e.toString());
			return false;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					/* Ignore */
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					/* Ignore */
				}
			}
		}
		return true;
	}

	public static boolean writeFile(byte[] data, File file) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			fos.write(data);
			fos.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 读取assets中的文本文件内容
	 * @param resourceFileName
	 * @return
	 */
	public static String readTextFromAssets(String resourceFileName) {
		AssetManager am = GlobalVars.getContext().getAssets();
		InputStream fis = null;
		ByteArrayOutputStream os = null;
		try {
			fis = am.open(resourceFileName);
			os = new ByteArrayOutputStream();

			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = fis.read(buff)) != -1) {
				os.write(buff, 0, len);
			}
			os.flush();
			byte[] byteArray = os.toByteArray();
			return new String(byteArray, "UTF-8");
		} catch (IOException e) {
			L.e(TAG, e.getMessage());
		} finally{
			try {
				os.close();
				fis.close();
			} catch (IOException e) {}
		}
		
		return null;
	}

	/**
	 * 获取应用私有目录
	 * @return
	 */
	public static String getSDApplictionDir() {
		boolean hasSd = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if (!hasSd) {
			return null;
		}
		String appPath = Environment.getExternalStorageDirectory() + "/appbase";
		File dir = new File(appPath);
		if (!dir.exists()) {
			if (!dir.mkdir()) {
				return null;
			} else {
				return appPath + "/";
			}
		} else {
			return appPath + "/";
		}
	}
}
