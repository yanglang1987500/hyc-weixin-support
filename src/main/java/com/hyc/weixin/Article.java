package com.hyc.weixin;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 图文消息中Article类的定义
 * @author Javen
 * @Email zyw205@gmial.com
 *
 */
@XStreamAlias("item")  
public class Article implements Serializable{

	private static final long serialVersionUID = 3677050165463398338L;

	// 图文消息名称
	@XStreamAlias("Title")  
    @XStreamCDATA  
    private String Title;
	
    // 图文消息描述
	@XStreamAlias("Description")  
    @XStreamCDATA  
    private String Description;
	
    // 图片链接，支持JPG、PNG格式，较好的效果为大图640*320，小图80*80，限制图片链接的域名需要与开发者填写的基本资料中的Url一致
	@XStreamAlias("PicUrl")  
    @XStreamCDATA 
    private String PicUrl;
	
    // 点击图文消息跳转链接
	@XStreamAlias("Url")  
    @XStreamCDATA 
    private String Url;
	
	public Article(){}
	
	public Article(String title,String description,String picUrl,String url){
		this.Title = title;
		this.Description = description;
		this.PicUrl = picUrl;
		this.Url = url;
	}

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return null == Description ? "" : Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPicUrl() {
        return null == PicUrl ? "" : PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getUrl() {
        return null == Url ? "" : Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

}