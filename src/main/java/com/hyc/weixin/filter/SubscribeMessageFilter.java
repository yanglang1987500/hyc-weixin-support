package com.hyc.weixin.filter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.anrhd.framework.context.AppConfig;
import org.anrhd.framework.data.FreemarkerHandler;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyc.weixin.InputMessage;
import com.hyc.weixin.OutputMessage;

/**
 * 初始关注过滤器
 * @author yanglang
 *
 */
public class SubscribeMessageFilter implements AutoMessageFilter {
	
	@Autowired
	private AppConfig appConfig;
	
	@Autowired
	private FreemarkerHandler freemarkerHandler;
	
	@Override
	public void doFilter(MessageRequest request, MessageResponse response,
			AutoMessageFilterChain chain) {
		InputMessage inputMsg = (InputMessage)request.getAttribute("inputMsg");
		OutputMessage outputMsg = (OutputMessage)request.getAttribute("outputMsg");
		if(outputMsg == null){
			String msgType = inputMsg.getMsgType();
			if("event".equals(msgType) && "subscribe".equals(inputMsg.getEvent())){
	        	outputMsg = new OutputMessage();
	            outputMsg.setFromUserName(inputMsg.getToUserName());
	            outputMsg.setToUserName(inputMsg.getFromUserName());  
	            outputMsg.setCreateTime(Calendar.getInstance().getTimeInMillis() / 1000);  
	            outputMsg.setMsgType("text"); 
	            Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("weixin_appid", appConfig.getConfig("weixin_appid"));
				resultMap.put("host", appConfig.getConfig("param.hyc.url"));
	            outputMsg.setContent(freemarkerHandler.getString(
						"/message/reply4subscribe.ftl", resultMap));
	            request.setAttribute("outputMsg", outputMsg);
			}
		}
		
        chain.doFilter(request, response);

	}

}
