package com.hyc.weixin.filter;

import com.hyc.weixin.InputMessage;
import com.hyc.weixin.OutputMessage;

/**
 * 微信 消息处理器接口
 * 用于处理微信消息
 * @author yanglang
 *
 */
public interface WXHandler {
	public OutputMessage handler(InputMessage inputMsg, String sendStr,String returnStr);
}
