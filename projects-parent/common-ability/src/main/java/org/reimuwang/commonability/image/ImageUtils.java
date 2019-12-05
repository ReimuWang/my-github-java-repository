package org.reimuwang.commonability.image;

import java.awt.image.BufferedImage;

public class ImageUtils {

    /**
     * 将原图片以长宽中较长的一边为基础调整为正方形
     * 另一边保持居中
     */
    public static BufferedImage toSquare(BufferedImage sourceImg) {
        int sw = sourceImg.getWidth();
        int sh = sourceImg.getHeight();
        if (sw == sh) return sourceImg;
        boolean wgth = false;    // 原图片宽是否大于高
        int rl = sh;
        if (sw > sh) {
            wgth = true;
            rl = sw;
        }
        BufferedImage result = new BufferedImage(rl, rl, sourceImg.getType());
        for (int y = 0; y < sh; y++) {
            for (int x = 0; x < sw; x++) {
                int rx = x, ry = y;
                if (wgth) ry += (sw - sh) / 2;
                else rx += (sh - sw) / 2;
                result.setRGB(rx, ry, sourceImg.getRGB(x, y));
            }
        }
        return result;
    }

}
