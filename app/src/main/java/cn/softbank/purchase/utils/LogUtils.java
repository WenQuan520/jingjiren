package cn.softbank.purchase.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

/**
 * 
 * @author ld
 * 
 */
public class LogUtils {

	private static final boolean LOG_MODE = true;
	private static final String TAG = "LDKJ";

	private LogUtils() {
	}

	public static void paintE(String msg, Object o) {
		paint(Log.ERROR, msg, o, null);
	}

	public static void paintW(String msg, Object o) {
		paint(Log.WARN, msg, o, null);
	}

	public static void paintD(String msg, Object o) {
		paint(Log.DEBUG, msg, o, null);
	}

	public static void paint(int type, String msg, String tag) {
		outputLog(type, tag + ": " + msg, null);
	}

	public static void paint(int type, String msg, Object o, Exception e) {
		String className = o.getClass().getSimpleName();
		msg = className + ": " + msg;
		outputLog(type, msg, e);
	}

	private static void outputLog(int type, String msg, Exception e) {
		if (!LOG_MODE)
			return;

		switch (type) {
		case Log.ASSERT:
			break;
		case Log.ERROR:
			Log.e(TAG, msg, e);
			break;
		case Log.WARN:
			Log.w(TAG, msg);
			break;
		case Log.DEBUG:
			Log.d(TAG, msg);
			break;
		case Log.INFO:
			Log.i(TAG, msg);
			break;
		case Log.VERBOSE:
			Log.v(TAG, msg);
			break;
		}
	}

	public static void outputLog(String directorypath, String msg) {
		File directory = new File(directorypath);
		if (!directory.isDirectory()) {
			directory.mkdirs();
		}

		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd hh-mm-ss");// �������ڸ�ʽ
		String fileName = df.format(new Date());
		File f = new File(directorypath + "/" + fileName + "log.txt");
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(f, true));
			writer.append(msg + "\r\n");
			writer.flush();
			writer.close();
		} catch (Exception e) {
			outputLog(Log.ERROR, "outputLog : Exception", e);
		}
	}

	public static boolean isDebug() {
		return LOG_MODE;
	}
}
