package com.hyc.weixin.filter;

/**
 * 消息过滤器接口
 * @author yanglang
 *
 */
public interface AutoMessageFilter {
	public void doFilter(MessageRequest request,MessageResponse response,AutoMessageFilterChain chain);
}
