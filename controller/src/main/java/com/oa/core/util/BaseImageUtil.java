package com.oa.core.util;

/**
 * @ClassName:BaseImage
 * @author:zxd
 * @Date:2019/06/21
 * @Time:下午 5:09
 * @Version V1.0
 * @Explain
 */

import org.json.JSONObject;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;


public class BaseImageUtil {

    /**
     * @param imgStr base64编码字符串
     * @param path   图片路径-具体到文件
     * @return
     * @Description: 将base64编码字符串转换为图片
     * @Author:
     * @CreateTime:
     */
    public static boolean generateImage(String imgStr, String path) {
        if(imgStr == null){
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try{
            //解密
            byte[] b = decoder.decodeBuffer(imgStr);
            //处理数据
            for (int i = 0;i<b.length;++i){
                if(b[i]<0){
                    b[i]+=256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * @Description: 根据图片地址转换为base64编码字符串
     * @Author:
     * @CreateTime:
     * @return
     */
    public static String getImageStr(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        String strImg = encoder.encode(data);
        strImg = strImg.replaceAll("\r\n","");
        return strImg;
    }

    public static byte[] getImageData(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void main(String[] args) {
        String strImg = getImageStr("C:\\Users\\Administrator\\Desktop\\微信图片_20190622085151.jpg");
        //System.out.println(strImg);
        JSONObject json = new JSONObject();
        json.put("img",strImg);
        System.out.println(json);
        generateImage(strImg, "C:\\Users\\Administrator\\Desktop\\86619-107.jpg");
    }
}
