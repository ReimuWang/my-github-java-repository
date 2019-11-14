package org.reimuwang.commonability.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.util.List;

/**
 * 从静态文件接收json格式输入，并转换为对应target对象
 * 若接收的是json数组，则转换为target对象的列表
 */
public class JsonInputUtils {

    private static final String DEFAULT_ENCODING = "UTF-8";

    private String sysPath;

    private String encoding = JsonInputUtils.DEFAULT_ENCODING;

    private JsonInputUtils(String sysPath) {
        this.sysPath = sysPath;
    }

    public static JsonInputUtils createFromSysPath(String sysPath) {
        return new JsonInputUtils(sysPath);
    }

    public JsonInputUtils setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public <T> T convertJsonObject(Class<T> clazz) throws IOException {
        return JSON.toJavaObject(JSONObject.parseObject(this.buildJsonStr()), clazz);
    }

    public <T> List<T> convertJsonArray(Class<T> clazz) throws IOException {
        return JSONArray.parseArray(this.buildJsonStr()).toJavaList(clazz);
    }

    private String buildJsonStr() throws IOException {
        Resource resource = new FileSystemResource(this.sysPath);
        EncodedResource encodedResource = new EncodedResource(resource, this.encoding);
        return FileCopyUtils.copyToString(encodedResource.getReader());
    }
}
