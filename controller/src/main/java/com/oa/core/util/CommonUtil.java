package com.oa.core.util;

import com.oa.core.helper.DateHelper;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @ClassName:CommonUtil
 * @author:zxd
 * @Date:2019/04/29
 * @Time:下午 4:49
 * @Version V1.0
 * @Explain 公共方法
 */
public class CommonUtil {

    public static int getRandomNumber(int num){
        //int n = (int)(1+Math.random()*(num-1+1));
        int n = DateHelper.getWeekOfThisDate();
        return n;
    }


    @Test
    public void test(){
        System.out.println(getRandomNumber(10));
    }


    /**
     * @method: FileLength
     * @param: src 文件绝对路径
     * @return:
     * @author: zxd
     * @date: 2019/07/04
     * @description: 根据路径获取文件大小
     */
    public static long FileLength(String src) {
        FileChannel fc = null;
        long size = 0L;
        try {
            java.io.File f = new File(src);
            if (f.exists() && f.isFile()) {
                FileInputStream fis = new FileInputStream(f);
                fc = fis.getChannel();
                LogUtil.sysLog(fc.size());
                size = fc.size();
            } else {
                LogUtil.sysLog("未找到文件");
            }
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
        } catch (IOException e) {
            e.getLocalizedMessage();
        } finally {
            if (null != fc) {
                try {
                    fc.close();
                } catch (IOException e) {
                    LogUtil.sysLog(e);
                }
            }
        }
        return size;
    }
}
