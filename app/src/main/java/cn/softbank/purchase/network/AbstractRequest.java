
package cn.softbank.purchase.network;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.text.TextUtils;

import cn.softbank.purchase.network.NetworkManager.NetworkState;
import cn.softbank.purchase.network.entity.MamaHaoServerError;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

/**
 * @描述:
 * @version 1.0
 */
public abstract class AbstractRequest<T> extends Request<String> {
    protected ConcurrentHashMap<String, String> params = new ConcurrentHashMap<String, String>();
    protected Context mContext;
    protected Listener<String> mListener;
    private Priority mLevel = Priority.NORMAL;

    private int DEFAULT_TIMEOUT = 12000;

    // 服务器返回错误信息是，返回的错误编码Key
    protected final String ERROR_CODE = "errcode";

    // 服务器返回列表信息是对应的可能Key
    protected final String RESULT_LIST_ROWS = "rows";
    protected final String RESULT_LIST_DATAS = "datas";
    protected final String RESULT_LIST_DATA = "data";
    
    // Volley错误监听回调类
    private final Response.ErrorListener mErrorListener;

    protected ApiCallBack mApiCallBack;

    public AbstractRequest(Context context, int method, final String url, final ApiCallBack apiCallBack) {
        super(method, url, null);
        mErrorListener = new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//            	System.out.println("error"+error.toString()+url);
                synchronized (apiCallBack) {
                    if (error instanceof AuthFailureError) {
                        apiCallBack.onApiFailure(getTag(), null, MamaHaoError.AuthFailureError);
                    } else if (error instanceof NetworkError) {
                        apiCallBack.onApiFailure(getTag(), null, MamaHaoError.NetworkError);
                    } else if (error instanceof NoConnectionError) {
                        apiCallBack.onApiFailure(getTag(), null, MamaHaoError.NoConnectionError);
                    } else if (error instanceof ParseError) {
                        apiCallBack.onApiFailure(getTag(), null, MamaHaoError.ParseError);
                    } else if (error instanceof ServerError) {
                        apiCallBack.onApiFailure(getTag(), null, MamaHaoError.ServerError);
                    } else if (error instanceof TimeoutError) {
                        apiCallBack.onApiFailure(getTag(), null, MamaHaoError.TimeoutError);
                    } else {
                        apiCallBack.onApiFailure(getTag(), null, MamaHaoError.UnKnowError);
                    }
                }
            }
        };

        NetworkState networkState = NetworkManager.getInstance().getNetworkState();

        /*
         * 根据网络状态，设置请求的超时时间： 1、 网络状态差的情况下如WAP、2G网络等情况下设置网络超时时间为7.5s，重连次数为3，重连超时系数是1；
         * 2、在网络情况为3G、WIFI等情况下设置网络超时时间为2.5s,重连次数为3，重连超时系数是1；
         */

        if (networkState != null) {
            switch (networkState.getNetWorkType()) {
                case NetworkManager.NETWORKTYPE_INVALID:
                    break;
                case NetworkManager.NETWORKTYPE_WAP:
                    setRetryPolicy(
                            new DefaultRetryPolicy(DEFAULT_TIMEOUT * 3, 1,
                                    1f));
                    break;
                case NetworkManager.NETWORKTYPE_2G:
                    setRetryPolicy(
                            new DefaultRetryPolicy(DEFAULT_TIMEOUT * 3, 1,
                                    1f));
                    break;
                case NetworkManager.NETWORKTYPE_WIFI:
                case NetworkManager.NETWORKTYPE_3G:
                default:
                    setRetryPolicy(
                            new DefaultRetryPolicy(DEFAULT_TIMEOUT, 1, 1f));
                    break;
            }
        } else {
            setRetryPolicy(
                    new DefaultRetryPolicy(DEFAULT_TIMEOUT * 3, 1,
                            1f));
        }

        this.mContext = context;
        this.mApiCallBack = apiCallBack;
    }

    public void deliverError(VolleyError error) {
        if (mErrorListener != null) {
            mErrorListener.onErrorResponse(error);
        }
    }

    @Override
    public Priority getPriority() {
        return mLevel;
    }

    /**
     * @描述 设置请求的优先级
     * @方法名 setPriority
     * @param priority
     * @返回类型 void
     * @创建人 WL
     * @创建时间 2015年8月18日下午9:15:22
     * @修改人 WL
     * @修改时间 2015年8月18日下午9:15:22
     * @修改备注 无
     * @since 无
     * @throws 无
     */
    public void setPriority(Priority priority) {
        this.mLevel = priority;
    }

    @Override
    protected void deliverResponse(String response) {
//    	System.out.println("response"+response);
        mListener.onResponse(response);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed = null;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    public void setTag(Object tag) {
        if (!(tag instanceof ReqTag))
            throw new IllegalArgumentException("tag type is error!");
        super.setTag(tag);
    }

    @Override
    public ReqTag getTag() {
        return (ReqTag) super.getTag();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
        map.put("json", new Gson().toJson(params));
//        System.out.println(new Gson().toJson(params).toString());
        return map;
    }
    
    public Map<String, String> getParam(){
    	return params;
    }

//    @Override
//	public String getCacheKey() {
//    	StringBuilder stringBuilder = new StringBuilder();
//    	int i = 0;
//    	for(String key:params.keySet()){
//    		if(i != params.keySet().size()-1){
//    			stringBuilder.append(key+params.get(key));
//    		}
//    		i++;
//    	}
//    	return super.getUrl()+stringBuilder.toString();
//	}

	public AbstractRequest<T> setParam(String key, String value) {
    	if(!TextUtils.isEmpty(value))
    		params.put(key, value);
        return this;
    }
	
    public enum MamaHaoError {
        AuthFailureError, NetworkError, NoConnectionError, ParseError, ServerError, TimeoutError, UnKnowError;
    }

    /**
     * @描述 网络请求回调接口
     * @Copyright Copyright (c) 2015
     * @version 1.0
     */
    public interface ApiCallBack {
        /**
         * @描述 网络请求成功回调
         * @方法名 onApiOnSuccess
         * @param tag
         * @param data
         * @返回类型 void
         * @创建人 WL
         * @创建时间 2015年8月18日下午8:43:05
         * @修改人 WL
         * @修改时间 2015年8月18日下午8:43:05
         * @修改备注 无
         * @since 无
         * @throws 无
         */
        void onApiOnSuccess(ReqTag tag, Object data);

        /**
         * @描述 网络请求失败回调
         * @方法名 onApiFailure
         * @param tag
         * @param error
         * @param clientError
         * @返回类型 void
         * @创建人 WL
         * @创建时间 2015年8月18日下午8:43:09
         * @修改人 WL
         * @修改时间 2015年8月18日下午8:43:09
         * @修改备注 无
         * @since 无
         * @throws 无
         */
        void onApiFailure(ReqTag tag, MamaHaoServerError error, MamaHaoError clientError);
    }

}
