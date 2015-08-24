package com.fileserver.utils;


import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: xxx
 * Date: 13-7-12
 * Time: ÉÏÎç8:42
 * To change this template use File | Settings | File Templates.
 */
public class CommUtil {
    public static final String OpenOffice_HOME;
    public static final String host_Str;
    public static final String port_Str;

    static {
        ResourceBundle rb = ResourceBundle.getBundle("com.fileserver.controller.OpenOfficeService");
        OpenOffice_HOME = rb.getString("OO_HOME");
        host_Str = rb.getString("oo_host");
        port_Str = rb.getString("oo_port");
    }

    public static int strToInt(String str){
        int i =0;
        if(str!=null&&!"".equals(str)){
            i = Integer.parseInt(str);
        }
        return i;
    }
    public static double strToDouble(String str){
        double d =0.0;
        if(str!=null&&!"".equals(str)){
            d = Double.parseDouble(str);
        }
        return d;
    }
    public static void main(String[] args ){
    	
    	System.out.println("Hehe");
    	
    }
}
