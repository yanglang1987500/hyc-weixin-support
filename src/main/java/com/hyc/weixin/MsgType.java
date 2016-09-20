package com.hyc.weixin;

public enum MsgType {
	Text("text", 1),Image("image",2),Voice("voice",3),Video("video",4),Location("location",5),News("news",6);
	
	private String name;
	private int index;

	private MsgType(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public static String getName(int index) {
		for (MsgType c : MsgType.values()) {
			if (c.getIndex() == index) {
				return c.name;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public String toString(){
		return this.name;
	}

}
