package org.reimuwang.filemanagement.entity.es.input;

import lombok.Data;

import java.util.List;

/**
 * 与配置在磁盘上的json文件的格式一一对应
 */
@Data
public class EsInputEntity {

    private String name;

    private List<String> tags;
}
