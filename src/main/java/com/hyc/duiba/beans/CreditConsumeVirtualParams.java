package com.hyc.duiba.beans;

import java.util.HashMap;
import java.util.Map;

import cn.com.duiba.credits.sdk.CreditConsumeParams;
import cn.com.duiba.credits.sdk.SignTool;

public class CreditConsumeVirtualParams extends CreditConsumeParams {
	
	private String developBizId;
	
	public Map<String, String> toRequestMap(String appSecret) {
		Map map = new HashMap();
		map.put("credits", this.getCredits());
		map.put("description", this.getDescription());
		map.put("uid", this.getUid());
		map.put("appKey", this.getAppKey());
		map.put("waitAudit", this.isWaitAudit());
		map.put("appSecret", appSecret);
		map.put("timestamp", this.getTimestamp().getTime());
		map.put("orderNum", this.getOrderNum());
		map.put("type", this.getType());
		map.put("facePrice", this.getFacePrice());
		map.put("actualPrice", this.getActualPrice());
		map.put("ip", this.getIp());
		map.put("params", this.getParams());
		map.put("developBizId", this.getDevelopBizId());

		putIfNotEmpty(map, "transfer", this.getTransfer());
		putIfNotEmpty(map, "qq", this.getQq());
		putIfNotEmpty(map, "alipay", this.getAlipay());
		putIfNotEmpty(map, "phone", this.getPhone());

		String sign = SignTool.sign(map);
		map.remove("appSecret");
		map.put("sign", sign);
		return map;
	}

	private void putIfNotEmpty(Map<String, String> map, String key, String value) {
		if ((value == null) || (value.length() == 0)) {
			return;
		}
		map.put(key, value);
	}

	public String getDevelopBizId() {
		return developBizId;
	}

	public void setDevelopBizId(String developBizId) {
		this.developBizId = developBizId;
	}
	
	
}
