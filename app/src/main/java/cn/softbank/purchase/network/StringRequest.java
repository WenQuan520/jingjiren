package cn.softbank.purchase.network;

import android.content.Context;

import cn.softbank.purchase.network.entity.DefaultMamahaoServerError;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.Constant;

import com.android.volley.Response.Listener;

/**
 * @author GXH
 * @version 1.0
 */
public class StringRequest<V extends MamaHaoServerError> extends AbstractRequest<String>
        implements Listener<String> {
    private Class<V> mErrorEntity;

    @SuppressWarnings("unchecked")
    public StringRequest(Context context, int method, String url, ApiCallBack apiCallBack) {
        super(context, method, Constant.URL_BASE + url, apiCallBack);
        mListener = this;
        this.mErrorEntity = (Class<V>) DefaultMamahaoServerError.class;
    }

    @SuppressWarnings("unchecked")
    public StringRequest(Context context, int method, String server, String url,
            ApiCallBack apiCallBack) {
        super(context, Method.POST, server + url, apiCallBack);
        mListener = this;
        this.mErrorEntity = (Class<V>) DefaultMamahaoServerError.class;
    }

    @Override
    public void onResponse(String response) {
        synchronized (mApiCallBack) {
        	mApiCallBack.onApiOnSuccess(getTag(),response);
        }


    }

    public StringRequest(Context context, String url, ApiCallBack apiCallBack) {
        this(context, Method.POST, url, apiCallBack);
    }

    public Class<V> getErrorEntity() {
        return mErrorEntity;
    }

    public void setErrorEntity(Class<V> errorEntity) {
        this.mErrorEntity = errorEntity;
    }

}
