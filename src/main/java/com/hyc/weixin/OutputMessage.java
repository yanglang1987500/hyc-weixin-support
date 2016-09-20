package com.hyc.weixin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")  
public class OutputMessage implements Serializable{  
  
	private static final long serialVersionUID = 343847737150050679L;

	@XStreamAlias("ToUserName")  
    @XStreamCDATA  
    private String ToUserName;  
  
    @XStreamAlias("FromUserName")  
    @XStreamCDATA  
    private String FromUserName;  
  
    @XStreamAlias("CreateTime")  
    private Long CreateTime;  
  
    @XStreamAlias("MsgType")  
    @XStreamCDATA  
    private String MsgType = "text";  
  
    private ImageMessage Image;  
    
    @XStreamAlias("Content")  
    @XStreamCDATA  
    private String Content;
    

    @XStreamAlias("ArticleCount")  
    private int ArticleCount;
    
    @XStreamAlias("Articles")  
    private List<Article> Articles;
  
    public String getToUserName() {  
        return ToUserName;  
    }  
  
    public void setToUserName(String toUserName) {  
        ToUserName = toUserName;  
    }  
  
    public String getFromUserName() {  
        return FromUserName;  
    }  
  
    public void setFromUserName(String fromUserName) {  
        FromUserName = fromUserName;  
    }  
  
    public Long getCreateTime() {  
        return CreateTime;  
    }  
  
    public void setCreateTime(Long createTime) {  
        CreateTime = createTime;  
    }  
  
    public String getMsgType() {  
        return MsgType;  
    }  
  
    public void setMsgType(String msgType) {  
        MsgType = msgType;  
    }  
  
    public ImageMessage getImage() {  
        return Image;  
    }  
  
    public void setImage(ImageMessage image) {  
        Image = image;  
    }

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}


	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<Article> getArticles() {
		return Articles;
	}

	public void setArticles(List<Article> articles) {
		Articles = articles;
	}  
    
    public static OutputMessage transfromMap(Map<String,Object> obj){
    	OutputMessage result = new OutputMessage();
    	result.setContent((String)obj.get("content"));
		result.setCreateTime(Long.parseLong(String.valueOf(obj.get("createTime"))));
		result.setToUserName((String)obj.get("toUserName"));
		result.setFromUserName((String)obj.get("fromUserName"));
		result.setMsgType((String)obj.get("msgType"));
		result.setArticleCount((Integer)obj.get("articleCount"));
		List<Article> arts = new ArrayList<Article>();
		List<Map<String,Object>> articles = (List<Map<String,Object>>)obj.get("articles");
		if(articles!=null)
			for(Map<String,Object> article:articles){
				arts.add(new Article((String)article.get("title"),(String)article.get("description"),(String)article.get("picUrl"),(String)article.get("url")));
			}
		result.setArticles(arts);
    	return result;
    }
  
}  
