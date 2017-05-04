package cn.softbank.purchase.utils;

import com.google.gson.Gson;

/**
 * 可供任意类使用的通用toString方法
 * 
 * @author zbr
 * 
 */
public class ObjectAnalyzer {

	public static String toString(Object obj) {
		try {
			Gson g = new Gson();
			String jsonStr = g.toJson(obj);
			return jsonStr;
		} catch (Exception e) {
			// TODO: handle exception
			return obj.toString();
		}
	}

}
