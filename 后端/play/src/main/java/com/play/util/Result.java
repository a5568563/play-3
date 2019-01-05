package com.play.util;

import java.util.Map;

public class Result {
	boolean status; //true成功 false失败
	Map<String, Object> data;//携带数据
	
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	
}
