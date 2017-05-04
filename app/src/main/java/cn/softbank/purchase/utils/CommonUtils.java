/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.softbank.purchase.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CommonUtils {

	private static final String[] regs = { "（", "）","【","】", "(", ")", "[", "]" };

	public static String getFormatEnglish(String str) {
		if (TextUtils.isEmpty(str)) {
			return str;
		}
		for (int i = 0; i < regs.length / 2; i++) {
			str = str.replaceAll(regs[i], regs[i + regs.length / 2]);
		}
		return str;
	}
	
	/* 
     * 将时间戳转换为时间
     */
    @SuppressLint("SimpleDateFormat")
	public static String stampToDate(long lt){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(lt*1000);
        res = simpleDateFormat.format(date);
        return res;
    }
    
    /* 
     * 将时间戳转换为时间
     */
    @SuppressLint("SimpleDateFormat")
    public static String stampToDate2(long lt){
    	String res;
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
    	Date date = new Date(lt);
    	res = simpleDateFormat.format(date);
    	return res;
    }

	/****
	 * 统一管理 关于价格的显示 带前缀¥
	 * 
	 * @param price
	 * @return
	 */
	public static String getPriceFormat(float price) {
		return new StringBuilder("¥").append(new java.text.DecimalFormat("#.##").format(price)).toString();
	}
	
	public static String getPriceFormat(double price) {
		return new StringBuilder("¥").append(new java.text.DecimalFormat("#.##").format(price)).toString();
	}
	public static String getPriceFormatNo¥(double price) {
		return new StringBuilder("").append(new java.text.DecimalFormat("#.##").format(price)).toString();
	}
	
	public static String getPriceFormatClean(float price) {
		return new java.text.DecimalFormat("#.##").format(price);
	}
	
	public static void setOriginalPrice(TextView tv,float price) {
		tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		tv.setText(price==0?"":"¥"+price);
	}
	
	public static double get2BigDecale(double doubles){
		BigDecimal c = new BigDecimal(doubles);
		BigDecimal d=c.setScale(2,BigDecimal.ROUND_HALF_UP);
		return d.doubleValue();
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public static int getGridViewHeight(GridView mGridView) {
		ListAdapter listAdapter = mGridView.getAdapter();
		if (listAdapter == null) {
			return 0;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, mGridView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		return totalHeight;
	}

	/**
	 * 验证邮箱地址是否正确 　　
	 * 
	 * @param email
	 * @return 　　
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 验证手机号码 　　
	 * 
	 * @param mobiles
	 * @return [0-9]{5,9} 　　
	 */
	public static boolean isMobileNO(String mobiles) {
		boolean flag = false;
		try {
			Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
			Matcher m = p.matcher(mobiles);
			flag = m.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}

		return false;
	}

	/**
	 * 检测Sdcard是否存在
	 * 
	 * @return
	 */
	public static boolean isExitsSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	static String getString(Context context, int resId) {
		return context.getResources().getString(resId);
	}

	public static String getTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

		if (runningTaskInfos != null)
			return runningTaskInfos.get(0).topActivity.getClassName();
		else
			return "";
	}

	/**
	 * 根据资源ID获得drawable对象
	 * 
	 * @param mcontext
	 * @param resid
	 * @return
	 */
	public static Drawable GetDrawable(Context mcontext, int resid) {
		Drawable drawable = mcontext.getResources().getDrawable(resid);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		return drawable;
	}

	/**
	 * 当前版本
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppVersion(Context context) {
		String version = "";
		try {
			version = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * 当前版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppVersionCode(Context context) {
		String version = "-1";
		try {
			version = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode+"";
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 得到手机屏幕宽高数组
	 */
	public static int[] getScreenSize(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getApplicationContext().getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		int screenHeigh = dm.heightPixels;
		int[] xy = new int[] { screenWidth, screenHeigh };
		return xy;
	}

	/**
	 * 由参数生成json格式的字符串
	 * 
	 * @param screenings
	 *            String数组
	 * @return
	 */
	public static String getJsonStr(String[] keys, Object[] values) {
		JSONObject json = new JSONObject();
		try {
			for (int i = 0; i < keys.length; i++) {
				if (values[i] instanceof String) {
					if (!TextUtils.isEmpty((String) values[i]))
						json.put(keys[i], values[i]);
				} else
					json.put(keys[i], values[i]);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json.toString();
	}

	/**
	 * 得到状态栏的高度
	 * 
	 * @param activity
	 * @return
	 */
	public static int getStatusHeight(Activity activity) {
		int statusHeight = 0;
		Rect localRect = new Rect();
		activity.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass
						.getField("status_bar_height").get(localObject)
						.toString());
				statusHeight = activity.getResources()
						.getDimensionPixelSize(i5);
			} catch (Exception e) {

			}
		}
		return statusHeight;
	}

	/**
	 * 得到图片缩略图
	 */
	public static Bitmap getImageThumbnail(String imagePath, int width,
			int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/**
	 * 打电话
	 * 
	 * @param phoneNum
	 */
	public static void makeCall(Context context, String phoneNum) {
		// TODO 向IM功能跳转
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:" + phoneNum));
		context.startActivity(intent);
	}

	/***
	 * 跳转拨号界面
	 * 
	 * @param context
	 * @param phoneNum
	 */
	public static void readyCall(Context context, String phoneNum) {
		// TODO 跳转拨号界面
		if(!TextUtils.isEmpty(phoneNum)){
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ phoneNum));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
		
	}

	/**
	 * 
	 * @param context
	 * @param imgRescouce
	 * @param oriText
	 * @param startPoint
	 * @return
	 */
	public static SpannableStringBuilder getImgText(Context context,
			int imgRescouce, String oriText, int startPoint) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				imgRescouce);
		String newText = new StringBuilder(oriText.substring(0, startPoint))
				.append("a").append(oriText.substring(startPoint)).toString();
		SpannableStringBuilder builder = new SpannableStringBuilder(newText);
		ImageSpan span = new ImageSpan(context, bitmap);
		builder.setSpan(span, startPoint, startPoint + 1,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return builder;
	}

	/**
	 * 解析errorCode
	 * 
	 * @param response
	 * @return
	 */
	public static String getErrorCodes(String response) {
		String errorCode = "";
		try {
			JSONObject json = new JSONObject(response);
			errorCode = json.getString("error_code");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return errorCode;
	}

	/**
	 * 解析errorMessage
	 * 
	 * @param response
	 * @return
	 */
	public static String getErrorMessage(String response) {
		String errorMessage = "";
		try {
			JSONObject json = new JSONObject(response);
			errorMessage = json.getString("error");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return errorMessage;
	}

	/**
	 * 带删除线的价格显示
	 * @param tv 
	 * @param oriPrice 原价
	 * @param newPrice 新价
	 * @param control 是否控制显示隐藏
	 */
	public static void setOriginalPrice(TextView tv,float oriPrice,float newPrice,boolean control) {
		if(oriPrice>newPrice){
			if(control)
				tv.setVisibility(View.VISIBLE);
			tv.setText(getPriceFormat(oriPrice));
			tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		}else if(control)
			tv.setVisibility(View.GONE);
	}
	
	/**
	 * 设置文本标签，降和惠
	 * @param reduce_tv
	 * @param discount_tv
	 * @param proLabel
	 */
	public static void setProLabelText(View reduce_tv,View discount_tv,String proLabel){
		if(TextUtils.isEmpty(proLabel)){
			reduce_tv.setVisibility(View.GONE);
			discount_tv.setVisibility(View.GONE);
			return;
		}
		//降
		if(proLabel.contains("1"))
			reduce_tv.setVisibility(View.VISIBLE);
		else
			reduce_tv.setVisibility(View.GONE);
		//惠
		if(proLabel.contains("2") || proLabel.contains("5"))
			discount_tv.setVisibility(View.VISIBLE);
		else
			discount_tv.setVisibility(View.GONE);
	}
	
	/**
	 * 整数(秒数)转换为时分秒格式(xx:xx:xx)
	 * 
	 * @param time
	 * @return
	 */
	public static String secToTime(long time) {
		long hour = 0;
		long minute = 0;
		long second = 0;
		if (time <= 0)
			return "00:00";
		StringBuilder timeStr = new StringBuilder();
		minute = time / 60;
		if (minute < 60) {
			second = time % 60;
			timeStr.append(unitFormat((int)minute)).append(":")
					.append(unitFormat((int)second));
		} else {
			hour = minute / 60;
			if (hour > 24)
				return hour/24+"天";
			minute = minute % 60;
			second = time - hour * 3600 - minute * 60;
			timeStr.append(unitFormat((int)hour)).append(":")
					.append(unitFormat((int)minute));
//			.append(":")
//					.append(unitFormat((int)second));
		}
		return timeStr.toString();
	}

	public static String unitFormat(int i) {
		StringBuilder retStr = new StringBuilder();
		if (i >= 0 && i < 10)
			retStr.append("0").append(Integer.toString(i));
		else
			retStr.append(i);
		return retStr.toString();
	}

	/***
	 * 正常使用被屏蔽的 url
	 */
	public static final String LOCALAPKNAME = "mamahao";

	public static String getLocalApkName(Context context) {
		return LOCALAPKNAME + "V" + (getAppVersionCode(context) + 1) + ".apk";
	}

	public static File getDirectory(Context context, boolean preferExternal) {
		String externalStorageState;
		File appCacheDir = null;
		try {
			externalStorageState = Environment.getExternalStorageState();
		} catch (NullPointerException e) {
			externalStorageState = "";
		}
		if ((preferExternal) && ("mounted".equals(externalStorageState))
				&& (hasExternalStoragePermission(context))) {
			appCacheDir = getExternalCacheDir(context, "Exception");
		}
		if (appCacheDir == null) {
			appCacheDir = context.getCacheDir();
		}
		if (appCacheDir == null) {
			String cacheDirPath = "/data/data/" + context.getPackageName()
					+ "/Exception/";
			appCacheDir = new File(cacheDirPath);
		}
		return appCacheDir;
	}

	public static File getDirectory(Context context, boolean preferExternal,
			String filename) {
		String externalStorageState;
		File appCacheDir = null;
		try {
			externalStorageState = Environment.getExternalStorageState();
		} catch (NullPointerException e) {
			externalStorageState = "";
		}
		if ((preferExternal) && ("mounted".equals(externalStorageState))
				&& (hasExternalStoragePermission(context))) {
			appCacheDir = getExternalCacheDir(context, filename);
		}
		if (appCacheDir == null) {
			appCacheDir = context.getCacheDir();
		}
		if (appCacheDir == null) {
			String cacheDirPath = "/data/data/" + context.getPackageName()
					+ "/" + filename + "/";
			appCacheDir = new File(cacheDirPath);
		}
		return appCacheDir;
	}

	private static File getExternalCacheDir(Context context, String fileName) {
		File dataDir = new File(new File(
				Environment.getExternalStorageDirectory(), "Android"), "data");
		File appCacheDir = new File(
				new File(dataDir, context.getPackageName()), fileName);
		if (!(appCacheDir.exists())) {
			if (!(appCacheDir.mkdirs())) {
				return null;
			}
			try {
				new File(appCacheDir, ".nomedia").createNewFile();
			} catch (IOException e) {
			}
		}
		return appCacheDir;
	}

	private static boolean hasExternalStoragePermission(Context context) {
		int perm = context
				.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
		return (perm == 0);
	}

	/** 
     * 将sp值转换为px值，保证文字大小不变 
     *  
     * @param spValue 
     * @param fontScale 
     *            （DisplayMetrics类中属性scaledDensity） 
     * @return 
     */  
    public static int sp2px(Context context, float spValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (spValue * fontScale + 0.5f);  
    }  
    /**
     * 转换类型
     * 
     * @param str
     * @return
     */
    public static long passLong(String str) {
        if (str != null && !"".equals(str) && !"null".equals(str)) {
            try {
                Long longTime = Long.parseLong(str);
                return longTime;
            } catch (Exception e) {
                return 0;
            }
        } else {
            return 0;
        }
    }
    
    /***
     * 获取状态栏高度
     * @return
     */
    public static int getStatusBar(Activity mContext) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sbar = mContext.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return sbar;
	}

    /**
     * 转换类型
     * 
     * @param str
     * @return
     */
    public static double passDouble(String str) {
        if (str != null && !"".equals(str) && !"null".equals(str)) {
            try {
                Double doubleStr = Double.parseDouble(str);
                return doubleStr;
            } catch (Exception e) {
                return 0;
            }
        } else {
            return 0;
        }
    }

	/**
	 * 得到安装子手机上的所有应用程序信息
	 * 微信包名:"com.tencent.mm"
	 * @return
	 */
	public static boolean checkInstalledApp(Context context,String packageName){
		PackageManager pm=context.getPackageManager();
		List<PackageInfo> packageInfos=pm.getInstalledPackages(0);
		for(PackageInfo pki:packageInfos){
			//启动程序
			if(pki.packageName.equals(packageName))
				return true;

		}
		return false;
	}
	
	public static boolean isNumeric(String str){
		  for (int i = str.length();--i>=0;){   
		   if (!Character.isDigit(str.charAt(i))){
		    return false;
		   }
		  }
		  return true;
	}
	
}
