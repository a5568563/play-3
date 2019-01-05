package com.play.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.play.service.SectionService;

@Controller
@RequestMapping("/section")
public class SectionController {
	
	@Autowired
	SectionService service;
	
	@RequestMapping("/add")
	public void AddSection(@RequestParam String name, String description){
		System.out.println("name = "+name);
		System.out.println("description = " + description);
		service.Add_Section(name, description);
	}

}
