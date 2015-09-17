package com.fileserver.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.fileserver.dao.UploadFileDao;
import com.fileserver.model.UploadFile;

public class testDatabase {

	
	public static void main(String[] args){
		
		 ApplicationContext ctx=null;
	        ctx=new FileSystemXmlApplicationContext("src/main/resources/config/spring/ApplicationContext-dao.xml");	       
	        System.out.println("=================Main: Test User part begins!!!==================");
	        
	        UploadFileDao dao = (UploadFileDao) ctx.getBean("UploadFileDao");
	        UploadFile f= new UploadFile();
	        f.setDescription("a");
	        f.setMapfilename("b");
	        f.setName("c");
	        f.setTag("d");
	        f.setUploader("f");
	        dao.addFile(f);
		
	}
}
