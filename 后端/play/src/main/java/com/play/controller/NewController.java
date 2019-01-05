package com.play.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.play.pojo.NID;
import com.play.pojo.NewGet;
import com.play.repository.NewsRepository;
import com.play.repository.OperationRepository;
import com.play.service.NewService;
import com.play.service.UserService;
import com.play.util.PageModel;
import com.play.util.Result;
import com.play.entity.Operation;
import com.play.entity.User;
import com.play.pojo.AddCTR;

@RestController
@CrossOrigin
public class NewController {
	@Autowired
	NewsRepository repository;
	@Autowired
	NewService newService;
	@Autowired
	UserService userService;
	@Autowired
	OperationRepository operationRepository;
	/**
	 * 
	* @Title: GetNewTitleList   
	* @Description: TODO(这里用一句话描述这个方法的作用)   
	* @param @param nGet   nGet.sid存放板块id,获取指定板块新闻 nGet.pagenum获取页数
	* @param @return    设定文件   
	* @return Result    返回类型   
	* @throws   
	 */
	@PostMapping("/get/titlelist")
	public Result GetNewTitleList(@RequestBody NewGet nGet) {
		int sid = nGet.getSid();
		int pagenum = nGet.getPagenum();
		return newService.GetTitleList(sid, pagenum);
	}
	
	
	@PostMapping("/get/new")
	public Result GetNew(@RequestBody NID nid) {
		int newid = nid.getNid();
		try {
			return newService.Get_New(newid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	* @Title: AddCTR   
	* @Description: 点击新闻时增加CTR 并且根据用户增加板块兴趣度
	* @param @param actr    设定文件   
	* @return void    返回类型   
	* @throws   
	 */
	@PostMapping("/add/new_CTR")
	public void AddCTR(@RequestBody AddCTR actr) {
		User user = userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
		int nid = actr.getNid();
		newService.CTR_Add(nid);
		Operation operation = new Operation(user, repository.findById(nid).get(), 1);
		operationRepository.save(operation);
	}
	
	@PostMapping("/get/Recommend")
	public Result getRecommend() {
		User user = userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
		try {
			return newService.get_RecommendNews(user);
		} catch (Exception e) {
			System.out.println("冷启动");
			// TODO: handle exception
			return newService.Cold_Start(user.getInformation().getInterest());
		}
	}
}
