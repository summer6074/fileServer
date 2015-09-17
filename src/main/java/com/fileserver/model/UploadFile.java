package com.fileserver.model;

import java.util.Date;

public class UploadFile {

    private Integer  fileid; 
    private String name ;
    private String mapfilename ;
    private String description;
    private String tag ;
    private String uploader;
    private Date uploadtime;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMapfilename() {
		return mapfilename;
	}
	public void setMapfilename(String mapfilename) {
		this.mapfilename = mapfilename;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public Date getUploadtime() {
		return uploadtime;
	}
	public String getUploader() {
		return uploader;
	}
	public void setUploader(String uploader) {
		this.uploader = uploader;
	}
	public Integer getFileid() {
		return fileid;
	} 
    
    
	
}
