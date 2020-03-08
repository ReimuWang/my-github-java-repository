package org.reimuwang.commonability.image;

import org.apache.commons.io.FilenameUtils;
import org.reimuwang.commonability.file.FileIOUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
        String extension = FilenameUtils.getExtension(file.getName());
        if (!IMAGE_TYPE_SET.contains(extension.toLowerCase())) {
            return false;
        }
        return true;
    }

    public static void downloadImage(String urlPath, String downloadPath) throws IOException {
        HttpURLConnection conn = (HttpURLConnection)new URL(urlPath).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);
        byte[] data = FileIOUtils.readInputStream(conn.getInputStream());
        try (FileOutputStream outStream = new FileOutputStream(new File(downloadPath))) {
            outStream.write(data);
        }
    }
}
