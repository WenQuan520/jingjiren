package cn.softbank.purchase.client;


import android.content.Context;
import android.text.TextUtils;
import cn.softbank.purchase.domain.DeliveryAddr;
import cn.softbank.purchase.network.NetworkManager;
import cn.softbank.purchase.network.ReqTag;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestQueue.RequestFilter;
import com.android.volley.toolbox.Volley;


/**
 * @Copyright Copyright (c) 2016
 * @Company 广州软银信息科技有限科技
 * @author GXH
 * @version 1.0
 */
public class PurchaseApi{
    private static PurchaseApi mApi;
    private Context mContext;
    private RequestQueue mQueue;
 // 默认地址信息
    private DeliveryAddr mAddr;

    private PurchaseApi(Context context) {
        this.mContext = context;

        // �?��请求队列框架
        mQueue = Volley.newRequestQueue(mContext);
        mQueue.start();

        // 初始化网络管理器
        NetworkManager.init(mContext);

    }

    public static void init(Context context) {
        if (mApi == null) {
            mApi = new PurchaseApi(context);
        } else {
            throw new IllegalArgumentException("HttpApi has been inited!");
        }
    }

    public static PurchaseApi getInstance() {
        if (mApi == null) {
            throw new IllegalArgumentException("HttpApi don't inited!");
        }
        return mApi;
    }

    public <T> void add(Request<T> req) {
        mQueue.add(req);
    }

    /**
     * @描述 取消和给定的<code>reqTag</code>相同的处于请求队�?code>RequestQueue</code>�?code>Request</code>
     * @方法�?cancel
     * @param reqTag
     * @返回类型 void
     * @创建�?WL
     * @创建时间 2015�?�?0日下�?:57:32
     * @修改�?WL
     * @修改时间 2015�?�?0日下�?:57:32
     * @修改备注 �?
     * @since �?
     * @throws �?
     */
    public void cancel(final int reqId) {
        
        mQueue.cancelAll(new RequestFilter() {

            @Override
            public boolean apply(Request<?> request) {
            	Object tag = request.getTag();
                if (!(tag instanceof ReqTag))
                    return false;

                ReqTag second = (ReqTag) tag;
                return reqId == second.getReqId();
            }
        });
    }

    /**
     * @描述 取消和给定的<code>tag</code>相同的处于请求队�?code>ReqeustQueue</code>�?code>Request</code>
     * @方法�?cacelAll
     * @param tag
     * @返回类型 void
     * @创建�?WL
     * @创建时间 2015�?�?0日下�?:59:39
     * @修改�?WL
     * @修改时间 2015�?�?0日下�?:59:39
     * @修改备注 �?
     * @since �?
     * @throws �?
     */
    public void cacelAll(final String requestGroupId) {
    	if(TextUtils.isEmpty(requestGroupId))
    		return;
    		
        mQueue.cancelAll(new RequestFilter() {

            @Override
            public boolean apply(Request<?> request) {
                Object tag = request.getTag();
                
                if(tag == null)
                	return false;
                
                if (!(tag instanceof ReqTag))
                    return false;

                ReqTag second = (ReqTag) tag;
                return requestGroupId.equals(second.getTag());
            }
        });
    }
    
    public void changedAddress(DeliveryAddr addr) {
        if (addr == null) {
//            reqGps();
            return;
        }
        this.mAddr = addr;
        this.isHaveAddr = true;
    }

    private boolean isHaveAddr = false;

    public synchronized DeliveryAddr getDeliveryAddr() {
        // 没有收货地址时，默认为杭州市西湖区
    	if(mAddr!=null)
    		return mAddr;
    	mAddr = new DeliveryAddr();
    	mAddr.setProvince("广东省");
    	mAddr.setCity("广州市");
    	mAddr.setArea("天河区");
    	mAddr.setAddrDetail("");
    	return null;
    }

    public boolean isHasAddr() {
        return isHaveAddr;
    }
    
}
