package com.fileserver.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

/**
 * 利用jodconverter(基于OpenOffice服务)将word文件(*.doc)转化为html格式，
 * 使用前请检查OpenOffice服务是否已经开启, OpenOffice进程名称：soffice.exe | soffice.bin
 * 
 * @author linshutao
 * */
public class Doc2OtherUtil {

	public static final String OpenOffice_HOME;
	public static final String host_Str;
	public static final String port_Str;

	static {
//		ResourceBundle rb = ResourceBundle
//				.getBundle("com.fileserver.controller.OpenOfficeService");
		OpenOffice_HOME = "11";//rb.getString("OO_HOME");
		host_Str = "127.0.0.1";//rb.getString("oo_host");
		port_Str = "8100";//rb.getString("oo_port");
	}

	Log log = LogFactory.getLog(getClass());
	private static Doc2OtherUtil doc2OtherUtil;

	/**
	 * 获取Doc2HtmlUtil实例
	 * */
	public static synchronized Doc2OtherUtil getDoc2OtherUtilInstance() {
		if (doc2OtherUtil == null) {
			doc2OtherUtil = new Doc2OtherUtil();
		}
		return doc2OtherUtil;
	}

	/**
	 * 转换文件
	 * 
	 * @param fromFileInputStream
	 *            :
	 * */
	public String doc2Html(InputStream fromFileInputStream, File toFileFolder) {
		String soffice_host = host_Str;
		String soffice_port = port_Str;
		log.debug("soffice_host:" + soffice_host + ",soffice_port:"
				+ soffice_port);

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String timesuffix = sdf.format(date);
		String htmFileName = "htmlfile" + timesuffix + ".html";
		String docFileName = "docfile" + timesuffix + ".doc";

		File htmlOutputFile = new File(toFileFolder.toString()
				+ File.separatorChar + htmFileName);
		File docInputFile = new File(toFileFolder.toString()
				+ File.separatorChar + docFileName);
		log.debug("########htmlOutputFile：" + toFileFolder.toString()
				+ File.pathSeparator + htmFileName);
		/**
		 * 由fromFileInputStream构建输入文件
		 * */
		try {
			OutputStream os = new FileOutputStream(docInputFile);
			int bytesRead = 0;
			byte[] buffer = new byte[1024 * 8];
			while ((bytesRead = fromFileInputStream.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}

			os.close();
			fromFileInputStream.close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		OpenOfficeConnection connection = new SocketOpenOfficeConnection(
				soffice_host, Integer.parseInt(soffice_port));
		try {
			connection.connect();
		} catch (ConnectException e) {
			System.err.println("文件转换出错，请检查OpenOffice服务是否启动。");
			log.error(e.getMessage(), e);
		}
		// convert
		DocumentConverter converter = new OpenOfficeDocumentConverter(
				connection);
		converter.convert(docInputFile, htmlOutputFile);
		connection.disconnect();
		
		return htmFileName;
	}
	
	public String doc2PDF(File docInputFile, File finalPdfFile) {
		String soffice_host = host_Str;
		String soffice_port = port_Str;
		log.debug("soffice_host:" + soffice_host + ",soffice_port:"
				+ soffice_port);

		OpenOfficeConnection connection = new SocketOpenOfficeConnection(
				soffice_host, Integer.parseInt(soffice_port));
		try {
			connection.connect();
		} catch (ConnectException e) {
			System.err.println("文件转换出错，请检查OpenOffice服务是否启动。");
			log.error(e.getMessage(), e);
		}
		// convert
		DocumentConverter converter = new OpenOfficeDocumentConverter(
				connection);
		converter.convert(docInputFile, finalPdfFile);
		connection.disconnect();
		
		return finalPdfFile.getName();
	} 
	
	
	
	public static void main(String[] args){
		OpenOfficeConnection connection = new SocketOpenOfficeConnection(
				"127.0.0.1", Integer.parseInt("8100"));
		try {
			connection.connect();
		} catch (ConnectException e) {
			System.err.println("文件转换出错，请检查OpenOffice服务是否启动。");
			
		}
		// convert
		DocumentConverter converter = new OpenOfficeDocumentConverter(
				connection);
		converter.convert(new File("C:\\Users\\tian\\workspace\\fileServer\\src\\main\\webapp\\download\\div_1.doc"),new File( "kxx_jeff1.pdf"));
		connection.disconnect();
		
		
	}
	

}