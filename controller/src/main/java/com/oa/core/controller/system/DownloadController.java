package com.oa.core.controller.system;

/**
 * @ClassName:DownloadController
 * @author:zxd
 * @Date:2019/07/04
 * @Time:上午 10:08
 * @Version V1.0
 * @Explain
 */
import com.alibaba.dubbo.common.utils.StringUtils;
import com.oa.core.helper.StringHelper;
import com.oa.core.util.CommonUtil;
import com.oa.core.util.ConfParseUtil;
import com.oa.core.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.Vector;

/**
 * 文件下载共通实现
 *
 */
@Controller
@RequestMapping("/download")
public class DownloadController {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadController.class);

    /**
     * 文件下载方法实现
     *
     * @param response
     * @param fileUrl    待下载文件路径
     */
    @RequestMapping("/download")
    public void download(HttpServletResponse response, String fileUrl) {
        Vector<String> vector = StringHelper.string2Vector(fileUrl, "/");
        String originName = vector.lastElement();
        // 参数不能为空，否则不支持下载
        if (StringUtils.isEmpty(fileUrl) || StringUtils.isEmpty(originName) || response == null) {
            return;
        }
        ConfParseUtil cp = new ConfParseUtil();
        String catPath = cp.getProperty("upload_file");
        fileUrl =  catPath+fileUrl;

        // 执行下载
        ServletOutputStream sos = null;
        BufferedInputStream bis = null;
        InputStream inputStream = null;
        try {
            LOGGER.info("执行下载开始：fileUrl=" + fileUrl + ";originName=" + originName);
            // 清空response
            response.reset();
            // 设置响应头
            response.setContentType("application/x-msdownload");

            // 重命名为原文件名称
            // 解决中文名称乱码
            originName = new String(originName.getBytes("gbk"), "iso-8859-1");
            response.addHeader("Content-Disposition", "attachment; filename=" + originName);
            response.addHeader("Content-Length", String.valueOf(CommonUtil.FileLength(fileUrl)));
            // 获取输出流
            sos = response.getOutputStream();

            // 获取网络路径连接
            bis = new BufferedInputStream(new FileInputStream(fileUrl));

            int i;
            // 设置下载缓冲
            /*byte[] buffer = new byte[1024];*/
            while ((i = bis.read()) != -1) {
                sos.write(i);
            }

            sos.flush();

            LOGGER.info("执行下载正常结束！");

        } catch (UnsupportedEncodingException e) {
            printLog(fileUrl, originName);
            e.printStackTrace();

        } catch (ProtocolException e) {
            printLog(fileUrl, originName);
            e.printStackTrace();

        } catch (MalformedURLException e) {
            printLog(fileUrl, originName);
            e.printStackTrace();

        } catch (IOException e) {
            printLog(fileUrl, originName);
            e.printStackTrace();
            // 关闭流
        } finally {
            closeStream(sos, bis, inputStream);
        }
    }

    /**
     * 输出日志
     *
     * @param fileUrl
     * @param originName
     */
    private void printLog(String fileUrl, String originName) {
        LOGGER.info("执行下载失败：fileUrl=" + fileUrl + ";originName=" + originName);
    }

    /**
     * 关闭流
     *
     * @param sos
     * @param bis
     * @param inputStream
     */
    private void closeStream(ServletOutputStream sos, BufferedInputStream bis
            , InputStream inputStream) {
        try {
            // 释放资源
            if (sos != null) {
                sos.close();
            }
            if (bis != null) {
                bis.close();
            }
            if (inputStream != null) {

                inputStream.close();
            }
        } catch (IOException ex) {
            LOGGER.info("断开连接出错！");
            ex.printStackTrace();
        }
    }
	
}