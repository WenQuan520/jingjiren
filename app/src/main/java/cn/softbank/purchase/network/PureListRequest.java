package cn.softbank.purchase.network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.util.Log;
import cn.softbank.purchase.network.entity.DefaultMamahaoServerError;
import cn.softbank.purchase.network.entity.MamaHaoServerError;
import cn.softbank.purchase.utils.Constant;
import cn.yicheng.jingjiren.BuildConfig;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
/** 
 * @描述:
 * @author GXH
 * @version 1.0
 */
public class PureListRequest<T, V extends MamaHaoServerError> extends
		AbstractRequest<String> implements Listener<String> {
	private Class<T> clazz;
	private Class<V> mErrorEntity;

	@SuppressWarnings("unchecked")
	public PureListRequest(Context context, int method, String url,
			ApiCallBack apiCallBack,
			Class<T> clazz) {
		super(context, method, Constant.URL_BASE + url, apiCallBack);
		this.clazz = clazz;
		mListener = this;
		this.mErrorEntity = (Class<V>) DefaultMamahaoServerError.class;
	}

	@SuppressWarnings("unchecked")
	public PureListRequest(Context context, int method, String server,
			String url,
			ApiCallBack apiCallBack,
			Class<T> clazz) {
		super(context, Method.POST, server + url, apiCallBack);
		this.clazz = clazz;
		mListener = this;
		this.mErrorEntity = (Class<V>) DefaultMamahaoServerError.class;
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
                		JsonArray jsonArray = jsonElement.getAsJsonObject().get("info").getAsJsonArray();
                		List<T> objList = new ArrayList<T>();
    					Gson gson = new GsonBuilder().setDateFormat(
    							"yyyy-MM-dd HH:mm:ss").create();
    					for (Iterator<JsonElement> jsonIterator = jsonArray
    							.iterator(); jsonIterator.hasNext();) {
    						JsonElement element3 = jsonIterator.next();
    						T resultT = gson.fromJson(element3, clazz);
    						objList.add(resultT);
    					}
    					gson = null;
    					mApiCallBack.onApiOnSuccess(getTag(), objList);
                	}
                } else {
                    mApiCallBack.onApiFailure(getTag(), null, MamaHaoError.ParseError);
                }
                
                if (BuildConfig.DEBUG) {
                    Log.i("Http Response-->",
                            new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement));
                }
				
			} catch (JsonSyntaxException e) {
				mApiCallBack.onApiFailure(getTag(), null,
						MamaHaoError.ParseError);
			} catch (JsonIOException e) {
				mApiCallBack.onApiFailure(getTag(), null,
						MamaHaoError.ParseError);
			} catch (JsonParseException e) {
				mApiCallBack.onApiFailure(getTag(), null,
						MamaHaoError.ParseError);
			} catch (RuntimeException e) {
				mApiCallBack.onApiFailure(getTag(), null,
						MamaHaoError.NoConnectionError);
			}
		}
	}

	public PureListRequest(Context context, String url,
			ApiCallBack apiCallBack, Class<T> clazz) {
		this(context, Method.POST, url, apiCallBack, clazz);
	}

	public Class<V> getErrorEntity() {
		return mErrorEntity;
	}

	public void setErrorEntity(Class<V> errorEntity) {
		this.mErrorEntity = errorEntity;
	}
}
