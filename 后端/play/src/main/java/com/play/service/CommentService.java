package com.play.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.entity.Comment;
import com.play.entity.News;
import com.play.entity.Operation;
import com.play.entity.User;
import com.play.entity.User_info;
import com.play.pojo.EasyComment;
import com.play.repository.CommentRepository;
import com.play.repository.NewsRepository;
import com.play.repository.OperationRepository;
import com.play.repository.UserRepository;
import com.play.util.PageModel;
import com.play.util.Result;

@Service("commentService")
public class CommentService {
	@Autowired
	CommentRepository repository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	NewsRepository newsRepository;
	@Autowired
	OperationRepository oprationRepository;
	/**
	 * 
	* @Title: Add_Comment   
	* @Description: TODO(这里用一句话描述这个方法的作用)   
	* @param @param content		评论内容
	* @param @param uid	    用户id
	* @param @param nid  新闻id   
	* @return void    返回类型   
	* @throws   
	 */
	@Transactional
	public void Add_Comment(String content, int uid, int nid) {
		News news = newsRepository.findById(nid).get();
		news.setHot(news.getHot()+1);
		newsRepository.save(news);
		
		Comment comment = new Comment();
		comment.setNews(newsRepository.findById(nid).get());
		comment.setComment_user(userRepository.findById(uid).get());
		comment.setContent(content);
		comment.setCreatetime(new Date());
		repository.save(comment);
		
		Operation operation = new Operation(userRepository.findById(uid).get(), newsRepository.findById(nid).get(), 2);
		oprationRepository.save(operation);
	}
	
	/**
	 * 
	* @Title: Get_Comment   
	* @Description: TODO(这里用一句话描述这个方法的作用)   
	* @param @param nid
	* @param @return    设定文件   
	* @return Result    返回类型   result。data包含user，content
	* @throws   
	 */
	public PageModel Get_Comment(int nid) {
		List<EasyComment> eList = new ArrayList<EasyComment>();
		List<Comment> cList = repository.findByNews(newsRepository.findById(nid).get());
		for (Comment comment : cList) {
			User_info user_info = comment.getComment_user().getInformation();
			System.out.println(user_info.getNickname()+"------------");
			String content = comment.getContent();
			Date createtime = comment.getCreatetime();
			EasyComment eComment = new EasyComment(user_info, content, createtime);
			eList.add(eComment);
		}
		Collections.sort(eList);
		PageModel pModel = new PageModel(eList);
		return pModel;
	}
}
