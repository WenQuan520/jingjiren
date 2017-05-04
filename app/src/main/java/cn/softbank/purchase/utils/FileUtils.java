/**
 * 项目名称:MamaHao1.0
 * 文件名称:FileUtils.java
 * 包名称:cn.atmobi.mamhao.utils
 * 日期:2015年8月11日下午5:28:27
 * Copyright (c) 2015, 杭州尽在网络技术有限公司 All Rights Reserved.
 *
 */
package cn.softbank.purchase.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

/**
 * @描述:
 * @Copyright Copyright (c) 2015
 * @Company 杭州尽在网络技术有限公司.
 * 
 * @author WL
 * @date 2015年8月11日下午5:28:27
 * @version 1.0
 */
@SuppressLint("NewApi") public abstract class FileUtils {
	public static final String PROJECT_NAME = "mamahao";
	public static final String ROOTPATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ PROJECT_NAME;

	public static boolean isDirExist(String dirPath) {
		File dir = new File(dirPath);
		return dir.exists() && dir.isDirectory();
	}

	/**
	 * 判断SD是否可以
	 * 
	 * @return
	 */
	public static boolean isSdcardExist() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static boolean isAvaiableSpace(int sizeMb) {
		boolean ishasSpace = false;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String sdcard = Environment.getExternalStorageDirectory().getPath();
			StatFs statFs = new StatFs(sdcard);
			long blockSize = statFs.getBlockSize();
			long blocks = statFs.getAvailableBlocks();
			long availableSpare = (blocks * blockSize) / (1024 * 1024);
			if (availableSpare > sizeMb) {
				ishasSpace = true;
			}
		}
		return ishasSpace;
	}

	public static boolean isAvaiableSpace(String path, int sizeMb) {
		boolean ishasSpace = false;

		StatFs statFs = new StatFs(path);
		long blockSize = statFs.getBlockSize();
		long blocks = statFs.getAvailableBlocks();
		long availableSpare = (blocks * blockSize) / (1024 * 1024);
		if (availableSpare > sizeMb) {
			ishasSpace = true;
		}
		return ishasSpace;
	}

	/**
	 * 判断SD卡上的文件是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean isFileExist(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}

	public static void checkRoot() {
		if (!isDirExist(ROOTPATH)) {
			createDir(ROOTPATH);
		}
	}

	public static List<String> getExtSDCardPaths() {
		List<String> paths = new CopyOnWriteArrayList<String>();
		String extFileStatus = Environment.getExternalStorageState();
		File extFile = Environment.getExternalStorageDirectory();
		if (extFileStatus.equals(Environment.MEDIA_MOUNTED) && extFile.exists()
				&& extFile.isDirectory() && extFile.canWrite()) {
			paths.add(extFile.getAbsolutePath());
		}
		try {
			// obtain executed result of command line code of 'mount', to judge
			// whether tfCard exists by the result
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec("mount");
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			int mountPathIndex = 1;
			while ((line = br.readLine()) != null) {
				// format of sdcard file system: vfat/fuse
				if ((!line.contains("fat") && !line.contains("fuse") && !line
						.contains("storage"))
						|| line.contains("secure")
						|| line.contains("asec")
						|| line.contains("firmware")
						|| line.contains("shell")
						|| line.contains("obb")
						|| line.contains("legacy") || line.contains("data")) {
					continue;
				}
				String[] parts = line.split(" ");
				int length = parts.length;
				if (mountPathIndex >= length) {
					continue;
				}
				String mountPath = parts[mountPathIndex];
				if (!mountPath.contains("/") || mountPath.contains("data")
						|| mountPath.contains("Data")) {
					continue;
				}
				File mountRoot = new File(mountPath);
				if (!mountRoot.exists() || !mountRoot.isDirectory()
						|| !mountRoot.canWrite()) {
					continue;
				}
				boolean equalsToPrimarySD = mountPath.equals(extFile
						.getAbsolutePath());
				if (equalsToPrimarySD) {
					continue;
				}
				paths.add(mountPath);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return paths;
	}

	public static void createDir(String... dirPath) {
		File dir = null;
		for (int i = 0; i < dirPath.length; i++) {
			dir = new File(dirPath[i]);
			if (!dir.exists() && !dir.isDirectory()) {
				dir.mkdirs();
			}
		}
	}

	public static void initFolders() {
		createDir(ROOTPATH);
	}

	public static String getBaseFilePath() {
		return ROOTPATH;
	}

	public static String getImageFolder() {
		createDir(ROOTPATH + File.separator + "image");
		return ROOTPATH + File.separator + "image";
	}

	public static String getImageSaveFolder() {
		createDir(ROOTPATH + File.separator + "save");
		return ROOTPATH + File.separator + "save";
	}

	public static String getTxtFolder() {
		createDir(ROOTPATH + File.separator + "txt");
		return ROOTPATH + File.separator + "txt";
	}

	public static String getDynamicFolder() {
		createDir(ROOTPATH + File.separator + "dynamic");
		return ROOTPATH + File.separator + "dynamic";
	}

	public static String getChatFolder(int friendUid) {
		createDir(ROOTPATH + File.separator + "chat" + File.separator
				+ friendUid);
		return ROOTPATH + File.separator + "chat" + File.separator + friendUid;
	}

	public static String getFavImageFolder() {
		createDir(ROOTPATH + File.separator + "fav");
		return ROOTPATH + File.separator + "fav";
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static File createSDFile(String path) throws IOException {
		File file = new File(path);
		if (!file.exists())
			file.createNewFile();

		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 * @return
	 */
	public static File createSDDir(String dirName) {
		File file = new File(dirName);
		if (!file.exists())
			file.mkdir();
		return file;
	}

	public static void createFileDir(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			File parentFile = file.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			parentFile = null;
		}
		file = null;
	}

	public static boolean deleteFile(String fileName) {
		try {
			if (fileName == null) {
				return false;
			}
			File f = new File(fileName);

			if (f == null || !f.exists()) {
				return false;
			}

			if (f.isDirectory()) {
				return false;
			}
			return f.delete();
		} catch (Exception e) {
			// Log.d(FILE_TAG, e.getMessage());
			return false;
		}
	}

	public static boolean deleteFileOfDir(String dirName, boolean isRecurse) {
		boolean blret = false;
		try {
			File f = new File(dirName);
			if (f == null || !f.exists()) {
				// Log.d(FILE_TAG, "file" + dirName + "not isExist");
				return false;
			}

			if (f.isFile()) {
				blret = f.delete();
				return blret;
			} else {
				File[] flst = f.listFiles();
				if (flst == null || flst.length <= 0) {
					return true;
				}

				int filenumber = flst.length;
				File[] fchilda = f.listFiles();
				for (int i = 0; i < filenumber; i++) {
					File fchild = fchilda[i];
					if (fchild.isFile()) {
						blret = fchild.delete();
						if (!blret) {
							break;
						}
					} else if (isRecurse) {
						blret = deleteFileDir(fchild.getAbsolutePath(), true);
					}
				}
			}
		} catch (Exception e) {
			blret = false;
		}

		return blret;
	}

	public static boolean deleteFileDir(String dirName, boolean isRecurse) {
		boolean blret = false;
		try {
			File f = new File(dirName);
			if (f == null || !f.exists()) {
				// Log.d(FILE_TAG, "file" + dirName + "not isExist");
				return false;
			}
			if (f.isFile()) {
				blret = f.delete();
				return blret;
			} else {
				File[] flst = f.listFiles();
				if (flst == null || flst.length <= 0) {
					f.delete();
					return true;
				}
				int filenumber = flst.length;
				File[] fchilda = f.listFiles();
				for (int i = 0; i < filenumber; i++) {
					File fchild = fchilda[i];
					if (fchild.isFile()) {
						blret = fchild.delete();
						if (!blret) {
							break;
						}
					} else if (isRecurse) {
						blret = deleteFileDir(fchild.getAbsolutePath(), true);
					}
				}

				// 删除当前文件夹
				blret = new File(dirName).delete();
			}
		} catch (Exception e) {
			// Log.d(FILE_TAG, e.getMessage());
			blret = false;
		}

		return blret;
	}

	/**
	 * 移动文件
	 * 
	 * @param filePath
	 */
	public static void removeToDir(String filePath, String toFilePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			return;
		}
		file.renameTo(new File(toFilePath));
	}

	/**
	 * 把图片压缩到200K
	 * 
	 * @param oldpath
	 *            压缩前的图片路径
	 * @param newPath
	 *            压缩后的图片路径
	 * @return
	 */
	public static File compressFile(String oldpath, String newPath) {
		Bitmap compressBitmap = FileUtils.decodeFile(oldpath);
		
		Bitmap newBitmap = comp(compressBitmap);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		newBitmap.compress(CompressFormat.PNG, 100, baos);

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		newBitmap.compress(CompressFormat.PNG, 100, os);
		byte[] bytes = os.toByteArray();

		File file = null;
		try {
			file = FileUtils.getFileFromBytes(bytes, newPath);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (newBitmap != null) {
				if (!newBitmap.isRecycled()) {
					newBitmap.recycle();
				}
				newBitmap = null;
			}
			if (compressBitmap != null) {
				if (!compressBitmap.isRecycled()) {
					compressBitmap.recycle();
				}
				compressBitmap = null;
			}
		}
		return file;
	}
	
	public static Bitmap comp(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 100, baos);
//		if (baos.toByteArray().length / 1024 > 1024 * 2) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(CompressFormat.JPEG, 60, baos);// 这里压缩50%，把压缩后的数据存放到baos中
//		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return bitmap;// 压缩好比例大小后再进行质量压缩
	}
	
	public static long getBitmapsize(Bitmap bitmap) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			return bitmap.getByteCount();
		}
		// Pre HC-MR1
		return bitmap.getRowBytes() * bitmap.getHeight();

	}

	private static Bitmap ratingImage(String filePath, Bitmap bitmap) {
		int degree = readPictureDegree(filePath);
		return rotaingImageView(degree, bitmap);
	}

	/**
	 * 旋转图片
	 * 
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		;
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
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
	 * 把字节数组保存为一个文件
	 * 
	 * @param b
	 * @param outputFile
	 * @return
	 */
	public static File getFileFromBytes(byte[] b, String outputFile) {
		File ret = null;
		BufferedOutputStream stream = null;
		try {
			ret = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(ret);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			// log.error("helper:get file from byte process error!");
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// log.error("helper:get file from byte process error!");
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	/**
	 * 图片压缩
	 * 
	 * @param fPath
	 * @return
	 */
	public static Bitmap decodeFile(String fPath) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		opts.inDither = false; // Disable Dithering mode
		opts.inPurgeable = true; // Tell to gc that whether it needs free
		opts.inInputShareable = true; // Which kind of reference will be used to
		BitmapFactory.decodeFile(fPath, opts);
		final int REQUIRED_SIZE = 200;
		int scale = 1;
		if (opts.outHeight > REQUIRED_SIZE || opts.outWidth > REQUIRED_SIZE) {
			final int heightRatio = Math.round((float) opts.outHeight
					/ (float) REQUIRED_SIZE);
			final int widthRatio = Math.round((float) opts.outWidth
					/ (float) REQUIRED_SIZE);
			scale = heightRatio < widthRatio ? heightRatio : widthRatio;//
		}
		Log.i("scale", "scal =" + scale);
		opts.inJustDecodeBounds = false;
		opts.inSampleSize = scale;
		Bitmap bm = BitmapFactory.decodeFile(fPath, opts).copy(
				Config.ARGB_8888, false);
		return bm;
	}

	/**
	 * 创建目录
	 * 
	 * @param path
	 */
	public static void setMkdir(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
			Log.e("file", "目录不存在  创建目录    ");
		} else {
			Log.e("file", "目录存在");
		}
	}

	/**
	 * 获取目录名称
	 * 
	 * @param url
	 * @return FileName
	 */
	public static String getFileName(String url) {
		int lastIndexStart = url.lastIndexOf("/");
		if (lastIndexStart != -1) {
			return url.substring(lastIndexStart + 1, url.length());
		} else {
			return null;
		}
	}

	/**
	 * 删除该目录下的文件
	 * 
	 * @param path
	 */
	public static void delFile(String path) {
		if (!TextUtils.isEmpty(path)) {
			File file = new File(path);
			if (file.exists()) {
				file.delete();
			}
		}
	}

}
