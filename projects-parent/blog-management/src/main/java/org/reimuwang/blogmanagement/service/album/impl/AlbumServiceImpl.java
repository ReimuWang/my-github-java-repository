package org.reimuwang.blogmanagement.service.album.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.reimuwang.blogmanagement.service.album.AlbumService;
import org.reimuwang.blogmanagement.utils.AlbumJsonDataHandler;
import org.reimuwang.commonability.alibaba.oss.AlibabaOssHandler;
import org.reimuwang.commonability.image.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class AlbumServiceImpl implements AlbumService {

    @Autowired
    private AlibabaOssHandler alibabaOssHandler;

    @Value("${reimuwang.alibaba.oss.photoDir}")
    private String ossPhotoDir;

    @Value("${reimuwang.alibaba.oss.minPhotoDir}")
    private String ossMinPhotoDir;

    @Value("${reimuwang.album.jsonpath}")
    private String jsonOutPutPath;

    @Value("${reimuwang.album.sourceDir}")
    private String localSourceDir;

    @Value("${reimuwang.album.photoDir}")
    private String localPhotoDir;

    @Value("${reimuwang.album.minPhotoDir}")
    private String localMinPhotoDir;

    @Value("${reimuwang.album.photoQuality}")
    private Float photoQuality;

    @Value("${reimuwang.album.minPhotoWidth}")
    private Integer minPhotoWidth;

    @Override
    public JSONObject jsonGenerate(String logMark) throws IOException {
        List<String> nameList = alibabaOssHandler.listObjects(this.ossPhotoDir);
        if (nameList.isEmpty()) {
            log.info(logMark + "oss目录" + this.ossPhotoDir + "中没有图片");
            return new JSONObject();
        }
        AlbumJsonDataHandler albumJsonDataHandler = new AlbumJsonDataHandler();
        nameList.forEach(name -> albumJsonDataHandler.add(name));
        JSONObject result = albumJsonDataHandler.createJson(this.jsonOutPutPath);
        log.info(logMark + "成功写入json文件=" + this.jsonOutPutPath);
        return result;
    }

    @Override
    public boolean imageCompressAndCopy(Float photoQualityParam, String logMark) throws IOException {
        if (null == photoQualityParam) {
            photoQualityParam = this.photoQuality;
        }
        File sourceDir = new File(this.localSourceDir);
        File localPhotoDir = new File(this.localPhotoDir);
        if (!localPhotoDir.exists()) {
            localPhotoDir.mkdir();
        }
        File localMinPhotoDir = new File(this.localMinPhotoDir);
        if (!localMinPhotoDir.exists()) {
            localMinPhotoDir.mkdir();
        }
        for(File sourceFile : sourceDir.listFiles()) {
            if (!ImageUtils.checkIfUnhideImageFile(sourceFile)) {
                log.warn("相册图片复制时，" + sourceFile.getName() + "被过滤掉");
                continue;
            }
            BufferedImage squareImg = ImageUtils.toSquare(ImageIO.read(sourceFile));
            int rotate = 0;
            // photo
            Thumbnails.of(squareImg).scale(1f)
                    .rotate(rotate)
                    .outputQuality(photoQualityParam)
                    .toFile(this.localPhotoDir + sourceFile.getName());
            // minPhoto
            Thumbnails.of(squareImg).size(minPhotoWidth, minPhotoWidth)
                    .rotate(rotate)
                    .toFile(this.localMinPhotoDir + sourceFile.getName());
        }
        return true;
    }

    @Override
    public boolean imageUpload(String logMark) {
        this.upload(localPhotoDir, ossPhotoDir);
        this.upload(localMinPhotoDir, ossMinPhotoDir);
        return true;
    }

    private void upload(String localDirPath, String ossDirPath) {
        File localDir = new File(localDirPath);
        for(File img : localDir.listFiles()) {
            if (!ImageUtils.checkIfUnhideImageFile(img)) {
                log.warn("相册图片上传时，" + img.getName() + "被过滤掉");
                continue;
            }
            alibabaOssHandler.putObject(img.getAbsolutePath(), ossDirPath + img.getName());
            log.info("图片" + img.getAbsolutePath() + "上传完成，" + localDirPath + "->" + ossDirPath);
        }
    }
}
