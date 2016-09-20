package com.hyc.duiba.tools;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.hyc.duiba.beans.CreditConsumeVirtualParams;

import cn.com.duiba.credits.sdk.CreditTool;
import cn.com.duiba.credits.sdk.SignTool;

public class CreditHycTool extends CreditTool {

	private String appKey;

	private String appSecret;

	public CreditHycTool(String appKey, String appSecret) {
		super(appKey, appSecret);
		this.appKey = appKey;
		this.appSecret = appSecret;
	}

	public CreditConsumeVirtualParams parseCreditVirtualConsume(HttpServletRequest request)
			throws Exception {
		if (!this.appKey.equals(request.getParameter("appKey"))) {
			throw new Exception("appKey不匹配");
		}
		if (request.getParameter("timestamp") == null) {
			throw new Exception("请求中没有带时间戳");
		}
		boolean verify = SignTool.signVerify(this.appSecret, request);
		if (!verify) {
			throw new Exception("签名验证失败");
		}
		CreditConsumeVirtualParams params = new CreditConsumeVirtualParams();
		params.setAppKey(this.appKey);
		params.setUid(request.getParameter("uid"));
		params.setDevelopBizId(request.getParameter("developBizId"));
		params.setTimestamp(new Date(Long.valueOf(
				request.getParameter("timestamp")).longValue()));
		params.setDescription(request.getParameter("description"));
		params.setOrderNum(request.getParameter("orderNum"));
		params.setParams(request.getParameter("params"));
		return params;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

}
