/**
 * 项目名称:MamaHao
 * 文件名称:MamaHaoError.java
 * 包名称:cn.atmobi.mamhao.network.entity
 * 日期:2015年7月14日下午1:36:17
 * Copyright (c) 2015, 杭州尽在网络技术有限公司 All Rights Reserved.
 *
 */
package cn.softbank.purchase.network.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @描述:妈妈好App对应服务器返回的标准的错误信息Json对象
 * @Copyright Copyright (c) 2015
 * @Company 杭州尽在网络技术有限公司.
 * @author WL
 * @date 2015年7月14日下午1:36:17
 * @version 1.0
 */
public class MamaHaoServerError {
    @SerializedName(value = "errorCode")
    public String errorCode;
    @SerializedName(value = "errmsg")
    public String msg;
}
