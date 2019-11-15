package org.reimuwang.commonability.http;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class CommonResponse {

    private String status;

    private Object data;

    private Object msg;

    public CommonResponse(Object data, Object msg) {
        this.data = null == data ? new JSONObject() : data;
        this.msg = null == msg ? "" : msg;
    }

    public static CommonResponse success(Object msg) {
        return CommonResponse.success(null, msg);
    }

    public static CommonResponse success(Object data, Object msg) {
        CommonResponse response = new CommonResponse(data, msg);
        response.setStatus(ResponseStatus.SUCCESS.getDesc());
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