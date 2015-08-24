package com.fileserver.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.fileserver.utils.Doc2OtherUtil;


@RestController
public class FileOperationController {

	
	@RequestMapping(value = "/preview", method = RequestMethod.GET, produces = { "text/javascript;charset=UTF-8" })
	@ResponseBody
	public ModelAndView preview(HttpServletRequest request) {

		ModelAndView view =new ModelAndView("preview");
		String originPath = request.getParameter("fileName");
		String finalPath = "download/"+originPath;
		if(originPath.endsWith("doc")){		
			String path1 = request.getSession().getServletContext().getRealPath("/download") +File.separator+originPath;//get path
			
			String path2 = path1+".pdf";
			File file = new File(path1);
			if(file.exists()){
				File file1 = new File(path2);
				if(!file1.exists()){
				
				    Doc2OtherUtil.getDoc2OtherUtilInstance().doc2PDF(file, file1);
				    finalPath= finalPath+".pdf";
				}
				 
			}
		}
		String filePath = finalPath;
		view.addObject("filePath", filePath);
		return view;

	}
	
	@RequestMapping("/upload_file")
    public String upload(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
               // 这里我用到了jar包
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                MultipartFile file = multiRequest.getFile((String) iter.next());
                
                
                if (file != null) {
                    String fileName = file.getOriginalFilename();
 
                    String path1 = request.getSession().getServletContext().getRealPath("/download") +File.separator;
 
                    //  下面的加的日期是为了防止上传的名字一样
                    String path = path1
                            + new SimpleDateFormat("yyyyMMddHHmmss")
                                    .format(new Date()) + fileName;
                    
                    savetoFileData(fileName,path);
 
                    File localFile = new File(path);
                    if(!localFile.getParentFile().exists()){
                    	
                    	localFile.getParentFile().mkdirs();
                    }                    

                    file.transferTo(localFile);
                }
 
            }
 
        }
        return "uploadSuccess";
 
    }
  
    private void savetoFileData(String fileName, String path) {
		// TODO Auto-generated method stub
		
	}

	@RequestMapping("/download")
    public String download(String fileName, HttpServletRequest request,
            HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName="
                + fileName);
        try {
            String path = Thread.currentThread().getContextClassLoader()
                    .getResource("").getPath()
                    + "download";//这个download目录为啥建立在classes下的
            InputStream inputStream = new FileInputStream(new File(path
                    + File.separator + fileName));
 
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
 
             // 这里主要关闭。
            os.close();
 
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
            //  返回值要注意，要不然就出现下面这句错误！
            //java+getOutputStream() has already been called for this response
        return null;
    }
	
	protected String getID(HttpServletRequest request){
		
	
	  return "11443";
	}
	}