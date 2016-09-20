package com.hyc.weixin.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息过滤器链
 * @author yanglang
 *
 */
public class AutoMessageFilterChain {
	
	private int index;
	
	private List<AutoMessageFilter> filters = new ArrayList<AutoMessageFilter>();
	
	public AutoMessageFilterChain(){
	}
	
	public AutoMessageFilterChain(List<AutoMessageFilter> filters){
		this.setFilters(filters);
	}
	
	public List<AutoMessageFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<AutoMessageFilter> filters) {
		this.filters = filters;
	}

	public void addFilter(AutoMessageFilter filter){
		this.filters.add(filter);
	}
	
	/**
	 * 启动过滤 
	 * @param request
	 * @param response
	 */
	public void doFilter(MessageRequest request,MessageResponse response){
		if(index == filters.size()){
			index = 0;
			return;
		}
        //得到当前过滤器
        AutoMessageFilter filter = filters.get(index);
        index++;
 
        filter.doFilter(request, response, this);
	}
}
