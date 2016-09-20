package com.hyc.weixin.filter;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息响应类
 * @author yanglang
 *
 */
public class MessageResponse {
	private Map<String,Object> attribute;
	
	public MessageResponse(){
		this.attribute = new HashMap<String, Object>();
	}

	public Object getAttribute(String key) {
		return attribute.get(key);
	}

	public void setAttribute(String key,Object value) {
		this.attribute.put(key, value);
	}
}
