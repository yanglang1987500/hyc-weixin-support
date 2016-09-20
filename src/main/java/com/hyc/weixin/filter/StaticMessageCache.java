package com.hyc.weixin.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.anrhd.framework.domain.AppMap;
import org.anrhd.framework.service.tools.LocalServiceTools;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyc.simple.domain.PubMsgRespondConst;
import com.hyc.simple.domain.PubMsgRespondKeysConst;
import com.hyc.weixin.beans.MessageRule;
import com.hyc.weixin.beans.MessageRuleKey;

/**
 * 静态消息缓存类
 * @author yanglang
 *
 */
public class StaticMessageCache {
	
	@Autowired
	private LocalServiceTools localServiceTools;
	
	/**
	 * 普通回复规则缓存
	 */
	private static List<MessageRule> rulesCache;
	
	/**
	 * 随机回复规则缓存
	 */
	private static List<MessageRule> randomRulesCache;
	
	/**
	 * 重置缓存
	 */
	public void reload(){
		rulesCache = new ArrayList<MessageRule>();
		randomRulesCache = new ArrayList<MessageRule>();
		List<Map<String,Object>> rules = queryRules();
		Map<String,MessageRule> temp = new HashMap<String,MessageRule>();
		for(Map<String,Object> rule:rules){
			MessageRule mrule = null;
			if(temp.containsKey(rule.get(PubMsgRespondConst.M_R_ID))){
				mrule = temp.get(rule.get(PubMsgRespondConst.M_R_ID));
				if(rule.get(PubMsgRespondKeysConst.M_R_K_ID)!=null)
					mrule.addKey(new MessageRuleKey(
								(String)rule.get(PubMsgRespondKeysConst.M_R_K_ID),
								(String)rule.get(PubMsgRespondKeysConst.M_R_KEY),
								(String)rule.get(PubMsgRespondKeysConst.MATCH_TYPE),
								mrule
							));
			}else{
				mrule = new MessageRule();
				mrule.setId((String)rule.get(PubMsgRespondConst.M_R_ID));
				mrule.setTitle((String)rule.get(PubMsgRespondConst.M_R_TITLE));
				mrule.setReplay((String)rule.get(PubMsgRespondConst.M_R_REPLY));
				mrule.setIsRandom((String)rule.get(PubMsgRespondConst.M_R_ISRANDOM));
				mrule.setOrder((String)rule.get(PubMsgRespondConst.M_R_ORDER));
				mrule.setKeys(new ArrayList<MessageRuleKey>());
				if(rule.get(PubMsgRespondKeysConst.M_R_K_ID)!=null){
					mrule.addKey(new MessageRuleKey(
							(String)rule.get(PubMsgRespondKeysConst.M_R_K_ID),
							(String)rule.get(PubMsgRespondKeysConst.M_R_KEY),
							(String)rule.get(PubMsgRespondKeysConst.MATCH_TYPE),
							mrule
						));
				}
				if("01".equals(mrule.getIsRandom())){
					rulesCache.add(mrule);
				}else{
					randomRulesCache.add(mrule);
				}
				temp.put(mrule.getId(), mrule);
			}
			
		}
	}

	/**
	 * 查询规则
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String,Object>> queryRules(){
		AppMap appMap = new AppMap();
		Map<String,Object> param = new HashMap<String,Object>();
		appMap.setIn(param);
		param.put("action", "001");
		this.localServiceTools.callService(appMap, "tx", "AutoMessage");
		if(appMap.getStatusData().isResultStatus()){
			return (List<Map<String,Object>>)appMap.getOut().get("rules");
		}
		return null;
	}

	/**
	 * 获取缓存消息规则
	 * @return
	 */
	public List<MessageRule> getRulesCache() {
		if(rulesCache == null)
			reload();
		return rulesCache;
	}

	public void setRulesCache(List<MessageRule> rulesCache) {
		this.rulesCache = rulesCache;
	}

	/**
	 * 获取缓存随机消息规则
	 * @return
	 */
	public List<MessageRule> getRandomRulesCache() {
		if(randomRulesCache == null)
			reload();
		return randomRulesCache;
	}

	public void setRandomRulesCache(List<MessageRule> randomRulesCache) {
		this.randomRulesCache = randomRulesCache;
	}
	
}
