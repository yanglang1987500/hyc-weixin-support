package com.hyc.weixin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.anrhd.framework.context.AppConfig;
import org.anrhd.framework.domain.AppMap;
import org.anrhd.framework.service.tools.LocalServiceTools;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 微信服务类.
 * @author Administrator
 *
 */
public class WeiXinUtil {
	
	static Logger log=LoggerFactory.getLogger(WeiXinUtil.class);
	
	@Autowired
	static AppConfig appConfig;

	
	static LocalServiceTools localServiceTools;
	//生产
//	private static String appid = "wx7151aa3ca063ee65";
//	private static String secret = "e81c541301c39aa1ad769acb14602327";
	
	//测试
	private static String appid = "";
	private static String secret = "";
	 
	
	private static String accessToken = null;
	// jssdk apiticket
	private static String ticket = null;
	
	private static String ticketAPI = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={0}&type=jsapi"; 

	private static String accessTokenAPI = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}"; 

	private static String openIdAPI = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";
	
	private static String sendTemplateMessageAPI = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={0}";
	
	private static String sendMessageAPI = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token={0}";
	
	private static String getOnlineCustomServiceListAPI = "https://api.weixin.qq.com/cgi-bin/customservice/getonlinekflist?access_token={0}";
	
	private static final String WEIXINKEY="weixinToken";
	private static final String TICKETKEY="weixinTicket";
	
	
	
	/**
	 * 获取关注者id。
	 * @param authCode 授权码 ，该授权码使用一次后失效.
	 * @return
	 * @throws WeiXinException
	 */
	public static String getOpenId(String authCode) throws WeiXinException{
		try {
			if(authCode != null){
				authCode = authCode.trim();
			}
			String url = MessageFormat.format(openIdAPI, appid, secret,authCode);
			String returnStr = callService("get",url,null);
			System.out.println(returnStr);
			
			JSONObject result = new JSONObject(returnStr);
			if(result.has("errcode")){
				String errCode = result.getString("errcode");
				if(errCode != null){
					throw new WeiXinException(result.getString("errmsg")+"("+errCode+") url:" + url);
				}
			}
			return result.getString("openid");
		} catch (JSONException e) {
			throw new WeiXinException(e);
		}
	}
	
	/**
	 * 调用微信服务代理接口
	 * @param wx_action
	 * @param wx_url
	 * @param wx_param
	 * @return
	 */
	public static String callService(String wx_action,String wx_url,String wx_param){
		AppMap appMap = new AppMap();
		Map<String,Object> in = new HashMap<String,Object>();
		in.put("wx_action", wx_action);
		in.put("wx_url", wx_url);
		in.put("wx_param", wx_param);
		appMap.setIn(in);
		localServiceTools.callService(appMap, "proxy", "Weixin");
		return (String)appMap.getOut().get("wx_response");
	}
	
	/**
	 * 调用图灵服务代理接口
	 * @param tl_action
	 * @param tl_url
	 * @param tl_param
	 * @return
	 */
	public static String callTulingService(String tl_action,String tl_url,String tl_param){
		AppMap appMap = new AppMap();
		Map<String,Object> in = new HashMap<String,Object>();
		in.put("tuling_url", tl_url);
		appMap.setIn(in);
		localServiceTools.callService(appMap, "proxy", "Tuling");
		return (String)appMap.getOut().get("tuling_response");
	}

	
	/**
	 * 刷新微信accesstoken值
	 * 因为微信的accesstoken只有两个小时有效期，需要定时进行刷新 
	 * @throws WeiXinException 
	 */
	public static void refreshToken() throws WeiXinException{
		log.info("token刷新--- start ");
		try {
			String url = MessageFormat.format(accessTokenAPI, appid, secret);
			String returnStr = callService("get",url,null);
			System.out.println(returnStr);
			JSONObject result = new JSONObject(returnStr);
			if(result.has("errcode")){
				String errCode = result.getString("errcode");
				if(errCode != null){
					throw new WeiXinException(result.getString("errmsg")+"("+errCode+")");
				}
			}
			accessToken = result.getString("access_token");
			// redis 写入
			writeRedis(WEIXINKEY, accessToken);
			
			log.info("token刷新--- end ");
		} catch (JSONException e) {
			throw new WeiXinException(e);
		}
		// 接着刷新ticket,2016 Add
		refreshTicket();
	}
	
	/**
	 * redis数据写入
	 * @param key
	 * @param data
	 */
	public static void writeRedis(String key, String data){
		key=key+"_"+getAppid();
		AppMap appMap = new AppMap();
		Map<String,Object> in = new HashMap<String,Object>();  
		in.put("_cache_redis_key_", key);
		in.put("action", "write"); 
		in.put("_cache_redis_data_", data);
		appMap.setIn(in); 
		localServiceTools.callService(appMap, "tx", "Redis");
	}
	
	/**
	 * 读取redis数据
	 * @param key
	 * @return
	 */
	public static String readRedis(String key){
		key=key+"_"+getAppid();
		AppMap appMap = new AppMap();
		Map<String,Object> in = new HashMap<String,Object>();  
		in.put("_cache_redis_key_", key);
		in.put("action", "read");  
		appMap.setIn(in); 
		localServiceTools.callService(appMap, "tx", "Redis");
		String data=appMap.getOut().get(key).toString();
		return data;
	}
	
	/**
	 * 刷新jssdk-Ticket
	 * @throws WeiXinException
	 */
	public static void refreshTicket() throws WeiXinException{
		log.info("ticket刷新--- start ");
		try {
			String url = MessageFormat.format(ticketAPI, getAccessToken());
			String returnStr = callService("get",url,null);
			System.out.println(returnStr);
			JSONObject result = new JSONObject(returnStr);
			String errCode = result.getString("errcode");

			if(errCode!=null&&!errCode.equals("0")){
				throw new WeiXinException(result.getString("errmsg")+"("+errCode+")");
			}

			ticket = result.getString("ticket");
			// redis 写入
			writeRedis(TICKETKEY, ticket);
			log.info("ticket刷新--- end ");
		} catch (JSONException e) {
			throw new WeiXinException(e);
		}
	}
	
	/**
	 * 发送模板消息给指定用户
	 * @param toUser 微信用户openid
	 * @param template_id 微信消息模板id
	 * @param linkUrl 详情链接
	 * @param topColor 顶部颜色  目前版本的微信此参数已不生效
	 * @param data JSONObject数据对象，用于指定模板中对应的键值对
	 * @throws WeiXinException
	 */
	public static void sendTemplateMessage(String toUser,String template_id,String linkUrl,String topColor,JSONObject data) throws WeiXinException{
		try {
			String url = MessageFormat.format(sendTemplateMessageAPI, getAccessToken());
			JSONObject param = new JSONObject();
			param.put("touser", toUser);
			param.put("template_id", template_id);
			param.put("url", linkUrl);
			param.put("topcolor", topColor);
			param.put("data", data);
			// TODO remove
			log.info(param.toString());
			callService("post",url,param.toString());
			
		} catch (JSONException e) {
			throw new WeiXinException(e);
		}
	}
	
	/**
	 * 发送模板消息给指定用户
	 * @param toUser 微信用户openid
	 * @param template_id 微信消息模板id
	 * @param linkUrl 详情链接
	 * @param data JSONObject数据对象，用于指定模板中对应的键值对
	 * @throws WeiXinException
	 */
	public static void sendTemplateMessage(String toUser,String template_id,String linkUrl,JSONObject data) throws WeiXinException{
		sendTemplateMessage(toUser,template_id,linkUrl,"#ff0000",data);
	}
	
	/**
	 * 发送模板消息给指定用户
	 * @param toUser 微信用户openid
	 * @param template_id 微信消息模板id
	 * @param data JSONObject数据对象，用于指定模板中对应的键值对
	 * @throws WeiXinException
	 */
	public static void sendTemplateMessage(String toUser,String template_id,JSONObject data) throws WeiXinException{
		sendTemplateMessage(toUser,template_id,"","#ff0000",data);
	}
	
	/**
	 * 获取微信 accesstoken
	 * @return
	 */
	public static String getAccessToken(){
		if(accessToken == null)
			try {
				refreshToken();
			} catch (WeiXinException e) {}
		accessToken=readRedis( WEIXINKEY );
		System.out.println("weixin AccessToken :"+accessToken);
		return accessToken;
	}
	
	/**
	 * 获得ticket
	 * @return
	 */
	public static String getTicket(){
		if(ticket == null)
			try {
				refreshTicket();
			} catch (WeiXinException e) {}
		ticket=readRedis(TICKETKEY );
		System.out.println("weixin Ticket:"+ticket);
		return ticket;
	}
	
	/**
	 * 发送普通消息给指定用户
	 * @param toUser 微信用户openid
	 * @param linkUrl 详情链接
	 * @param topColor 顶部颜色  目前版本的微信此参数已不生效
	 * @param data JSONObject数据对象，用于指定模板中对应的键值对
	 * @throws WeiXinException
	 * @throws UnsupportedEncodingException 
	 */
	public static void sendMessage(String toUser,String message) throws WeiXinException, UnsupportedEncodingException{
		try {
			String url = MessageFormat.format(sendMessageAPI, getAccessToken());
			JSONObject param = new JSONObject();
			param.put("touser", toUser);
			param.put("msgtype", "text");
			param.put("text",new JSONObject("{\"content\":\""+URLEncoder.encode(message,"utf-8")+"\"}")); 
			callService("post",url,URLDecoder.decode(param.toString(),"utf-8"));
		}  catch (JSONException e) {
			throw new WeiXinException(e);
		}
	}
	
	
	/**
	 * 获取在线客服列表
	 * @return
	 * @throws WeiXinException
	 */
	public static JSONArray getOnlineCustomServiceList() throws WeiXinException{
		try {
			String url = MessageFormat.format(getOnlineCustomServiceListAPI, getAccessToken());
			String result = callService("get",url,null);
			JSONObject obj = new JSONObject(result);
			if(obj.has("kf_online_list"))
				return obj.getJSONArray("kf_online_list");
			else
				return new JSONArray();
		} catch (Exception e) {
			throw new WeiXinException(e);
		} 
	}
	
	/**
	 * 创建微信关键字模板内容json字符串
	 * @param keywords 关键字对象列表
	 * @return
	 * @throws JSONException
	 */
	public static String createData(List<TplKeywordBean> keywords) throws JSONException{
		JSONObject result = new JSONObject();
		for(TplKeywordBean keyword:keywords){
			// 设置统一的信息头，和信息尾巴，统一格式处理 2016.04.20 by Caigen
			if("first".equals(keyword.getName())){
				keyword.setValue("【和云筹】亲爱的和粉您好，"+keyword.getValue()+"\n");
			}else if("remark".equals(keyword.getName())){
				keyword.setValue(keyword.getValue()+"如有任何疑问，请致电 400-919-6600");
			}else{
				keyword.setValue(keyword.getValue()+"\n");
			}
			JSONObject nameJson = new JSONObject();
			nameJson.put("value", keyword.getValue());
			nameJson.put("color", keyword.getColor());
			result.put(keyword.getName(), nameJson);
		}
		// TODO remove
		log.info(result.toString());
		return result.toString();
	}
	
	public static void setAppid(String appid) {
		WeiXinUtil.appid = appid;
	}

	public static void setSecret(String secret) {
		WeiXinUtil.secret = secret;
	}
	
	public static String getAppid(){
		return appid;
	}
	
	public static String getSecret(){
		return secret;
	}
	
	@Autowired
	public void setLocalServiceTools(LocalServiceTools tools){
		WeiXinUtil.localServiceTools = tools;
	}
	

}
