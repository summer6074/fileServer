package com.fileserver.dao;

import java.util.List;
import java.util.Map;

import com.fileserver.model.UploadFile;

public interface UploadFileDao {
    public List<UploadFile> getFiles(Map<String,Object> param);
    public void addFile(UploadFile file);
    public void deleteFile(UploadFile file);
}