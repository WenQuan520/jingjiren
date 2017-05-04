package cn.softbank.purchase.utils;
/**
 * 获取两个经纬度之间的距离
 * @author Administrator
 *
 */
public class DistanceUtils {
	private final double EARTH_RADIUS = 6378137.0;
	
	private static DistanceUtils instance = null;
	
	public static DistanceUtils getInstance() {
        synchronized (DistanceUtils.class) {
            if (instance == null) {
                instance = new DistanceUtils();
            }
        }
        return instance;
    }

    private DistanceUtils() {}

	// 返回单位是米
	public double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
		double Lat1 = rad(latitude1);
		double Lat2 = rad(latitude2);
		double a = Lat1 - Lat2;
		double b = rad(longitude1) - rad(longitude2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(Lat1) * Math.cos(Lat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	private double rad(double d) {
		return d * Math.PI / 180.0;
	}
	/**
	 * 返回转换好的字符串  超过1000显示km单位
	 * @param longitude1
	 * @param latitude1
	 * @param longitude2
	 * @param latitude2
	 * @return
	 */
	public String getDistanceStr(double longitude1, double latitude1, double longitude2, double latitude2) {
		float distance=(float)getDistance(longitude1, latitude1, longitude2, latitude2);
		
		StringBuilder distanceStr=new StringBuilder();
		if(distance>1000)
			distanceStr.append(CommonUtils.getPriceFormatClean(distance/1000)).append("km").toString();
		else
			distanceStr.append(CommonUtils.getPriceFormatClean(distance)).append("m").toString();
		return distanceStr.toString();
	}
}
