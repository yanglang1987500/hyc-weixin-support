package com.hyc.weixin.filter;

import java.util.Calendar;
import java.util.List;

import org.anrhd.framework.service.tools.LocalServiceTools;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyc.weixin.InputMessage;
import com.hyc.weixin.OutputMessage;
import com.hyc.weixin.beans.MessageRule;
import com.hyc.weixin.beans.MessageRuleKey;

/**
 * 静态消息过滤器
 * 处理与账户无关的自动回复
 * @author yanglang
 *
 */
public class StaticMessageFilter implements AutoMessageFilter {

	@Autowired
	private LocalServiceTools localServiceTools;
	@Autowired
	private StaticMessageCache staticMessageCache;
	
	@Override
	public void doFilter(MessageRequest request, MessageResponse response,
			AutoMessageFilterChain chain) {
		InputMessage inputMsg = (InputMessage)request.getAttribute("inputMsg");
		OutputMessage outputMsg = (OutputMessage)request.getAttribute("outputMsg");
		if(outputMsg == null){
			String message = inputMsg.getContent();//查询关键字
			String result = null;
			List<MessageRule> rules = staticMessageCache.getRulesCache();
			if(rules!=null && message != null)
				for(MessageRule rule:rules){
					boolean exit = false;
					List<MessageRuleKey> keys = rule.getKeys();
					if(keys!=null)
						for(MessageRuleKey key:keys){
							//String matchType = key.getMatchType();
							String keyValue  = key.getKey();
							if(keyValue.equals(message)){
								result = rule.getReplay();
								exit = true;
								break;
							}
							// 取消模糊匹配 2016.05 by Caigen
//							if("01".equals(matchType)){
//								if(keyValue.equals(message)){
//									result = rule.getReplay();
//									exit = true;
//									break;
//								}
//							}else{
//								if(message.indexOf(keyValue)!=-1 || keyValue.indexOf(message)!=-1){
//									result = rule.getReplay();
//									exit = true;
//									break;
//								}
//							}
						}
					if(exit)
						break;
				}
			if(result!=null){
				outputMsg = new OutputMessage();
	            outputMsg.setFromUserName(inputMsg.getToUserName());
	            outputMsg.setToUserName(inputMsg.getFromUserName());  
	            outputMsg.setCreateTime(Calendar.getInstance().getTimeInMillis() / 1000);  
	            outputMsg.setMsgType("text");
	            outputMsg.setContent(result);
				request.setAttribute("outputMsg", outputMsg);
			}
		}
        chain.doFilter(request, response);
	}
}
