package com.play.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author sy
 CREATE TABLE `operation` (
  `operation_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `new_id` int(11) NOT NULL,
  `operate_time` datetime NOT NULL,
  `operate_type` int(11) NOT NULL,
  PRIMARY KEY (`operation_id`),
  KEY `FK6ephw1ef4qhl1cnj2tlkfsyn` (`new_id`),
  KEY `FKgbajdwas0l2dy3c5xeailxfsn` (`user_id`),
  CONSTRAINT `FK6ephw1ef4qhl1cnj2tlkfsyn` FOREIGN KEY (`new_id`) REFERENCES `news` (`news_id`),
  CONSTRAINT `FKgbajdwas0l2dy3c5xeailxfsn` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
@Entity
@Table(name="operation")
public class Operation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="operation_id")
	int id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	User user;
	
	@ManyToOne
	@JoinColumn(name="new_id")
	News news;
	
	Date operate_time;

	int operate_type; // 1、点击进入新闻内容  2、评论  
	
	public Operation(){
		
	}
	
	public Operation (User user, News news,int type) {
		this.user = user;
		this.news = news;
		this.operate_time = new Date();
		this.operate_type = type;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public Date getOperate_time() {
		return operate_time;
	}

	public void setOperate_time(Date operate_time) {
		this.operate_time = operate_time;
	}

	public int getOperate_type() {
		return operate_type;
	}

	public void setOperate_type(int operate_type) {
		this.operate_type = operate_type;
	}
	
	
}
