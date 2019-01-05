package com.play.entity;

import static org.junit.Assert.assertFalse;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;




/*CREATE TABLE `user` (
`user_id` int(11) NOT NULL AUTO_INCREMENT,
`user_info_id` int(11) DEFAULT NULL,
`username` varchar(32) NOT NULL,
`password` varchar(32) NOT NULL,
`createtime` datetime DEFAULT NULL,
PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

*/
@Entity
@Table(name="user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id",nullable=false, unique=true)
	int id;
	
	@OneToOne
	@JoinColumn(name="user_info_id")
	User_info information;
	
	String username;
	
	String password;
	
	Date createtime;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    private List<Role> roles;
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	public User(String username, String password) {
		// TODO Auto-generated constructor stub
		this.setUsername(username);
		this.setPassword(password);
		this.setCreatetime(new Date());
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User_info getInformation() {
		return information;
	}

	public void setInformation(User_info information) {
		this.information = information;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	
	
}
