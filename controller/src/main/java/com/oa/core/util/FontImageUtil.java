package com.oa.core.util;

/**
 * @ClassName:FontImageUtil
 * @author:zxd
 * @Date:2019/05/18
 * @Time:上午 10:32
 * @Version V1.0
 * @Explain
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class FontImageUtil {

    public static void main(String[] args) throws Exception {
        createImage("请", new Font("宋体", Font.BOLD, 30), new File(
                "e:/a.png"), 64, 64);
        createImage("请", new Font("黑体", Font.BOLD, 35), new File(
                "e:/a1.png"), 64, 64);
        createImage("请", new Font("黑体", Font.PLAIN, 40), new File(
                "e:/a2.png"), 64, 64);

    }

    /**
     * 根据str,font的样式以及输出文件目录
     * @param str	字符串
     * @param font	字体
     * @param outFile	输出文件位置
     * @param width	宽度
     * @param height	高度
     * @throws Exception
     */
    public static void createImage(String str, Font font, File outFile, Integer width, Integer height) throws Exception {
        // 创建图片
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setClip(0, 0, width, height);
        g.setColor(Color.black);
        // 先用黑色填充整张图片,也就是背景
        g.fillRect(0, 0, width, height);
        // 在换成红色
        g.setColor(Color.red);
        // 设置画笔字体
        g.setFont(font);
        /** 用于获得垂直居中y */
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics(font);
        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        int y = (clip.height - (ascent + descent)) / 2 + ascent;
        // 256 340 0 680
        for (int i = 0; i < 6; i++) {
            // 画出字符串
            g.drawString(str, i * 680, y);
        }
        g.dispose();
        // 输出png图片
        ImageIO.write(image, "png", outFile);
    }
	
}
