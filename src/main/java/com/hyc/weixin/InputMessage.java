package com.hyc.weixin;

import java.io.Serializable;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class InputMessage implements Serializable {

	private static final long serialVersionUID = -2166419875702986489L;
	// 消息接收方
	@XStreamAlias("ToUserName")
	private String ToUserName;
	// 消息发送方，用户openid
	@XStreamAlias("FromUserName")
	private String FromUserName;
	// 消息创建时间
	@XStreamAlias("CreateTime")
	private Long CreateTime;
	// 消息类型
	@XStreamAlias("MsgType")
	private String MsgType = "text";
	// 消息ID
	@XStreamAlias("MsgId")
	private String MsgId;
	
	// 文本消息
	@XStreamAlias("Content")
	private String Content;
	// 图片消息
	@XStreamAlias("PicUrl")
	private String PicUrl;
	// 位置消息，经度
	@XStreamAlias("LocationX")
	private String LocationX;
	// 位置消息，纬度
	@XStreamAlias("LocationY")
	private String LocationY;
	@XStreamAlias("Scale")
	private Long Scale;
	@XStreamAlias("Label")
	private String Label;
	// 链接消息
	@XStreamAlias("Title")
	private String Title;
	@XStreamAlias("Description")
	private String Description;
	@XStreamAlias("Url")
	private String URL;
	// 语音信息
	@XStreamAlias("MediaId")
	private String MediaId;
	@XStreamAlias("Format")
	private String Format;
	@XStreamAlias("Recognition")
	private String Recognition;
	// 事件
	@XStreamAlias("Event")
	private String Event;
	@XStreamAlias("EventKey")
	private String EventKey;
	@XStreamAlias("Ticket")
	private String Ticket;
	
	// 消息ID, 调用模板消息成功后返回
	@XStreamAlias("MsgID")
	private String MsgID;
	@XStreamAlias("Status")
	private String Status;

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public Long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Long createTime) {
		CreateTime = createTime;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}  
	
	public String getMsgId() {
		return MsgId;
	}

	public void setMsgId(String msgId) {
		MsgId = msgId;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

	public String getLocationX() {
		return LocationX;
	}

	public void setLocationX(String locationX) {
		LocationX = locationX;
	}

	public String getLocationY() {
		return LocationY;
	}

	public void setLocationY(String locationY) {
		LocationY = locationY;
	}

	public Long getScale() {
		return Scale;
	}

	public void setScale(Long scale) {
		Scale = scale;
	}

	public String getLabel() {
		return Label;
	}

	public void setLabel(String label) {
		Label = label;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getEvent() {
		return Event;
	}

	public void setEvent(String event) {
		Event = event;
	}

	public String getEventKey() {
		return EventKey;
	}

	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String getFormat() {
		return Format;
	}

	public void setFormat(String format) {
		Format = format;
	}

	public String getRecognition() {
		return Recognition;
	}

	public void setRecognition(String recognition) {
		Recognition = recognition;
	}

	public String getTicket() {
		return Ticket;
	}

	public void setTicket(String ticket) {
		Ticket = ticket;
	}
	
	public String getMsgID() {
		return MsgID;
	}

	public void setMsgID(String msgID) {
		MsgID = msgID;
	} 

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public static InputMessage transfromMap(Map<String, Object> obj) {
		InputMessage result = new InputMessage();
		result.setContent((String) obj.get("content"));
		result.setCreateTime(Long.parseLong(String.valueOf(obj
				.get("createTime"))));
		result.setToUserName((String) obj.get("toUserName"));
		result.setFromUserName((String) obj.get("fromUserName"));
		result.setMsgType((String) obj.get("msgType"));
		result.setEvent((String) obj.get("event"));
		result.setEventKey((String) obj.get("eventKey"));
		return result;
	}
}