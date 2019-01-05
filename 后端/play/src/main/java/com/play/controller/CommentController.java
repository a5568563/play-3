package com.play.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.play.entity.User;
import com.play.pojo.CommentAdd;
import com.play.pojo.NID;
import com.play.service.CommentService;
import com.play.service.UserService;
import com.play.util.PageModel;

@RestController
@CrossOrigin
public class CommentController {
	@Autowired
	CommentService service;
	@Autowired
	UserService userService;
	
	@PostMapping("/addcomment")
	public void Add_Comment(@RequestBody CommentAdd commentAdd) {
		User user = userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
		service.Add_Comment(commentAdd.getContent(), user.getId(), commentAdd.getNid());
	}
	
	@PostMapping("/get/comment")
	public PageModel Get_Comment(@RequestBody NID nid) {
		return service.Get_Comment(nid.getNid());
	}
}
