package org.reimuwang.commonability.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ImageUtils {

    private static final Set<String> IMAGE_TYPE_SET;

    static {
        IMAGE_TYPE_SET = new HashSet<>();
        IMAGE_TYPE_SET.add("jpg");
        IMAGE_TYPE_SET.add("jpeg");
        IMAGE_TYPE_SET.add("png");
    }

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

    public static boolean checkIfUnhideImageFile(File file) {
        if (null == file || !file.exists() || !file.isFile() || file.isHidden()) {
            return false;
        }
        String[] nameArray = file.getName().split("\\.");
        if (nameArray.length != 2 || !IMAGE_TYPE_SET.contains(nameArray[1].toLowerCase())) {
            return false;
        }
        return true;
    }
}
