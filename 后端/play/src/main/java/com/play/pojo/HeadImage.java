package com.play.pojo;


import org.springframework.web.multipart.MultipartFile;

public class HeadImage {
	MultipartFile file;

	public MultipartFile getHeadimage() {
		return file;
	}

	public void setHeadimage(MultipartFile headimage) {
		this.file = headimage;
	}
	
}
