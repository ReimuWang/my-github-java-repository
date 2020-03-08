package org.reimuwang.commonability.http;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class CommonResponse {

    private String status;

    private Object data;

    private Object msg;

    private Integer totalCount;

    private CommonResponse(Object data, Object msg) {
        this.data = null == data ? new JSONObject() : data;
        this.msg = null == msg ? "" : msg;
    }

    public static CommonResponse success() {
        return CommonResponse.success(null, null);
    }

    public static CommonResponse success(Object msg) {
        return CommonResponse.success(null, msg);
    }

    public static CommonResponse success(Object data, Object msg) {
        CommonResponse response = new CommonResponse(data, msg);
        response.setStatus(ResponseStatus.SUCCESS.getDesc());
        return response;
    }

    public static CommonResponse success(Object data, Object msg, Integer totalCount) {
        CommonResponse response = new CommonResponse(data, msg);
        response.setStatus(ResponseStatus.SUCCESS.getDesc());
        response.setTotalCount(null == totalCount ? 0 : totalCount);
        return response;
    }

    public static CommonResponse error() {
        return CommonResponse.error(null, null);
    }

    public static CommonResponse error(Object msg) {
        return CommonResponse.error(null, msg);
    }

    public static CommonResponse error(Object data, Object msg) {
        CommonResponse response = new CommonResponse(data, msg);
        response.setStatus(ResponseStatus.ERROR.getDesc());
        return response;
    }
}

enum ResponseStatus {

    SUCCESS("success"),
    ERROR("error");

    private String desc;

    ResponseStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}