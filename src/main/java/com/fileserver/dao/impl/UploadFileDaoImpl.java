package com.fileserver.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fileserver.dao.UploadFileDao;
import com.fileserver.model.UploadFile;

@Component("UploadFileDao")
public class  UploadFileDaoImpl extends SqlSessionDaoSupport implements UploadFileDao{
    private static final String  FILE_DAO_NAMESPACE="com.fileserver.dao.UploadFileDao.";


	public void addFile(UploadFile file) {
		// TODO Auto-generated method stub
		 this.getSqlSession().insert(FILE_DAO_NAMESPACE+"addfile", file);
		
	}

	public void deleteFile(UploadFile file) {
		// TODO Auto-generated method stub
		this.getSqlSession().delete(FILE_DAO_NAMESPACE+"deletefile", file);
		
	}

	public List<UploadFile> getFiles(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return 	this.getSqlSession().selectList(FILE_DAO_NAMESPACE+"getfilebyMap", param);
	}
	
	@Override
	@Autowired
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
	}

}
