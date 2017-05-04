package cn.softbank.purchase.utils;

import cn.softbank.purchase.base.MyApplication;
import cn.softbank.purchase.domain.LoginData;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {

	public static final String login = "login";

	public static void saveToSP(Context context, String key, Object value) {
		SharedPreferences sp = context.getSharedPreferences("config", 0);
		
		if (value instanceof String) {
			sp.edit().putString(key, (String) value).commit();
		} else if (value instanceof Boolean) {
			sp.edit().putBoolean(key, (Boolean) value).commit();
		} else if (value instanceof Float) {
			sp.edit().putFloat(key, (Float) value).commit();
		} else if (value instanceof Integer) {
			sp.edit().putInt(key, (Integer) value).commit();
		} else if (value instanceof Long) {
			sp.edit().putLong(key, (Long) value).commit();
		}

	}

	public static void saveToSP(String config, Context context, String key,
			Object value) {
		SharedPreferences sp = context.getSharedPreferences(config, 0);
		if (value instanceof String) {
			sp.edit().putString(key, (String) value).commit();
		} else if (value instanceof Boolean) {
			sp.edit().putBoolean(key, (Boolean) value).commit();
		} else if (value instanceof Float) {
			sp.edit().putFloat(key, (Float) value).commit();
		} else if (value instanceof Integer) {
			sp.edit().putInt(key, (Integer) value).commit();
		} else if (value instanceof Long) {
			sp.edit().putLong(key, (Long) value).commit();
		}

	}

	public static String getString(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences("config", 0);
		return sp.getString(key, "");
	}

	public static int getInt(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences("config", 0);
		return sp.getInt(key, 0);
	}
	public static long getLong(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences("config", 0);
		return sp.getLong(key, 0);
	}

	public static float getFloat(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences("config", 0);
		return sp.getFloat(key, 0.0f);
	}

	public static float getDouble(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences("config", 0);
		return sp.getFloat(key, 0);
	}

	public static boolean getBoolean(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences("config", 0);
		return sp.getBoolean(key, false);
	}

	public static boolean getBoolean(String config, Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(config, 0);
		return sp.getBoolean(key, false);
	}

	public static void CleanData(Context context) {
		SharedPreferences sp = context.getSharedPreferences("config", 0);
		sp.edit().clear();
	}
	
	public static void saveUserData(Context context,LoginData d,boolean savePic) {
		saveToSP(context, "userId", d.getUserId());
		saveToSP(context, "kehujingliName", d.getKehujingliName());
		saveToSP(context, "alipay", d.getAlipay());
		saveToSP(context, "kehujingliPhone", d.getKehujingliPhone());
		if(savePic)
			saveToSP(context, "avatar_url", d.getAvatar_url());
		MyApplication.getInstance().setMemberId(d.getUserId());
	}
	
	public static void clearUserData(Context context) {
		saveToSP(context, "userId", "");
		saveToSP(context, "kehujingliName", "");
		saveToSP(context, "kehujingliPhone", "");
		saveToSP(context, "avatar_url", "");
		saveToSP(context, "alipay", "");
		MyApplication.getInstance().setMemberId("");
		MyApplication.getInstance().setMemberName("");
	}
	
	public static LoginData getUserData(Context context){
		return new LoginData(
				getString(context, "userId"), 
				getString(context, "avatar_url"), 
				getString(context, "kehujingliName"), 
				getString(context, "kehujingliPhone"), 
				getString(context, "alipay"));
	}
	
}
