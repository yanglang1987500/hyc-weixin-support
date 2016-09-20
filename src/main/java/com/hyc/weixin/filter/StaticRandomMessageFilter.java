package com.hyc.weixin.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.List;

import org.anrhd.framework.context.AppConfig;
import org.anrhd.framework.service.tools.LocalServiceTools;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyc.weixin.InputMessage;
import com.hyc.weixin.OutputMessage;
import com.hyc.weixin.WeiXinUtil;

/**
 * 静态随机消息过滤器
 * 放在最后一个 用于处理前面没有任何回复的情况
 * 调用图灵机器人接口随机取一段笑话之类的给用户
 * @author yanglang
 *
 */
public class StaticRandomMessageFilter implements AutoMessageFilter {

	@Autowired
	private LocalServiceTools localServiceTools;
	@Autowired
	private StaticMessageCache staticMessageCache;
	@Autowired
	private AppConfig appConfig;
	//处理器列表 由app-context-wx-support.xml 进行配置
	private List<WXHandler> handlers;
	
	private static String tulingAPI = "http://www.tuling123.com/openapi/api?key={0}&userid={1}&info={2}"; 
	
	@Override
	public void doFilter(MessageRequest request, MessageResponse response,
			AutoMessageFilterChain chain) {
		InputMessage inputMsg = (InputMessage)request.getAttribute("inputMsg");
		OutputMessage outputMsg = (OutputMessage)request.getAttribute("outputMsg");
		String message = inputMsg.getContent();//查询关键字
		if(outputMsg == null && message != null){
			try {
				String url = MessageFormat.format(tulingAPI, appConfig.getConfig("tuling_apikey"),
						inputMsg.getFromUserName(), URLEncoder.encode(message, "utf-8"));
				String returnStr = WeiXinUtil.callTulingService("get",url,null);
				JSONObject tulingResult = new JSONObject(returnStr);
				if(tulingResult.has("code")){
					if(handlers!=null)
						for(WXHandler handler : handlers ){
							outputMsg = handler.handler(inputMsg,message,returnStr);
							if(outputMsg!=null){
								request.setAttribute("outputMsg", outputMsg);
								break;
							}
						}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
        chain.doFilter(request, response);
	}
	
	public List<WXHandler> getHandlers() {
		return handlers;
	}

	public void setHandlers(List<WXHandler> handlers) {
		this.handlers = handlers;
	}
	
}
