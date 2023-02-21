package com.oa.core.helper;

import com.oa.core.util.LogUtil;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zxd
 */
public class StringHelper {
	
    /**
     * 将带标记的字符串转换成Vector
     *
     * @param strSource 要转换的文字
     * @param split     分隔符
     * @return
     */
    public static Vector<String> string2Vector(String strSource, String split) {
        Vector<String> result = new Vector<String>();
        if ((strSource != null) && (strSource.trim().length() > 0)) {
            if ((split == null) || (split.length() == 0)) {
                split = ";,\n\t";
            }
            StringTokenizer st = new StringTokenizer(strSource, split);
            while (st.hasMoreTokens()) {
                result.addElement(st.nextToken().trim());
            }
        }
        return result;
    }

    public static ArrayList<String> string2ArrayList(String strSource, String split) {
        ArrayList<String> result = new ArrayList<>();
        if ((strSource != null) && (strSource.trim().length() > 0)) {
            if ((split == null) || (split.length() == 0)) {
                split = ";,\n\t";
            }
            StringTokenizer st = new StringTokenizer(strSource, split);
            while (st.hasMoreTokens()) {
                result.add(st.nextToken().trim());
            }
        }
        return result;
    }

    public static String vector2String(Vector<String> strSource) {
        String split = ",";
        return vector2String(strSource, split);
    }
    /**
     * 将Vector转换成带标记的字符串
     *
     * @param strSource 要转换的文字
     * @param split     分隔符
     * @return
     */
    public static String vector2String(Vector<String> strSource, String split) {
        if (strSource == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (String string : strSource) {
            result.append(string);
            result.append(split);
        }
        return result.toString();
    }

    public static String list2String(List<String> strSource) {
        String split = ",";
        return list2String(strSource, split);
    }
	
    /**
     * 将ArrayList转换成带标记的字符串
     *
     * @param strSource 要转换的文字
     * @param split     分隔符
     * @return
     */
    public static String list2String(List<String> strSource, String split) {
        if (strSource == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (String string : strSource) {
            result.append(string);
            result.append(split);
        }
        return result.toString();
    }

    /**
     * 判断字符串为空
     *
     * @param str
     * @return
     * @author zhaosj
     */
    public static boolean isEmpty(String str) {
        if (null != str && !"".equals(str.trim()) && !"null".equals(str.trim())) {
            return false;
        }
        return true;
    }

    /**
     * 判断字符串不为空
     *
     * @param str
     * @return
     * @author zhaosj
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 删除末尾分隔符
     *
     * @param str      原字符串
     * @param separate 分隔符
     * @return
     * @author zhaosj
     */
    public static String subEndSeparate(String str, String separate) {
        if (isNotEmpty(str)) {
            if (str.endsWith(separate)) {
                return str.substring(0, str.lastIndexOf(separate));
            } else {
                return str;
            }
        }
        return null;
    }

    /**
     * 如果字符串为空，则返回"",否则返回原值
     *
     * @param str
     * @return
     */
    public static String isNull(String str) {
        if (null == str) {
            return "";
        }
        return str;
    }
    public static Object isNull(Object str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        return str;
    }

    /**
     * 获取中括号中的内容
     *
     * @param content
     * @return List
     */
    public static List<String> getMsg(String content) {
        List<String> msg = new ArrayList<String>();
        String aa = "(\\[[^\\]]*\\])";
        Pattern p = Pattern.compile(aa);
        Matcher m = p.matcher(content);
        while (m.find()) {
            String qq = m.group().substring(1, m.group().length() - 1);
            msg.add(qq);
        }
        return msg;
    }

    public static List<String> getBracket(String content,String attach) {
        List<String> bracket = new ArrayList<String>();
        String aa = "(\\[[^\\]]*\\])";
        Pattern p = Pattern.compile(aa);
        Matcher m = p.matcher(content);
        while (m.find()) {
            String qq = m.group().substring(1, m.group().length() - 1);
            if(attach.equals("{")){
                qq = "{"+qq+"}";
            }else if(attach.equals("<")){
                qq = "<"+qq+">";
            }else if(attach.equals("'")){
                qq = "'"+qq+"'";
            }else if(attach.equals("\"")){
                qq = "\""+qq+"\"";
            }else if(attach.equals("[")){
                qq = "["+qq+"]";
            }
            bracket.add(qq);
        }
        return bracket;
    }

    public static String listToString(List<String> list,String split){
        if(list==null){
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean first = true;

        //第一个前面不拼接","
        for(String string :list) {
            if(first) {
                first=false;
            }else{
                result.append(split);
            }
            result.append(string);
        }
        return result.toString();
    }

    /**
     * 中文转unicode编码
     *
     * @param gbString 转码中文
     * @return unicode编码
     */
    public static String gbEncoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int i = 0; i < utfBytes.length; i++) {
            String hexB = Integer.toHexString(utfBytes[i]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }

    /**
     * unicode编码转中文
     *
     * @param dataStr unicode编码
     * @return 转码中文
     */
    public static String decodeUnicode(final String dataStr) {
        if(dataStr!=null && dataStr!="") {
            int start = 0;
            int end = 0;
            final StringBuffer buffer = new StringBuffer();
            while (start > -1) {
                end = dataStr.indexOf("\\u", start + 2);
                String charStr = "";
                if (end == -1) {
                    charStr = dataStr.substring(start + 2, dataStr.length());
                } else {
                    charStr = dataStr.substring(start + 2, end);
                }
                char letter = (char) Integer.parseInt(charStr, 16);
                buffer.append(new Character(letter).toString());
                start = end;
            }
            return buffer.toString();
        }else{
            return dataStr;
        }
    }
    public static String Space(String type){
        String space = type;
        switch (type) {
            case "k0":
                space="&ensp;";
                break;
            case "k1":
                space="&nbsp;";
                break;
            case "&":
                space="&amp;";
                break;
            case "<":
                space="&lt;";
                break;
            case ">":
                space="&gt;";
                break;
            case "\"":
                space="&quot;";
                break;
            case "'":
                space="&qpos;";
                break;
            default:
        }
        return space;
    }

    public static String get2Num(){
        Random random = new Random();
        int ends = random.nextInt(99);
        return String.format("%02d",ends);
    }

    /**
     * 将Vector转换成带标记的字符串
     *
     * @param strSource 要转换的文字
     * @param split     分隔符
     * @return
     */
    public static String vector2SqlField(Vector<String> strSource, String split) {
        if (strSource == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (String string : strSource) {
            result.append(split);
            result.append(string);
        }
        return result.toString();
    }

    public static String getMString(int num) {
        String s = "";
        switch (num) {
            case 1:
                s = "一";
                break;
            case 2:
                s = "二";
                break;
            case 3:
                s = "三";
                break;
            case 4:
                s = "四";
                break;
            case 5:
                s = "五";
                break;
            case 6:
                s = "六";
                break;
            case 7:
                s = "七";
                break;
            case 8:
                s = "八";
                break;
            case 9:
                s = "九";
                break;
            case 10:
                s = "十";
                break;
            case 11:
                s = "十一";
                break;
            case 12:
                s = "十二";
                break;
            default:
                s = String.valueOf(num);
                break;
        }
        return s;
    }
    @Test
    public void test(){

        LogUtil.sysLog("========================>"+get2Num()+"<======================");
    }
}
