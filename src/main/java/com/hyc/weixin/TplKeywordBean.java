package com.hyc.weixin;


/**
 * 微信模板消息关键字bean
 * @author IBM
 *
 */
public class TplKeywordBean {
	//key名称
	private String name;
	
	//值
	private String value;
	
	//颜色
	private String color = "#173177";
	
	public TplKeywordBean(){}
	
	public TplKeywordBean(String name,String value,String color){
		this.name = name;
		this.value = value;
		this.color = color;
	}
	
	public TplKeywordBean(String name,String value){
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
