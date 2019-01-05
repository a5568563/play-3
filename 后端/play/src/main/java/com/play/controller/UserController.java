package com.play.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.play.entity.User;
import com.play.entity.User_info;
import com.play.pojo.Getinfo;
import com.play.pojo.HeadImage;
import com.play.pojo.UserRegisteration;
import com.play.service.UserService;
import com.play.util.Result;

@RestController
@CrossOrigin
public class UserController {
	@Autowired
    private UserService userService;
	
	@Qualifier("getTokenStore")
    @Autowired
    private TokenStore tokenStore;
	
	@PostMapping("/register")
    private String userRegister(@RequestBody UserRegisteration userRegisteration) {
		System.out.println(userRegisteration.toString());
        if (userService.getUser(userRegisteration.getUsername()) != null)
            return "用户名已存在";

        User_info userInfo = new User_info();
        userInfo.setPhone(userRegisteration.getTelephone());
        userInfo.setNickname(userRegisteration.getNickname());
        userInfo.setInterest(userRegisteration.getInterest());
        
        userService.register(new User(
                userRegisteration.getUsername(),
                userRegisteration.getPassword()),
        		Arrays.asList("USER"),
                userInfo
        );

        return "success";
    }
	
	@PostMapping("/user/getNick")
	private Result getNickname() {
		User user = userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
		
		User_info user_info = user.getInformation();
		
		Result result = new Result();
		
		Map<String, Object>data = new HashMap<String, Object>();
		
		data.put("nickname", user_info.getNickname());
		
		result.setData(data);
		
		result.setStatus(true);
		
		return result;
	}
	
	@PostMapping("/user/getInfo")
	public Result getUserInfo() {
		User user = userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
		
		User_info user_info = user.getInformation();
		
		Result result = new Result();
		
		Map<String, Object>data = new HashMap<String, Object>();
		
		Getinfo getinfo = new Getinfo();
		getinfo.setDetail(user_info.getDetail());
		getinfo.setNickname(user_info.getNickname());
		getinfo.setHeadimage(user_info.getHeadimage());
		getinfo.setInterest(user_info.getInterest());
		data.put("info", getinfo);
		result.setData(data);
		result.setStatus(true);
		
		return result;
	}
	
	
	@PostMapping("/user/setHeadImage")
	public Result set_HeadImage (@RequestParam(value="file") MultipartFile file) {
		
		User user = userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
		
		User_info user_info = user.getInformation();
		
		return userService.setHeadImage(user_info, file);
	}
	
	@GetMapping("/user/getSimpleByUsername")
	public User_info getSimpleByUsername(@RequestParam (value = "username") String username) {
		User user = userService.getUser(username);
		if (user == null) {
			return new User_info();
		} else {
			return user.getInformation();
		}
	}
	
	@GetMapping("/user/getHeadImage")
	public String get_HeadImage () {
		
		User user = userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
		
		User_info user_info = user.getInformation();
		
		return user_info.getHeadimage();
	}
	
	@GetMapping("/user/valid_psw")
	public String valid_psw(@RequestParam (value = "password") String password){
		User user = userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
		if (password.equals(user.getPassword())){
			return "success";
		} else {
			return "failure";
		}
	}
	
	@GetMapping("/user/change_psw")
	public String change_psw(@RequestParam (value = "password") String password) {
		User user = userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
		return userService.change_psw(user, password);
	}
	
	@GetMapping("/user/change_userinfo")
	public String change_userinfo(@RequestParam String nickname, String detail, String headimage, String interest) {
		User user = userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
		return userService.change_userinfo(user, nickname, detail, headimage, interest);
	}
}
