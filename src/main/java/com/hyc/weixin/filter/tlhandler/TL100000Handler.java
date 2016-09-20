package com.hyc.weixin.filter.tlhandler;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.hyc.weixin.InputMessage;
import com.hyc.weixin.MsgType;
import com.hyc.weixin.OutputMessage;
import com.hyc.weixin.filter.WXHandler;

/**
 * 文本回复处理器
 * @author yanglang
 *
 */
public class TL100000Handler implements WXHandler {

	@Override
	public OutputMessage handler(InputMessage inputMsg, String sendStr, String returnStr) {
		
		try {
			JSONObject tulingResult = new JSONObject(returnStr);
			String code = tulingResult.getString("code");
			if("100000".equals(code)){
				String text = tulingResult.optString("text");
				text = text.replaceAll("<br>", "\n");
				if(sendStr.indexOf("天气")==-1){
					OutputMessage outputMsg = new OutputMessage();
			        outputMsg.setFromUserName(inputMsg.getToUserName());
			        outputMsg.setToUserName(inputMsg.getFromUserName());  
			        outputMsg.setCreateTime(Calendar.getInstance().getTimeInMillis() / 1000);  
			        outputMsg.setMsgType(MsgType.Text.getName());
			        outputMsg.setContent(text);
					return outputMsg;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
