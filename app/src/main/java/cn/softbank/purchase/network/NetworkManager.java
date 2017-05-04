package cn.softbank.purchase.network;


import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import cn.softbank.purchase.utils.Constant;
import cn.softbank.purchase.utils.MD5;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * @Copyright Copyright (c) 2016
 * @Company 广州软银信息科技有限科技.
 * @author GXH
 * @version 1.0
 */
public class NetworkManager {
    /** 没有网络 **/
    public static final int NETWORKTYPE_INVALID = 0;
    /** wap网络 **/
    public static final int NETWORKTYPE_WAP = 1;
    /** 2G网络 **/
    public static final int NETWORKTYPE_2G = 2;
    /** 3G和3G以上网络，或者统称为快速网络 **/
    public static final int NETWORKTYPE_3G = 3;
    /** wifi网络 **/
    public static final int NETWORKTYPE_WIFI = 4;

    /**
     * @字段 mInstance
     * @功能描述：网络状态管理类单例
     * @创建人 WL
     * @创建时间 2015年7月28日上午9:38:01
     */
    private static NetworkManager mInstance;
    /**
     * @字段 mContext
     * @功能描述：应用上下文
     * @创建人 WL
     * @创建时间 2015年7月28日上午9:38:20
     */
    private final Context mContext;

    /**
     * @字段 mCurrentNetworkState
     * @功能描述：当前网络状态类
     * @创建人 WL
     * @创建时间 2015年7月28日上午9:38:35
     */
    private NetworkState mCurrentNetworkState;
    /**
     * @字段 mConnectivityManager
     * @功能描述：管理与网络连接相关的系统类
     * @创建人 WL
     * @创建时间 2015年7月28日上午9:39:19
     */
    private final ConnectivityManager mConnectivityManager;

    /**
     * @描述 NetworkManager单例实例化方法
     * @方法名 init
     * @param context
     * @返回类型 void
     * @创建人 WL
     * @创建时间 2015年7月28日上午9:44:53
     * @修改人 WL
     * @修改时间 2015年7月28日上午9:44:53
     * @修改备注 无
     * @since 无
     * @throws 无
     */
    public static void init(Context context) {
        if (mInstance == null) {
            mInstance = new NetworkManager(context);
        } else {
            throw new IllegalStateException("NetworkManager has been inited");
        }
    }

    /**
     * @描述 获取NetworkManager单例方法
     * @方法名 getInstance
     * @return
     * @返回类型 NetworkManager
     * @创建人 WL
     * @创建时间 2015年7月28日上午9:46:11
     * @修改人 WL
     * @修改时间 2015年7月28日上午9:46:11
     * @修改备注 无
     * @since 无
     * @throws 无
     */
    public static NetworkManager getInstance() {
        if (mInstance == null) {
            throw new IllegalStateException("NetworkManager has not been inited");
        }
        return mInstance;
    }

    /**
     * @描述 NetworkManager私有构造
     * @param context
     */
    private NetworkManager(Context context) {
        this.mContext = context;
        this.mConnectivityManager = (ConnectivityManager) this.mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);

    }

    /**
     * @描述 网络状态获取
     * @方法名 getNetworkState
     * @return
     * @返回类型 NetworkState
     * @创建人 WL
     * @创建时间 2015年7月28日上午9:47:26
     * @修改人 WL
     * @修改时间 2015年7月28日上午9:47:26
     * @修改备注 无
     * @since 无
     * @throws 无
     */
    public synchronized NetworkState getNetworkState() {
        updateNetworkInfo();
        return mCurrentNetworkState;
    }

    /**
     * @描述更新网络状态
     * @方法名 updateNetworkInfo
     * @返回类型 void
     * @创建人 WL
     * @创建时间 2015年7月28日上午9:53:40
     * @修改人 WL
     * @修改时间 2015年7月28日上午9:53:40
     * @修改备注 无
     * @since 无
     * @throws 无
     */
    public synchronized void updateNetworkInfo() {
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();

        // 默认网络状态为无连接，无效网络状态
        NetworkState networkState = new NetworkState(false, NETWORKTYPE_INVALID);
        if (networkInfo != null && networkInfo.isAvailable()
                && networkInfo.isConnected()) {
            networkState.mIsConnected = true;

            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                networkState.mNetworkType = NETWORKTYPE_WIFI;
            } else {
                String proxyHost = android.net.Proxy.getDefaultHost();
                networkState.mNetworkType = TextUtils.isEmpty(proxyHost)
                        ? (isFastMobileNetwork() ? NETWORKTYPE_3G : NETWORKTYPE_2G)
                        : NETWORKTYPE_WAP;
            }

        }

        // 判断是否网络状态是否和之前的有变化，避免频繁的通知网络状态变更
        if (!networkState.equals(mCurrentNetworkState)) {
            mCurrentNetworkState = networkState;
//            EventBus.getDefault().post(new NetworkMessageEvent(mCurrentNetworkState.mIsConnected,
//                    mCurrentNetworkState.mNetworkType));
        }
    }

    /**
     * @描述 判断是否快速网络
     * @方法名 isFastMobileNetwork
     * @return
     * @返回类型 boolean
     * @创建人 WL
     * @创建时间 2015年7月28日上午10:39:24
     * @修改人 WL
     * @修改时间 2015年7月28日上午10:39:24
     * @修改备注 无
     * @since 无
     * @throws 无
     */
    private boolean isFastMobileNetwork() {
        TelephonyManager tm = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        switch (tm.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }

    public class NetworkState {
        private boolean mIsConnected;
        private int mNetworkType;

        private NetworkState(boolean isConnected, int networkType) {
            this.mIsConnected = isConnected;
            this.mNetworkType = networkType;
        }

        @Override
        public boolean equals(Object o) {
            if (o != null && o instanceof NetworkState) {
                NetworkState state = (NetworkState) o;
                return (mIsConnected == state.mIsConnected)
                        && (mNetworkType == state.mNetworkType);
            }
            return false;
        }

        public boolean isConnected() {
            return mIsConnected;
        }

        public int getNetWorkType() {
            return mNetworkType;
        }
    }
    
    public class NetworkMessageEvent {
        public final int mNetworkType;

        public final boolean mIsConnect;

        public NetworkMessageEvent(boolean isConnect, int networkType) {
            this.mIsConnect = isConnect;
            this.mNetworkType = networkType;
        }
    }
    
    /**
     * 生成token值
     * @param apicode
     * @param params
     * @return
     */
    public String getPostToken(Map<String,String> params){
    	//生成token
    	StringBuilder access_token = new StringBuilder();
    	
    	Map<String, String> sortMap = new TreeMap<String, String>(
				new Comparator<String>() {

					@Override
					public int compare(String str1, String str2) {
						// TODO Auto-generated method stub
						return str1.compareTo(str2);
					}
				});

		sortMap.putAll(params);
    	
    	for (String value : sortMap.values()) {  
    		if(!TextUtils.isEmpty(value))
    			access_token.append(value);
    	} 
    	
    	access_token.append(Constant.ACCESS_KEY);
    	
    	return MD5.Md5(access_token.toString());
    }
    
}
