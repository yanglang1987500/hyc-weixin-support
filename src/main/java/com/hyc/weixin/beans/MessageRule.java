package com.hyc.weixin.beans;

import java.util.ArrayList;
import java.util.List;

public class MessageRule {
	private String id;
	private String title;
	private String replay;
	private String order;
	private String isRandom;
	private List<MessageRuleKey> keys;
	public MessageRule(){
		this.keys = new ArrayList<MessageRuleKey>();
	};
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getReplay() {
		return replay;
	}
	public void setReplay(String replay) {
		this.replay = replay;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getIsRandom() {
		return isRandom;
	}
	public void setIsRandom(String isRandom) {
		this.isRandom = isRandom;
	}
	public List<MessageRuleKey> getKeys() {
		return keys;
	}
	public void setKeys(List<MessageRuleKey> keys) {
		this.keys = keys;
	}
	public void addKey(MessageRuleKey key){
		this.keys.add(key);
	}
}
