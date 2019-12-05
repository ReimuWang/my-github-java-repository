package org.reimuwang.blogmanagement.service.album.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.reimuwang.blogmanagement.service.album.AlbumManageService;
import org.reimuwang.blogmanagement.utils.AlbumJsonDataHandler;
import org.reimuwang.commonability.alibaba.oss.AlibabaOssHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class AlbumManageServiceImpl implements AlbumManageService {

    @Autowired
    private AlibabaOssHandler alibabaOssHandler;

    @Value("${reimuwang.alibaba.oss.photoDir}")
    private String ossPhotoDir;

    @Value("${reimuwang.album.local.jsonpath}")
    private String jsonOutPutPath;

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
}
