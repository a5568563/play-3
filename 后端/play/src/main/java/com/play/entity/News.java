package com.play.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;



/*CREATE TABLE `news` (
  `news_id` int(11) NOT NULL AUTO_INCREMENT,
  `index` int(11) DEFAULT NULL,
  `section_id` int(11) DEFAULT NULL,
  `title` varchar(64) NOT NULL,
  `title_picture` varchar(64) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `CTR` int(11) DEFAULT NULL COMMENT '鐐瑰嚮閲?',
  `createtime` varchar(32) NOT NULL,
  PRIMARY KEY (`news_id`),
  KEY `FKp7jdp21lrskejcqdr0edfl3va` (`section_id`),
  CONSTRAINT `FKp7jdp21lrskejcqdr0edfl3va` FOREIGN KEY (`section_id`) REFERENCES `section` (`section_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8;


*/
@Entity
@Table(name="news")
public class News  implements Comparable<News>{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="news_id")
	int id;
	
	@ManyToOne
	@JoinColumn(name="section_id")
	Section section;
	
	
	String title;
	
	String title_picture;
	
	String content;
	
	int CTR;
	
	String createtime;
	
	@OneToMany(mappedBy="news")
	Set<Comment> comments = new HashSet<Comment>();

	Date inserttime;
	
	int hot;
	
	public int getHot() {
		return hot;
	}

	public void setHot(int hot) {
		this.hot = hot;
	}

	public Date getInserttime() {
		return inserttime;
	}

	public void setInserttime(Date inserttime) {
		this.inserttime = inserttime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public Section getSection() {
		return section;
	}

	@JsonBackReference
	public void setSection(Section section) {
		this.section = section;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle_picture() {
		return title_picture;
	}

	public void setTitle_picture(String title_picture) {
		this.title_picture = title_picture;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getCTR() {
		return CTR;
	}

	public void setCTR(int cTR) {
		CTR = cTR;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	@Override
	public int compareTo(News o) {
		// TODO Auto-generated method stub
		return o.getInserttime().compareTo(this.getInserttime());
	}
}
