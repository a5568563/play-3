package com.play.pojo;

import java.util.Date;

import com.play.entity.User_info;

public class EasyComment implements Comparable<EasyComment>{
	User_info user_info;
	String content;
	Date createtime;
	
	public EasyComment(User_info user_info, String content, Date createtime) {
		super();
		this.user_info = user_info;
		this.content = content;
		this.createtime = createtime;
	}
	public User_info getUser_info() {
		return user_info;
	}
	public void setUser_info(User_info user_info) {
		this.user_info = user_info;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	@Override
	public int compareTo(EasyComment o) {
		// TODO Auto-generated method stub
		return o.getCreatetime().compareTo(this.getCreatetime());
	}
	
}
