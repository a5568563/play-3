package com.play.util;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class FileOperation {
	  /**
	   *  生成文件
	   *  @param fileName
	   */
	  public static boolean createFile(File fileName){
	    if(!fileName.exists()){  
	      try {
	        fileName.createNewFile();
	      } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      }
	    }
	    return true;
	  }
	  
	  /**
	   * @description 读文件
	   * @throws IOException 
	   */
	  public static String readTxtFile(File fileName) throws IOException{
	    String result = "";
	    InputStreamReader reader = null;
	    BufferedReader bufferedReader = null;
	    reader = new InputStreamReader(new FileInputStream(fileName), "utf-8");
	    bufferedReader = new BufferedReader(reader);
	    
	    String read = null;
	    while((read = bufferedReader.readLine()) != null){
	      result = result + read + "\n";
	    }
	    
	    if(bufferedReader != null){
	      bufferedReader.close();
	    }
	    
	    if(reader != null){
	        reader.close();
	    }
	    return result;
	  }
	  
	  /**
	   * @description 写文件
	   * @param args
	   * @throws UnsupportedEncodingException 
	   * @throws IOException
	   */
	  public static boolean writeTxtFile(String content,File fileName) throws UnsupportedEncodingException, IOException{
	    FileOutputStream o = null;
	    o = new FileOutputStream(fileName);
	    o.write(content.getBytes("UTF-8"));
	    o.close();
	    return true; 
	  }
}