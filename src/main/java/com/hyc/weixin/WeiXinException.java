package com.hyc.weixin;

/**
 * 调用微信服务的异常类.
 * @author Administrator
 *
 */
public class WeiXinException extends Exception{

    static final long serialVersionUID = -3387516993124229948L;

    public WeiXinException() {
	super();
    }

    public WeiXinException(String message) {
	super(message);
    }


    public WeiXinException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeiXinException(Throwable cause) {
        super(cause);
    }

}
