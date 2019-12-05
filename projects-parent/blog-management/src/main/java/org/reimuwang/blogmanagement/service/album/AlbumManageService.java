package org.reimuwang.blogmanagement.service.album;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

public interface AlbumManageService {

    JSONObject jsonGenerate(String logMark) throws IOException;

    boolean imageCompressAndCopy(Float photoQualityParam, String logMark) throws IOException;

    boolean imageUpload(String logMark);
}
