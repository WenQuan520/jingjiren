package cn.softbank.purchase.utils;

/***
 * 防止连续点击
 * @author GXH
 *
 */
public class OnClickContiNuousUtil {
	private static long lastClickTime;

	public synchronized static boolean isFastClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < 500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
	
	public synchronized static boolean isLowClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < 30000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
}
