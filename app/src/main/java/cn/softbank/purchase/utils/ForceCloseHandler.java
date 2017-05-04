package cn.softbank.purchase.utils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

/***
 * app 异常闪退异常报告 使用了android独有的UncaughtExceptionHandler
 * 
 * @author ld
 * 
 */
public class ForceCloseHandler implements Thread.UncaughtExceptionHandler {

	public static final String LOG_FILE_NAME = "forceclose.log";

	private static ForceCloseHandler inst;

	public static ForceCloseHandler getInstance() {
		if (inst == null)
			inst = new ForceCloseHandler();
		return inst;
	}

	private Context mContext;
	private Thread.UncaughtExceptionHandler mExceptionHandler;
	private JSONObject jsonObject;

	private ForceCloseHandler() {
	}

	public void init(Context context) {
		mContext = context;
		mExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	private void handleUncaughtException(Throwable ex) {
		if (ex == null)
			return;
		try {
			if (jsonObject == null)
				jsonObject = new JSONObject();
			saveDeviceInfo();
			saveForceCloseInfo2File(ex);
			// 把异常信息发送到服务器
			// 在这个地方也可以写接口 将错误的信息传送到服务器

		} catch (Exception e) {
			LogUtils.paint(Log.ERROR, "cn.baimifan.beautiful", mContext, e);
		}
	}

	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		handleUncaughtException(throwable);
		mExceptionHandler.uncaughtException(thread, throwable);
	}

	public void saveDeviceInfo() throws JSONException {
		TelephonyManager tm = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);

		jsonObject.put("platform", "android");
		jsonObject.put("model", Build.MODEL);
		jsonObject.put("trackid", "1.0");// 跟踪id 可配置
		jsonObject.put("product", "妈妈好");// 产品标识
		jsonObject.put("os_version", Build.VERSION.RELEASE);
		jsonObject.put("deviceid", "未知");// 设备id
		jsonObject.put("net_type", tm.getNetworkOperator());
		jsonObject.put("timestamp", System.currentTimeMillis());
		jsonObject.put("app_version", CommonUtils.getAppVersionCode(mContext));
	}

	private void saveForceCloseInfo2File(Throwable ex) throws Exception {
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString() + "\n";
		jsonObject.put("errorDetail", result);
//		jsonObject.toString();
		File dirFile = CommonUtils.getDirectory(mContext,true);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		String ExceptionDir = dirFile.getAbsolutePath()
				+ "/BeautifulException";

		if (true) {// 在这里可以配置 是否打印日志
			LogUtils.outputLog(ExceptionDir, result);
		}
	}

}