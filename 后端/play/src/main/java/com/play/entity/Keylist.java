package com.play.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/*
 * CREATE TABLE `keylist` (
  `keylist_id` int(11) NOT NULL AUTO_INCREMENT,
  `new_id` int(11) NOT NULL,
  `section_id` int(11) DEFAULT NULL,
  `total_num` int(11) DEFAULT NULL,
  `key_group` varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`keylist_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2104 DEFAULT CHARSET=utf8;
 */
@Entity
@Table(name="keylist")
public class Keylist {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="keylist_id")
	int id;
	
	@ManyToOne
	@JoinColumn(name="new_id")
	News news;
	
	@ManyToOne
	@JoinColumn(name="section_id")
	Section section;
	
	int total_num;
	
	String key_group;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public int getTotal_num() {
		return total_num;
	}

	public void setTotal_num(int total_num) {
		this.total_num = total_num;
	}

	public String getKey_group() {
		return key_group;
	}

	public void setKey_group(String key_group) {
		this.key_group = key_group;
	}
	
	
}
