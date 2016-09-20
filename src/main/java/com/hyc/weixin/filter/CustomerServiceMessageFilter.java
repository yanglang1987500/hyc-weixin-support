package com.hyc.weixin.filter;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import org.anrhd.framework.context.AppConfig;
import org.anrhd.framework.data.FreemarkerHandler;
import org.anrhd.framework.util.DateTimeUtil;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyc.weixin.InputMessage;
import com.hyc.weixin.OutputMessage;
import com.hyc.weixin.WeiXinException;
import com.hyc.weixin.WeiXinUtil;

/**
 * 在线客服过滤器
 * @author yanglang
 *
 */
public class CustomerServiceMessageFilter implements AutoMessageFilter {
	
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
			if("event".equals(msgType) && "CLICK".equals(inputMsg.getEvent())){
	        	if("HYC_CUSTOM_SERVICE".equals(inputMsg.getEventKey())){
	        		try {
		        		//判断有无客服在线 若无 直接转机器人服务
		        		JSONArray csList = WeiXinUtil.getOnlineCustomServiceList();
		        		String respondMsg = "";
		        		if(csList.length()>0){
		        			respondMsg = "您好，有问题要咨询小云？我们已经收到您的请求，客服将尽快与您联系。请稍等！";
		        		}else{
		        			respondMsg = "您好，有问题要咨询？您的咨询请求我们已记录，客服稍后将主动联系您！（非工作时间和节假日回复可能不够及时，希望您能谅解！）";
		        		}
		        		outputMsg = new OutputMessage();
		                outputMsg.setFromUserName(inputMsg.getToUserName());
		                outputMsg.setToUserName(inputMsg.getFromUserName());  
		                outputMsg.setCreateTime(Calendar.getInstance().getTimeInMillis() / 1000);  
						WeiXinUtil.sendMessage(inputMsg.getFromUserName(), respondMsg);
						outputMsg.setMsgType("transfer_customer_service");  
		                outputMsg.setContent("来自和云筹服务器的转发：此用户点击了微信客服，时间："+DateTimeUtil.getCurrDateTime("yyyy-MM-dd HH:mm:ss")+"。");
			            request.setAttribute("outputMsg", outputMsg);
					} catch (WeiXinException e) {
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
	        	}
	        }
		}
		
        chain.doFilter(request, response);
	}

}
