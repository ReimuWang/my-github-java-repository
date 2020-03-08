package org.reimuwang.commonability.alibaba.oss;

import ch.qos.logback.classic.gaffer.PropertyUtil;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.PutObjectResult;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AlibabaOssHandler {

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;

    /**
     * 递归列出某前缀(相当于目录)下的所有非目录文件的去掉所有前缀信息的简单名称
     * @param prefix 形如 dir1/dir2/ 注意：最开始没有/，最后有/
     *
     * 例如：
     * 传入前缀为 photos/
     * 则objectListing.getObjectSummaries()取出的值形如：
     *
     * photos/
     * photos/2018-5-1_dd.jpg
     * photos/2018-5-1_ssss.jpg
     * photos/2018-6-1_aa.jpg
     * photos/dd/
     *
     * 本方法会去掉所有前缀信息，同时过滤掉目录photos/及photos/dd/
     * 最终返回字符串列表：
     *
     * 2018-5-1_dd.jpg
     * 2018-5-1_ssss.jpg
     * 2018-6-1_aa.jpg
     */
    public List<String> listObjects(String prefix) {
        if (StringUtils.isBlank(prefix)) {
            throw new NullPointerException("prefix为空");
        }
        OSSClient ossClient = null;
        ObjectListing objectListing;
        try {
            List<String> result = new ArrayList<String>();
            ossClient = getClient();
            String separator = "/";
            do {
                ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName)
                                                        .withPrefix(prefix)
                                                        .withMarker(null)
                                                        .withMaxKeys(200);
                objectListing = ossClient.listObjects(listObjectsRequest);
                List<String> nameList = objectListing
                                        .getObjectSummaries()
                                        .stream()
                                        .map(objectSummary -> objectSummary.getKey())
                                        .filter(name -> !name.endsWith(separator))
                                        .map(name -> {
                                            String[] array = name.split(separator);
                                            return array[array.length - 1];
                                        })
                                        .collect(Collectors.toList());
                result.addAll(nameList);
            } while (objectListing.isTruncated());
            return result;
        } finally {
            closeClient(ossClient);
        }
    }

    private OSSClient getClient() {
        return new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    private void closeClient(OSSClient ossClient) {
        if (null != ossClient) {
            ossClient.shutdown();
        }
    }

    /**
     * @param sourceName 源文件，形如 D:\\1.txt
     * @param objectName 上传到阿里云后的文件名 形如 dir1/dir2/test.txt
     */
    public PutObjectResult putObject(String sourceName, String objectName) {
        if (StringUtils.isBlank(sourceName) || StringUtils.isBlank(objectName)) {
            throw new NullPointerException("sourceName 或 objectName为空");
        }
        OSSClient ossClient = null;
        try {
            ossClient = getClient();
            return ossClient.putObject(bucketName, objectName, new File(sourceName));
        } finally {
            closeClient(ossClient);
        }
    }
}
