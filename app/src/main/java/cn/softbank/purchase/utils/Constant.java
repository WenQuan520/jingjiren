/*
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
package cn.softbank.purchase.utils;

/**
 * 测试打包须知:
 * 1.修改PARTNER,SELLER,RSA_PRIVATE,URL_PAY_BACK四项
 * 2.修改DEBUGMODE
 * 3.修改URL_BASE(请求根目录)
 * 4.注意版本号的修改
 * @author GXH
 *
 */
public class Constant {
    public static final boolean DEBUGMODE = true;// 项目发布时，改为false！
    public static final int DBVERSION = 3;//DB版本
    
    public static final String ACCESS_KEY = "LDDcbXmfaXtWx550";
    
    /** 支付宝环境->START,上面一行为正式环境,下面一行为测试环境*/
    
    // 微信支付
//    public static String URL_WEIXIN_PAY = "/wxpay/build.do?orderNo=";
 // 银联支付
    public static String URL_GET_UNION_TN = "";
    
    public static final int SDK_PAY_FLAG = 1;
    public static final int SDK_CHECK_FLAG = 2;
    //微信APP_ID
    public static final String APP_ID = "wx73ce0c7fa5af1828";
    
    public static final String APP_SECRET = "4387c33d9a29182e713286c8f48fcf89";
    // 位置类型:0开始，1中间，2结束，3开始+结束
    public static final int POS_TYPE_MIDDLE = 0;
    public static final int POS_TYPE_START = 1;
    public static final int POS_TYPE_END = 2;
    public static final int POS_TYPE_START_END = 3;
    
    public static final int DEFAULT_TOP_MARGIN = -1;// 大号
    
    public static final int INLET_DETAIL = 1;// 
    public static final int INLET_CART = 2;// 
    
    public static final float SEND_FEE = 10;
    
    // 环信相关
    public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
    public static final String GROUP_USERNAME = "item_groups";
    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";
    public static final String ACCOUNT_REMOVED = "account_removed";

    public static class Config {
        public static final boolean DEVELOPER_MODE = false;
    }

    public static class Extra {
        public static final String FRAGMENT_INDEX = "com.nostra13.example.universalimageloader.FRAGMENT_INDEX";
        public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";
    }

    public static String URL_BASE = "http://139.224.44.8/index.php";
    public static String URL_WULIU = "http://m.kuaidi100.com/index_all.html";
     public static String URL_ZHUCE = "http://139.224.44.8/index.php?s=help/zhucezhanghao";
     public static String URL_YEWUCAOZUO = "http://139.224.44.8/index.php?s=help/yewucaozuo";
     public static String URL_YEJIGUISHU = "http://139.224.44.8/index.php?s=help/yejiguishu";
     public static String URL_WODEQIANBAO = "http://139.224.44.8/index.php?s=help/wodeqianbao";
     public static String URL_XINGWEIZHUNZE = "http://139.224.44.8/index.php?s=help/xingweizhunze";
     public static String URL_TOUSUJIANYI = "http://139.224.44.8/index.php?s=help/tousujianyi";
     public static String URL_GUANYUWOMEN = "http://139.224.44.8/index.php?s=help/guanyuwomen";
     public static String URL_AGREEMENT = "http://139.224.44.8/index.php?s=help/dengluxieyi";

    // 常用数据
     public static final String NET_NOT_AVAILABLE = "net_not_available";
    public static int DEFAULT_LEFT_BACK = 0;// 标题栏默认返回键
    public static final int DEFAULT_AREAID = 943;// 默认地区ID，杭州上城
    public static final String NET_USER_ERROR_MEG = "userErrorMsg:";
    public static final String NET_SUCCESS_FLAG = "success_code";// "{\"success_code\":";
}
