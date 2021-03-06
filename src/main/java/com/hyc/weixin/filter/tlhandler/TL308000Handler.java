package com.hyc.weixin.filter.tlhandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hyc.weixin.Article;
import com.hyc.weixin.InputMessage;
import com.hyc.weixin.MsgType;
import com.hyc.weixin.OutputMessage;
import com.hyc.weixin.filter.WXHandler;

/**
 * 菜谱、视频、小说 回复处理器
 * @author yanglang
 *
 */
public class TL308000Handler implements WXHandler{

	@Override
	public OutputMessage handler(InputMessage inputMsg, String sendStr, String returnStr) {
		try {
			JSONObject tulingResult = new JSONObject(returnStr);
			String code = tulingResult.getString("code");
			if("308000".equals(code)){
				OutputMessage outputMsg = new OutputMessage();
		        outputMsg.setFromUserName(inputMsg.getToUserName());
		        outputMsg.setToUserName(inputMsg.getFromUserName());  
		        outputMsg.setCreateTime(Calendar.getInstance().getTimeInMillis() / 1000);  
				dealWith308000(returnStr,outputMsg);
				return outputMsg;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private void dealWith308000(String returnStr, OutputMessage outputMsg) throws JSONException {
        outputMsg.setMsgType(MsgType.News.getName());
    	JSONObject tulingResult = new JSONObject(returnStr);
    	String content = tulingResult.optString("text");
    	outputMsg.setContent(content);
    	JSONArray list = tulingResult.optJSONArray("list");
        List<Article> arts = new ArrayList<Article>();
    	for(int i = 0,len = list.length()>5?5:list.length();i<len;i++){
    		Article art1 = new Article(list.getJSONObject(i).optString("name")+"\n"+list.getJSONObject(i).optString("info"),list.getJSONObject(i).optString("name"),
    				list.getJSONObject(i).optString("icon"),list.getJSONObject(i).optString("detailurl"));
    		arts.add(art1);
    	}
    	outputMsg.setArticles(arts);
        outputMsg.setArticleCount(arts.size());
	}
}
