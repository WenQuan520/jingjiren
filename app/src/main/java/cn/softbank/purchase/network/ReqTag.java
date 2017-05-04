/**
 * 项目名称:MamaHao
 * 文件名称:ReqTag.java
 * 包名称:cn.atmobi.mamhao.network
 * 日期:2015年7月14日下午12:39:05
 * Copyright (c) 2015, 杭州尽在网络技术有限公司 All Rights Reserved.
 *
 */
package cn.softbank.purchase.network;

/** 
 * @描述:
 * @Copyright Copyright (c) 2016
 * @author GXH
 * @version 1.0
 */
public class ReqTag {
	/**
	 * 请求表识
	 */
	private int reqId;
    private String tag;
    /**
     * 应用数据
     */
    private Object identify;
    /**
     * 组Id
     */
    private String reqGroupId;
    /**
     * 是否由baseActivity处理无网络空白页
     */
    private boolean handleNetworkError;
    /**
     * 是否由baseActivity处理简单数据错误提示 默认为true
     */
    private boolean handleSimpleRes;
    /**
     * 禁止自动取消缓冲框
     */
    private boolean disAbleHideProgress;

    private ReqTag(Builder builder){
    	this.reqId = builder.reqId;	
    	this.reqGroupId = builder.reqGroupId;
		this.tag = builder.tag;
		this.identify = builder.identify;
		this.handleNetworkError = builder.handleNetworkError;
		this.handleSimpleRes = builder.handleSimpleRes;
		this.disAbleHideProgress = builder.disAbleHideProgress;
	}
	
	public static class Builder{
		private int reqId;
		private String reqGroupId;
		private String tag;
	    private Object identify;
	    private boolean handleNetworkError;
	    private boolean handleSimpleRes;
	    private boolean disAbleHideProgress;
	    
	    public ReqTag build(int reqId){
	    	this.reqId = reqId;
	    	return new ReqTag(this);
	    }
		
		public Builder reqGroupId(String value){
			reqGroupId = value;
			return this;
		}
		public Builder identify(Object value){
			identify = value;
			return this;
		}
		public Builder handleNetworkError(boolean value){
			handleNetworkError = value;
			return this;
		}
		public Builder handleSimpleRes(boolean value){
			handleSimpleRes = value;
			return this;
		}
		public Builder disAbleHideProgress(){
			disAbleHideProgress = true;
			return this;
		}
		public Builder tag(String value){
			tag = value;
			return this;
		}
	}
	
    public int getReqId() {
		return reqId;
	}

	public String getReqGroupId() {
		return reqGroupId;
	}

	public String getTag() {
        return tag;
    }

    public Object getIdentify() {
        return identify;
    }

	public boolean isHandleNetworkError() {
		return handleNetworkError;
	}

	public boolean isHandleSimpleRes() {
		return handleSimpleRes;
	}

	public boolean isDisAbleHideProgress() {
		return disAbleHideProgress;
	}

	public void setReqId(int reqId) {
		this.reqId = reqId;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setIdentify(Object identify) {
		this.identify = identify;
	}

	public void setReqGroupId(String reqGroupId) {
		this.reqGroupId = reqGroupId;
	}

	public void setHandleNetworkError(boolean handleNetworkError) {
		this.handleNetworkError = handleNetworkError;
	}

	public void setHandleSimpleRes(boolean handleSimpleRes) {
		this.handleSimpleRes = handleSimpleRes;
	}

	@Override
	public String toString() {
		return "ReqTag [reqId=" + reqId + ", tag=" + tag + ", identify="
				+ identify + ", reqGroupId=" + reqGroupId
				+ ", handleNetworkError=" + handleNetworkError
				+ ", handleSimpleRes=" + handleSimpleRes
				+ ", disAbleHideProgress=" + disAbleHideProgress + "]";
	}
    
}
