package com.fileserver.utils;




import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;



/**
 * Created with IntelliJ IDEA.
 * Date: 13-7-11
 * Time: ����10:46
 * To change this template use File | Settings | File Templates.
 */
public class FlashUtil {

    public static  void main(String[] args){
        String outPath = FlashUtil.beginConvert("","����.pdf");
        System.out.println("����swf�ļ�:" + outPath);
//        boolean outPath = new FlashUtil().isExistFlash("123.pdf");
//        System.out.println("�Ƿ����swf�ļ�:" + outPath);
    }
    private static final String DOC = ".doc";
    private static final String DOCX = ".docx";
    private static final String XLS = ".xls";
    private static final String XLSX = ".xlsx";
    private static final String PDF = ".pdf";
    private static final String SWF = ".swf";
    private static final String TOOL = "pdf2swf.exe";

    /**
     * ��ڷ���-ͨ���˷���ת���ļ���swf��ʽ
     * @param filePath �ϴ��ļ������ļ��еľ���·��
     * @param fileName	�ļ�����
     * @return			����swf�ļ���
     */
    public static String beginConvert(String filePath,String fileName) {
        String outFile = "";
        String fileNameOnly = "";
        String fileExt = "";
        if (null != fileName && fileName.indexOf(".") > 0) {
            int index = fileName.indexOf(".");
            fileNameOnly = fileName.substring(0, index);
            fileExt = fileName.substring(index).toLowerCase();
        }
        String inputFile = filePath + File.separator + fileName;
        String outputFile = "";
        //�����flash�ļ���ֱ����ʾ
        if(fileExt.equals(SWF)){
            outFile = fileName;
        }else {
            //��Ҫ��������ĺ���תƴ��
            fileNameOnly = new CnToSpell().getPinYin(fileNameOnly);
            //������ڶ�Ӧ��flash�ļ�
            boolean  isExistFlash = isExistFlash(filePath,fileNameOnly);
            if(isExistFlash){
                outFile = fileNameOnly + SWF;
            }else {
                //�����office�ĵ�����תΪpdf�ļ�
                if (fileExt.equals(DOC) || fileExt.equals(DOCX) || fileExt.equals(XLS)
                        || fileExt.equals(XLSX)) {
                    outputFile = filePath + File.separator + fileNameOnly + PDF;
                    File pdfFile = new File(outputFile);
                    if(!pdfFile.exists()){//�ж�pdf�ļ��Ƿ��Ѿ�����
                        office2PDF(inputFile, outputFile);
                    }
                    inputFile = outputFile;
                    fileExt = PDF;
                }
                if (fileExt.equals(PDF)) {
                    outputFile = filePath + File.separator + fileNameOnly + SWF;
                    outputFile = outputFile.replace("\\","/");
                    File swfFile = new File(outputFile);
                    if(!swfFile.exists()){//�ж�swf�ļ��Ƿ��Ѿ�����
                        File parentFolder = swfFile.getParentFile();
                        if(parentFolder!=null&&!parentFolder.exists()){
                            parentFolder.mkdirs();
                        }
                        String toolFile = null;
                        if(filePath.indexOf("flexpaper")==-1){
                            toolFile = filePath + File.separator +"flexpaper"+ File.separator + TOOL;
                        }else{
                            toolFile = filePath + File.separator + TOOL;
                        }
                        convertPdf2Swf(inputFile, outputFile, toolFile);
                    }
                    outFile = fileNameOnly + SWF;
                }
            }
        }
        return outFile;
    }

    /**
     * ��pdf�ļ�ת����swf�ļ�
     * @param sourceFile pdf�ļ�����·��
     * @param outFile	 swf�ļ�����·��
     * @param toolFile	 ת�����߾���·��
     */
    private static void convertPdf2Swf(String sourceFile, String outFile,
                                String toolFile) {
        String command = toolFile + " \"" + sourceFile + "\" -o  \"" + outFile
                + "\" -s flashversion=9 ";
        try {
            Process process = Runtime.getRuntime().exec(command);
            System.out.println(loadStream(process.getInputStream()));
            System.err.println(loadStream(process.getErrorStream()));
            System.out.println(loadStream(process.getInputStream()));
            System.out.println("###--Msg: swf ת���ɹ�");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ����ļ������Ƿ��Ѵ��ڶ�Ӧ��flash�ļ�
     * @return
     */
        private static boolean isExistFlash(String filePath,String fileNameOnly){
            String fileName = fileNameOnly.substring(fileNameOnly.lastIndexOf("/")+1);
            String newFilePath = fileNameOnly.substring(0 ,fileNameOnly.lastIndexOf("/")+1);
            File file = new File(filePath + File.separator+newFilePath);
            if(!file.exists()){//�ж��Ƿ��Ѿ��������ļ��У�Ȼ����ȥ�ж��ļ����Ƿ���ڶ�Ӧ��flash�ļ�
                return false;
            }
            File[] files = file.listFiles();
            for(int j=0;j<files.length;j++){
                if(files[j].isFile()){
                    String filesName = files[j].getName();
                    if(filesName.indexOf(".")!=-1){
                        if(SWF.equals(filesName.substring(filesName.lastIndexOf(".")).toLowerCase())){
                                if(fileName.equals(filesName.substring(0,filesName.lastIndexOf(".")))){
                                    return true;
                                }
                        }
                    }
                }
            }
            return false;
        }

    /**
     * office�ĵ�תpdf�ļ�
     * @param sourceFile	office�ĵ�����·��
     * @param destFile		pdf�ļ�����·��
     * @return
     */
    private static int office2PDF(String sourceFile, String destFile) {
        String OpenOffice_HOME = CommUtil.OpenOffice_HOME;
        String host_Str = CommUtil.host_Str;
        String port_Str = CommUtil.port_Str;
        try {
            File inputFile = new File(sourceFile);
            if (!inputFile.exists()) {
                return -1; // �Ҳ���Դ�ļ�
            }
            // ���Ŀ��·��������, ���½���·��
            File outputFile = new File(destFile);
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs();
            }
            // ����OpenOffice�ķ���
            String command = OpenOffice_HOME
                    + "\\program\\soffice.exe -headless -accept=\"socket,host="
                    + host_Str + ",port=" + port_Str + ";urp;\"";
            System.out.println("###\n" + command);
            Process pro = Runtime.getRuntime().exec(command);
            // ����openoffice����
            OpenOfficeConnection connection = new SocketOpenOfficeConnection(
                    host_Str, Integer.parseInt(port_Str));
            connection.connect();
            // ת��
            DocumentConverter converter = new OpenOfficeDocumentConverter(
                    connection);
            converter.convert(inputFile, outputFile);

            // �ر����Ӻͷ���
            connection.disconnect();
            pro.destroy();

            return 0;
        } catch (FileNotFoundException e) {
            System.out.println("�ļ�δ�ҵ���");
            e.printStackTrace();
            return -1;
        } catch (ConnectException e) {
            System.out.println("OpenOffice��������쳣��");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    static String loadStream(InputStream in) throws IOException{
        int ptr = 0;
        in = new BufferedInputStream(in);
        StringBuffer buffer = new StringBuffer();

        while ((ptr=in.read())!= -1){
            buffer.append((char)ptr);
        }
        return buffer.toString();
    }
}

