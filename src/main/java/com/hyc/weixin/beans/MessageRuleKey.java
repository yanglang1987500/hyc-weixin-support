package com.hyc.weixin.beans;

public class MessageRuleKey {
	private String id;
	private String key;
	private String matchType;
	private MessageRule parent;
	public MessageRuleKey(){}
	public MessageRuleKey(String id,String key,String matchType,MessageRule parent){
		this.id = id;
		this.key = key;
		this.matchType = matchType;
		this.parent = parent;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getMatchType() {
		return matchType;
	}
	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}
	public MessageRule getParent() {
		return parent;
	}
	public void setParent(MessageRule parent) {
		this.parent = parent;
	}
	
}
