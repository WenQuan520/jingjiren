package cn.softbank.purchase.db;

import cn.softbank.purchase.utils.SharedPreference;
import android.content.Context;
/**
 * @描述: 小型数据库操作
 * @author Administrator GXH
 *
 */
public class SPDAO {
	
	public static final String SEARCH_HISTORY = "searchHistory";
	public static final String CART_SELS = "cartSels";

	private static SPDAO instance = null;
	
	public static SPDAO getInstance() {
        if (instance == null) 
            instance = new SPDAO();
        return instance;
    }

    private SPDAO() {}

    /**
     * 插入一条数据
     * @param context
     * @param name 键
     * @param value 值
     */
	public void insert(Context context,String name,String value){
		String dataNow = SharedPreference.getString(context, name);
		StringBuilder sbNew = new StringBuilder();
		if(dataNow!=null)
			sbNew.append(dataNow);
		SharedPreference.saveToSP(context, name, sbNew.append(value).append(",").toString());
    }
	
	/**
	 * 插入一条数据至第一条
	 * @param context
	 * @param name 键
	 * @param value 值
	 */
	public void insertIndex(Context context,String name,String value){
		StringBuilder sbNew = new StringBuilder();
		sbNew.append(value).append(",");
		String dataNow = SharedPreference.getString(context, name);
		if(dataNow!=null)
			sbNew.append(dataNow);
		SharedPreference.saveToSP(context, name, sbNew.toString());
	}
    
	/**
     * 删除一条数据
     * @param context
     * @param name 键
     * @param value 值
     */
    public void delete(Context context,String name,String value){
    	StringBuilder values=new StringBuilder(SharedPreference.getString(context, name));
    	int startIndex=values.indexOf(new StringBuilder(value).append(",").toString());
    	values.delete(startIndex, value.length()+startIndex+1);
    	SharedPreference.saveToSP(context, name, values.toString());
    }
    
    /**
     * 删除一条数据
     * @param context
     * @param name 键
     * @param value 值
     */
    public void clear(Context context,String name){
    	SharedPreference.saveToSP(context, name, "");
    }
    
    /**
     * 判断是否存在
     * @param context
     * @param name 键
     * @param value 值
     */
    public boolean judgeExists(Context context,String name,String value){
    	String values = SharedPreference.getString(context, name);
    	return (values.length()>0 && 
    			values.indexOf(new StringBuilder(value).append(",").toString())>=0);
    }
}
