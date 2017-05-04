/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.softbank.purchase.base;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.text.TextUtils;
import cn.softbank.purchase.client.PurchaseApi;
import cn.softbank.purchase.domain.HomeTypeData;
import cn.softbank.purchase.domain.SortItem;
import cn.softbank.purchase.utils.Constant;
import cn.softbank.purchase.utils.ForceCloseHandler;
import cn.softbank.purchase.utils.SharedPreference;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
/**
 * 
 * @author Administrator GXH
 *
 */
public class MyApplication extends Application {

    public static Context applicationContext;
    private static MyApplication instance;
    // login user name
    public final String PREF_USERNAME = "username";
    public String memberName = "";
    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";

    public String memberId;
    private String userToken;
    private String city;
    private double lon;
    private double lat;
    private List<List<SortItem>> sortTotalDatas = new ArrayList<>();
	
    
    @SuppressWarnings("unused")
    @Override
    public void onCreate() {
        
        if (Constant.Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
        }
        
        super.onCreate();
        
        applicationContext = this;
      ForceCloseHandler.getInstance().init(applicationContext);
        instance = this;
        
        //imageLoader
        initImageLoader(getApplicationContext());
        
//        ShareSDK.initSDK(this);

        PurchaseApi.init(applicationContext);
        
        memberId = SharedPreference.getString(applicationContext, "userId");
        
//        memberId = "118";
        
        memberName = SharedPreference.getString(applicationContext, "nickname");
        userToken = SharedPreference.getString(applicationContext, "userToken");
//        if(TextUtils.isEmpty(memberName))
//        	memberName = SharedPreference.getString(applicationContext, "username");
        	
        
//        if(memberId == null)
//        	memberId = "";

    }

    public static MyApplication getInstance() {
        return instance;
    }
    
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2)
        .denyCacheImageMultipleSizesInMemory()
        .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(1024 * 1024 * 1024)
        .memoryCacheExtraOptions(480, 800)
                .threadPoolSize(3)
        .denyCacheImageMultipleSizesInMemory()
//                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCache(new WeakMemoryCache())
        .tasksProcessingOrder(QueueProcessingType.LIFO);
//      config.discCache(new UnlimitedDiskCache(cacheDir));//自定义缓存路�?
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
    
    // 获取当前登陆用户名称
    public String getMemberName() {
        return memberName;
    }

    // 设置当前登陆用户名称
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
    
    public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	
	public String getCity() {
		if(TextUtils.isEmpty(city))
			return "杭州市";
		return city;
//		return "广州";
	}

	public void setCity(String city) {
		this.city = city;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		SharedPreference.saveToSP(applicationContext, "memberId", memberId);
		this.memberId = memberId;
	}
	
	public boolean isUserLogin(){
		return !TextUtils.isEmpty(memberId);
	}

	public List<List<SortItem>> getSortTotalDatas() {
		if(sortTotalDatas.size()==0)
			initSortDatas();
		return sortTotalDatas;
	}

	public void setSortTotalDatas(HomeTypeData homeTypeData) {
		//区域
		List<SortItem> sorts1 = new ArrayList<>();
		sorts1.add(new SortItem("不限", true));
		for(String region:homeTypeData.getRegion())
			sorts1.add(new SortItem(region, false));
		
		List<SortItem> sorts2 = new ArrayList<>();
		sorts2.add(new SortItem("不限", true));
		for(String type:homeTypeData.getBuildingType())
			sorts2.add(new SortItem(type, false));
		
		List<SortItem> sorts3 = new ArrayList<>();
		sorts3.add(new SortItem("不限", true));
		for(String area:homeTypeData.getArea())
			sorts3.add(new SortItem(area, false));
		
		List<SortItem> sorts4 = new ArrayList<>();
		sorts4.add(new SortItem("不限", true));
		for(String price:homeTypeData.getPrice())
			sorts4.add(new SortItem(price, false));
		
		sortTotalDatas.add(sorts1);
		sortTotalDatas.add(sorts2);
		sortTotalDatas.add(sorts3);
		sortTotalDatas.add(sorts4);
		
	}

	private void initSortDatas(){
		List<SortItem> sorts1 = new ArrayList<>();
		sorts1.add(new SortItem("不限", true));
		
		List<SortItem> sorts2 = new ArrayList<>();
		sorts2.add(new SortItem("不限", true));
		sorts2.add(new SortItem("住宅", false));
		sorts2.add(new SortItem("写字楼", false));
		sorts2.add(new SortItem("别墅", false));
		sorts2.add(new SortItem("酒店公寓", false));
		sorts2.add(new SortItem("商住公寓", false));
		sorts2.add(new SortItem("统建楼", false));
		sorts2.add(new SortItem("军房产", false));
		sorts2.add(new SortItem("沿街商铺", false));
		sorts2.add(new SortItem("综合体商铺", false));
		
		List<SortItem> sorts3 = new ArrayList<>();
		sorts3.add(new SortItem("不限", true));
		sorts3.add(new SortItem("50平米以下", false));
		sorts3.add(new SortItem("50-70平米", false));
		sorts3.add(new SortItem("70-90平米", false));
		sorts3.add(new SortItem("90-110平米", false));
		sorts3.add(new SortItem("110-130平米", false));
		sorts3.add(new SortItem("130-150平米", false));
		sorts3.add(new SortItem("150-200平米", false));
		sorts3.add(new SortItem("200-300平米", false));
		sorts3.add(new SortItem("300-500平米", false));
		sorts3.add(new SortItem("500平米以上", false));
		
		List<SortItem> sorts4 = new ArrayList<>();
		sorts4.add(new SortItem("不限", true));
		sorts4.add(new SortItem("50万以下", false));
		sorts4.add(new SortItem("50-80万", false));
		sorts4.add(new SortItem("80-100万", false));
		sorts4.add(new SortItem("100-120万", false));
		sorts4.add(new SortItem("120-150万", false));
		sorts4.add(new SortItem("150-200万", false));
		sorts4.add(new SortItem("200-250万", false));
		sorts4.add(new SortItem("250-300万", false));
		sorts4.add(new SortItem("300-500万", false));
		sorts4.add(new SortItem("500万以上", false));
		
		sortTotalDatas.add(sorts1);
		sortTotalDatas.add(sorts2);
		sortTotalDatas.add(sorts3);
		sortTotalDatas.add(sorts4);
	}
	
}
