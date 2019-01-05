package com.play.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import com.play.entity.Role;
import com.play.entity.User;
import com.play.entity.User_info;
import com.play.repository.RoleRepository;
import com.play.repository.UserInfoRepository;
import com.play.repository.UserRepository;
import com.play.util.Result;

@Service("userService")
public class UserService {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserInfoRepository userInfoRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Value("${web.image-upload-path}")
	String ImageUploadPath;
	
	@Value("${web.static-path}")
	String BasePath;
	
	@Bean
	public static NoOpPasswordEncoder passwordEncoder() {
	  return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}
	
	public User getUser(String username){
        return userRepository.findByUsername(username);
    }
	
	/**
	 * 
	* @Title: getUserInfo   
	* @Description: 获取userinfo  
	* @param     通过user   
	* @return void    返回类型   
	* @throws   
	 */

	public User_info getUserInfo(String nickname) {
		return userInfoRepository.findByNickname(nickname);
	}
	
	
	public User findByName(String username) {
		return userRepository.findByUsername(username);
	}
	/**
	 * 
	* @Title: register   
	* @Description: 注册用户  
	* @param @param user
	* @param @param user_info
	* @return Result    返回类型   
	* @throws   
	 */
	@Transactional
	public void register(User user, List<String> userRoleNames, User_info user_info) {
		 // 注册身份
        List<Role> userRole = new ArrayList<>();

        for(String userRoleName : userRoleNames){
            userRole.add(roleRepository.findByName(userRoleName));
        }

        if(user.getPassword() == null || user.getUsername() == null)
            return;
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        user.setRoles(userRole);
        
     // 关联信息表
        userInfoRepository.save(user_info);
        user.setInformation(user_info);
        userRepository.save(user);
	}
	
	public Result setHeadImage(User_info user_info,MultipartFile image) {
		Result result = new Result();
		//upload image to static src
		if(!image.isEmpty()) {
			if (image.getContentType().contains("image")) {
				String temp= "images" + File.separator  + "upload" + File.separator;
				// 获取图片的文件名
				String fileName = image.getOriginalFilename();
				// 获取图片的扩展名
				String extensionName = StringUtils.substringAfter(fileName, ".");
				// 新的图片文件名 = 获取时间戳+"."图片扩展名
				String newFileName = String.valueOf(System.currentTimeMillis()) + "." + extensionName;
				// 文件路径
				String filePath = BasePath.concat(ImageUploadPath).concat("/HeadImage/");
				// 数据库保存的目录
				String datdDirectory = temp;
				
				File dest = new File(filePath, newFileName);
				if (!dest.getParentFile().exists()) {
					  dest.getParentFile().mkdirs();
				}
				// 判断是否有旧头像，如果有就先删除旧头像，再上传
				  if (StringUtils.isNotBlank(user_info.getHeadimage())) {
					  String oldFilePath = BasePath.concat(ImageUploadPath).concat(user_info.getHeadimage());
					  File oldFile = new File(oldFilePath);
					  if (oldFile.exists()) {
					  oldFile.delete();
					  }
				  }
				// 上传到指定目录
				  try {
					image.transferTo(dest);
				} catch (IllegalStateException | IOException e) {
					// TODO Auto-generated catch block
					result.setStatus(false);
				}
				// 将反斜杠转换为正斜杠
				  String data = datdDirectory.replaceAll("\\\\", "/") + newFileName;
				  
				//set user_info.headimage = string (file) image.path
				  user_info.setHeadimage(newFileName);
				  userInfoRepository.save(user_info);
				  Map<String, Object> map = new HashMap<String, Object>();
				  map.put("filename", newFileName);
				  result.setData(map);
			}
		}
		result.setStatus(true);
		return result;
	}
	public String change_psw (User user, String psw) {
		user.setPassword(psw);
		userRepository.save(user);
		return "success";
	}
	public String change_userinfo(User user,String nickname,String detail,String headimage,String interest) {
		User_info user_info = user.getInformation();
		user_info.setNickname(nickname);
		user_info.setDetail(detail);
		user_info.setHeadimage(headimage);
		user_info.setInterest(interest);
		userInfoRepository.save(user_info);
		return "success";
	}
}
