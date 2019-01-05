package com.play.service;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.entity.Section;
import com.play.repository.SectionRepository;

@Service("sectionService")
public class SectionService {
	@Autowired
	SectionRepository repository;
	
	/**
	 * 
	* @Title: Add_Section   
	* @Description: 添加一个模块   
	* @param    name:板块名	description:详情
	* @return void    返回类型   
	* @throws   
	 */
	@Transactional
	public void Add_Section (String name, String description) {
		Section section = new Section();
		Date createtime = new Date();
		section.setName(name);
		section.setDescription(description);
		section.setCreatetime(createtime);
		repository.save(section);
	}

}
