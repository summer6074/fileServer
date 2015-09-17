package com.fileserver.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;



/**
 * Created with IntelliJ IDEA.
 * User: xxx
 * Date: 13-7-12
 * Time: ����3:52
 * To change this template use File | Settings | File Templates.
 */
public class CnToSpell {

    public  String getPinYin(String src) {
        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        // ���ú���ƴ������ĸ�ʽ
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++) {
                // �ж��Ƿ�Ϊ�����ַ�
                if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);// �����ֵļ���ȫƴ���浽t2������
                    t4 += t2[0];// ȡ���ú���ȫƴ�ĵ�һ�ֶ��������ӵ��ַ���t4��
                } else {
                    // ������Ǻ����ַ���ֱ��ȡ���ַ������ӵ��ַ���t4��
                    t4 += Character.toString(t1[i]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        if(t4.indexOf("��")!=-1){
            t4 = t4.replace("��","[");
        }
        if(t4.indexOf("��")!=-1){
            t4 = t4.replace("��","]");
        }
        if(t4.indexOf("��")!=-1){
            t4 = t4.replace("��","(");
        }
        if(t4.indexOf("��")!=-1){
            t4 = t4.replace("��",")");
        }
        return t4;
    }


    public  String getPinYinHeadChar(String str) {
        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            // ��ȡ���ֵ�����ĸ
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }


    public  String getCnASCII(String cnStr) {
        StringBuffer strBuf = new StringBuffer();
        // ���ַ���ת�����ֽ�����
        byte[] bGBK = cnStr.getBytes();
        for (int i = 0; i < bGBK.length; i++) {
            strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
        }
        return strBuf.toString();
    }

    public static void main(String[] args) {
        CnToSpell cnToSpell = new CnToSpell();
        String cnStr = "hh�л����񹲺͹���A-C��(12)_12345.pdf";
        System.out.println(cnToSpell.getPinYin(cnStr));
        System.out.println(cnToSpell.getPinYinHeadChar(cnStr));
        System.out.println(cnToSpell.getCnASCII(cnStr));
    }
}
