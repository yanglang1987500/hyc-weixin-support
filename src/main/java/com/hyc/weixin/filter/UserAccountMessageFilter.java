package com.hyc.weixin.filter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.anrhd.framework.context.AppConfig;
import org.anrhd.framework.data.FreemarkerHandler;
import org.anrhd.framework.domain.AppMap;
import org.anrhd.framework.service.tools.LocalServiceTools;
import org.anrhd.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.hyc.weixin.InputMessage;
import com.hyc.weixin.OutputMessage;

/**
 * 用户账号信息过滤器
 * 用于查找用户账户信息数据
 * @author yanglang
 *
 */
public class UserAccountMessageFilter implements AutoMessageFilter {
	
	@Autowired
	private LocalServiceTools localServiceTools;
	
	@Autowired
	private FreemarkerHandler freemarkerHandler;
	
	private static final String splitReg = "\\|";
	@Autowired
	private AppConfig appConfig; 
	
	@Override
	public void doFilter(MessageRequest request, MessageResponse response,
			AutoMessageFilterChain chain) {
		InputMessage inputMsg = (InputMessage)request.getAttribute("inputMsg");
		OutputMessage outputMsg = (OutputMessage)request.getAttribute("outputMsg");
		if(outputMsg == null){
			String userId = (String)request.getAttribute("userid");//用户系统id
			String message = inputMsg.getContent();//查询关键字
			
			// 增加对菜单的兼容性
			if("event".equals(inputMsg.getMsgType()) && "CLICK".equals(inputMsg.getEvent())){
				message=inputMsg.getEventKey();
			}
			
			QueryType type = checkType(message);
			if(type != QueryType.Null){
				outputMsg = new OutputMessage();
	            outputMsg.setFromUserName(inputMsg.getToUserName());
	            outputMsg.setToUserName(inputMsg.getFromUserName());  
	            outputMsg.setCreateTime(Calendar.getInstance().getTimeInMillis() / 1000);  
	            outputMsg.setMsgType("text"); 
				
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("weixin_appid", appConfig.getConfig("weixin_appid"));
				resultMap.put("host", appConfig.getConfig("param.hyc.url"));
				resultMap.put("message", message);
				
				if(!StringUtil.hasText(userId)){
					//用户尚未绑定微信 
					outputMsg.setContent(freemarkerHandler.getString(
							"/message/reply4notbind.ftl", resultMap));
				}else{
					resultMap.putAll(queryAsset(userId));
					switch(type){
						case Asset:
							outputMsg.setContent(freemarkerHandler.getString(
									"/message/reply4asset.ftl", resultMap));	
							break;
						case Balance:
							outputMsg.setContent(freemarkerHandler.getString(
									"/message/reply4balance.ftl", resultMap));	
							break;
						case Income:
							outputMsg.setContent(freemarkerHandler.getString(
									"/message/reply4income.ftl", resultMap));	
							break;
						case Hebalance:
							outputMsg.setContent(freemarkerHandler.getString(
									"/message/reply4hebalance.ftl", resultMap));	
							break;
						default:
							break;
					}
				}
	            request.setAttribute("outputMsg", outputMsg);
			}
		}
		
        chain.doFilter(request, response);
	}
	
	/**
	 * 根据用户id查询资产信息
	 * @param userId 用户id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> queryAsset(String userId){
		AppMap appMap = new AppMap();
		Map<String,Object> param = new HashMap<String,Object>();
		appMap.setIn(param);
		param.put("u_i_id", userId);
		this.localServiceTools.callService(appMap, "tx", "100158");
		if(appMap.getStatusData().isResultStatus()){
			return (Map<String, Object>)appMap.getOut().get("resultMap");
		}
		return null;
	}
	
	/**
	 * 检查查询类型
	 * @param message 用户发送消息关键字
	 * @return
	 */
	private QueryType checkType(String message){
		//匹配 资产
		String assetKeyStr = appConfig.getConfig("autoMessageReply4Asset");
		if(StringUtils.hasText(assetKeyStr)){
			String[] assetKeys = assetKeyStr.split(splitReg);
			if(doCheckType(message,assetKeys))
				return QueryType.Asset;
		}
		//匹配 余额
		String balanceKeyStr = appConfig.getConfig("autoMessageReply4Balance");
		if(StringUtils.hasText(balanceKeyStr)){
			String[] balanceKeys = balanceKeyStr.split(splitReg);
			if(doCheckType(message,balanceKeys))
				return QueryType.Balance;
		}
		//匹配 收益
		String incomeKeyStr = appConfig.getConfig("autoMessageReply4Income");
		if(StringUtils.hasText(incomeKeyStr)){
			String[] incomeKeys = incomeKeyStr.split(splitReg);
			if(doCheckType(message,incomeKeys))
				return QueryType.Income;
		}
		//匹配 和币
		String hebalanceKeyStr = appConfig.getConfig("autoMessageReply4Hebalance");
		if(StringUtils.hasText(hebalanceKeyStr)){
			String[] hebalanceKeys = hebalanceKeyStr.split(splitReg);
			if(doCheckType(message,hebalanceKeys))
				return QueryType.Hebalance;
		}
		return QueryType.Null;
	}
	
	/**
	 * 执行检查消息类型
	 * 全字匹配
	 * @param message 消息
	 * @param keys 规则关键字列表
	 * @return
	 */
	private boolean doCheckType(String message,String[] keys){
		for(String key : keys){
			if(StringUtil.hasText(key) && key.equals(message))
				return true;
		}
		return false;
	}
	
	/**
	 * 查询类型
	 * @author yanglang
	 *
	 */
	enum QueryType{
		Null,Asset,Balance,Income,Hebalance
	}

}
