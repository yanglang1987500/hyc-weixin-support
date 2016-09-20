package com.hyc.weixin.filter.tlhandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.hyc.weixin.Article;
import com.hyc.weixin.InputMessage;
import com.hyc.weixin.MsgType;
import com.hyc.weixin.OutputMessage;
import com.hyc.weixin.filter.WXHandler;

/**
 * 天气回复处理器
 * @author yanglang
 *
 */
public class TL1000004TianqiHandler implements WXHandler {

	@Override
	public OutputMessage handler(InputMessage inputMsg, String sendStr, String returnStr) {
		try {
			JSONObject tulingResult = new JSONObject(returnStr);
			String code = tulingResult.getString("code");
			if("100000".equals(code)){
				String text = tulingResult.optString("text");
				text = text.replaceAll("<br>", "\n");
				if(sendStr.indexOf("天气")!=-1){
					OutputMessage outputMsg = new OutputMessage();
			        outputMsg.setFromUserName(inputMsg.getToUserName());
			        outputMsg.setToUserName(inputMsg.getFromUserName());  
			        outputMsg.setCreateTime(Calendar.getInstance().getTimeInMillis() / 1000);  
					dealWithTianqi(text,outputMsg);
					return outputMsg;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void dealWithTianqi(String returnStr,OutputMessage outputMsg){
		 outputMsg.setMsgType(MsgType.News.getName());
    	String[] args = returnStr.split(":");
    	String content = args[0],listStr = args[1];
    	outputMsg.setContent(content);
    	String[] strs = listStr.split(";");
        List<Article> arts = new ArrayList<Article>();
    	for(int i = 0,len = strs.length;i<len;i++){
    		Article art1 = new Article(strs[i],"","","");
    		arts.add(art1);
    	}
    	outputMsg.setArticles(arts);
        outputMsg.setArticleCount(arts.size());
	}
}
