package com.fileserver.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.fileserver.dao.UploadFileDao;
import com.fileserver.model.UploadFile;
import com.fileserver.utils.CnToSpell;
import com.fileserver.utils.Doc2OtherUtil;


@RestController
public class FileOperationController {

	@Autowired
	private UploadFileDao uploadFileDao;
	
	@RequestMapping(value = "/preview", method = RequestMethod.GET, produces = { "text/javascript;charset=UTF-8" })
	@ResponseBody
	public ModelAndView preview(HttpServletRequest request) {

		ModelAndView view =new ModelAndView("preview");
		String fileId = request.getParameter("fileId");
		if(StringUtils.isEmpty(fileId)){
			throw new RuntimeException("no id contains");
		}
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("fileid", fileId);
		List<UploadFile> files = this.uploadFileDao.getFiles(param);
		if(files==null || files.isEmpty()){
			throw new RuntimeException("no file found");
		}
		
		String originPath = files.get(0).getMapfilename();
		
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
    public String upload(@RequestParam("desc") String desc,@RequestParam("tag") String tag,HttpServletRequest request,
            HttpServletResponse response) throws IOException {
               // 这里我用到了jar包
		
		HttpSession session = request.getSession();
		String userName = (String)session.getAttribute("userName");
		String userId = (String)session.getAttribute("userId");
		if(userName == null){
			userName="Unknown";
			
		}
		if(userId == null){
			userId="Unknown";
			
		}
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                MultipartFile file = multiRequest.getFile((String) iter.next());
                
                
                if (file != null) {
                    String fileName = file.getOriginalFilename();
 
                    String path1 = request.getSession().getServletContext().getRealPath("/download/"+userId) +File.separator;
                    //  下面的加的日期是为了防止上传的名字一样
                    CnToSpell cn= new CnToSpell();            
                    String newFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + fileName;
                    newFileName= cn.getPinYin(newFileName);
                    String path = path1
                            + newFileName;
                    
                    savetoFileData(fileName,newFileName,desc,tag,userName);//uploader?
 
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
  
    private void savetoFileData(String fileName, String path, String desc, String tag,String uploader) {
		// TODO Auto-generated method stub
    	UploadFile file = new UploadFile();
    	file.setDescription(desc);
    	file.setMapfilename(path);
    	file.setName(fileName);
    	file.setTag(tag);
    	file.setUploader(uploader);
		this.uploadFileDao.addFile(file);
    	
	}
    
    

    

	
	@RequestMapping("/downloadFile")
    public String download(String fileId, HttpServletRequest request,
            HttpServletResponse response) throws UnsupportedEncodingException {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("fileid", fileId);
		List<UploadFile> files = this.uploadFileDao.getFiles(param);
		if(files==null || files.isEmpty()){
			return null ;
		}
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName="
                + URLEncoder.encode(files.get(0).getName(),"UTF-8"));
        try {
            String path = request.getSession().getServletContext().getRealPath("/download");//这个download目录为啥建立在classes下的
            InputStream inputStream = new FileInputStream(new File(path
                    + File.separator + files.get(0).getMapfilename()));
 
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
	
	}