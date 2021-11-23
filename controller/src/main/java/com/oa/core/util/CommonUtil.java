package com.oa.core.util;

import com.oa.core.helper.DateHelper;
import org.junit.Test;

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
}
