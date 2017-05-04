package cn.softbank.purchase.network;

import android.content.Context;
import android.util.Log;
import cn.softbank.purchase.network.entity.DefaultMamahaoServerError;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.Constant;
import cn.yicheng.jingjiren.BuildConfig;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * @描述 返回JavaBean对象格式的Reqeust
 * @Copyright Copyright (c) 2016
 * @Company 广州软银信息科技有限科技.
 * @author GXH
 * @version 1.0
 */
public class BeanRequest<T, V extends MamaHaoServerError> extends AbstractRequest<String>
        implements Listener<String> {

    private Class<T> clazz;

    private Class<V> mErrorEntity;

    @SuppressWarnings("unchecked")
    public BeanRequest(Context context, int method, String url, ApiCallBack apiCallBack,
            Class<T> clazz) {
        super(context, method, Constant.URL_BASE + url, apiCallBack);
        mListener = this;
        this.clazz = clazz;
        this.mErrorEntity = (Class<V>) DefaultMamahaoServerError.class;
    }
    
    @SuppressWarnings("unchecked")
    public BeanRequest(Context context, int method, String server, String url,
            ApiCallBack apiCallBack,
            Class<T> clazz) {
        super(context, Method.POST, server + url, apiCallBack);
        mListener = this;
        this.clazz = clazz;
        this.mErrorEntity = (Class<V>) DefaultMamahaoServerError.class;
    }
    

    public BeanRequest(Context context, String url, ApiCallBack apiCallBack, Class<T> clazz) {
        this(context, Method.POST, url, apiCallBack, clazz);
    }
    
    public BeanRequest(Context context, String url, ApiCallBack apiCallBack, Class<T> clazz, boolean isParseText) {
    	this(context, Method.POST, url, apiCallBack, clazz);
    }
    
    public BeanRequest(Context context, String server,String url, ApiCallBack apiCallBack, Class<T> clazz) {
        this(context, Method.POST, server,url, apiCallBack, clazz);
    }

    @Override
    public void onResponse(String response) {
        synchronized (mApiCallBack) {
            try {
                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse(response);
                if (jsonElement.isJsonObject()){
                	
                	if(jsonElement.getAsJsonObject().get("error").getAsString().equals("1")){
                		MamaHaoServerError error = new MamaHaoServerError();
                		error.msg = jsonElement.getAsJsonObject().get("errorMsg").getAsString();
                		mApiCallBack.onApiFailure(getTag(),error, null);
                	}else{
                		mApiCallBack.onApiOnSuccess(getTag(),
                				new Gson()
                		.fromJson(jsonElement.getAsJsonObject().get("info"), clazz));
                	}
                } else {
                    mApiCallBack.onApiFailure(getTag(), null, MamaHaoError.ParseError);
                }

                if (BuildConfig.DEBUG) {
                    Log.i("Http Response-->",
                            new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                                    .setPrettyPrinting().create().toJson(jsonElement));
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                mApiCallBack.onApiFailure(getTag(), null, MamaHaoError.ParseError);
            } catch (JsonIOException e) {
                e.printStackTrace();
                mApiCallBack.onApiFailure(getTag(), null, MamaHaoError.ParseError);
            } catch (JsonParseException e) {
                e.printStackTrace();
                mApiCallBack.onApiFailure(getTag(), null, MamaHaoError.ParseError);
            } catch (RuntimeException e) {
                e.printStackTrace();
                mApiCallBack.onApiFailure(getTag(), null, MamaHaoError.UnKnowError);
            }
        }


    }

    public Class<V> getErrorEntity() {
        return mErrorEntity;
    }

    public void setErrorEntity(Class<V> errorEntity) {
        this.mErrorEntity = errorEntity;
    }

}
