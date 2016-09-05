package com.yunbei.shorturl.core.event;

public enum EventType {

	VISITOR_LOG(1, "短链接访问记录事件");

	private int type;

	private String descr;

	private EventType(int type, String descr) {
		this.type = type;
		this.descr = descr;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

}
