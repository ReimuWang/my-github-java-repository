package org.reimuwang.blogmanagement.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.reimuwang.commonability.file.FileIOUtils;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class AlbumJsonDataHandler {

    private Map<String, JSONObject> map;

    public AlbumJsonDataHandler() {
        this.map = new TreeMap<>(Comparator.reverseOrder());
    }

    public void add(String name) {
        try {
            // 解析原始名称
            String[] array1 = name.split("\\.");
            String[] array2 = array1[0].split("_");
            String dateStr = array2[0];
            String hmsStr = array2[1];
            String subjectStr = array2[2];
            String descStr = array2[3];
            // 获得本图片所属天的结构体(若没有则创建)
            if (!this.map.containsKey(dateStr)) {
                JSONObject arrValue = new JSONObject();
                arrValue.put("text", new JSONArray());
                arrValue.put("type", new JSONArray());
                arrValue.put("link", new JSONArray());
                arrValue.put("year", Integer.parseInt(dateStr.substring(0, 4)));
                arrValue.put("month", Integer.parseInt(dateStr.substring(4, 6)));
                arrValue.put("day", Integer.parseInt(dateStr.substring(6, 8)));
                JSONObject arr = new JSONObject();
                arr.put("arr", arrValue);
                this.map.put(dateStr, arr);
            }
            JSONObject day = this.map.get(dateStr);
            day.getJSONObject("arr").getJSONArray("text").add(this.getHMS(hmsStr) +
                    ("0".equals(subjectStr) ? "" : "[" + subjectStr + "]") +
                    ("0".equals(descStr) ? "" : descStr));
            day.getJSONObject("arr").getJSONArray("type").add("image");
            day.getJSONObject("arr").getJSONArray("link").add(name);
        } catch (Exception e) {
            log.error("AlbumJsonDataHandler添加名称出错,name=" + name, e);
            throw e;
        }
    }

    private String getHMS(String hmsStr) {
        String hour = hmsStr.substring(0, 2);
        String minute = hmsStr.substring(2, 4);
        String second = hmsStr.substring(4, 6);
        String nullValue = "99";
        if (nullValue.equals(hour)) return "";
        StringBuilder result = new StringBuilder();
        result.append("[").append(hour).append("时");
        if (nullValue.equals(minute)) return result.append("]").toString();
        result.append(minute).append("分");
        if (nullValue.equals(second)) return result.append("]").toString();
        result.append(second).append("秒]");
        return result.toString();
    }

    public JSONObject createJson(String path) throws IOException {
        JSONObject result = new JSONObject();
        JSONArray rootArr = new JSONArray();
        result.put("list", rootArr);
        for (Map.Entry<String, JSONObject> entry : this.map.entrySet()) {
            rootArr.add(entry.getValue());
        }
        FileIOUtils.writeByChar(path, result.toJSONString());
        return result;
    }
}
