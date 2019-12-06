package org.reimuwang.blogmanagement.entity.article;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * 按照hexo文章生成规范，文章头部区域例子为：
 *
 * ---
 * title: Java 设计模式-2.Adapter模式
 * date: 2018-06-08 10:27:49
 * tags: [Java,设计模式]
 * categories: Java 设计模式
 * ---
 *
 * 在此基础上，个人自身添加了如下规范
 * 1.title格式为:
 *   categories-文章真实名称
 * 2.存储文章的文件名为title去掉所有空格后的结果
 */
@Data
@Slf4j
public class ArticleEntity {

    /**
     * 原始文件
     */
    @JSONField(serialize=false)
    private File article;

    /**
     * File.getName()获得的存在系统磁盘上的简单文件名
     */
    private String fileName = "";

    /**
     * 展示名，即为文章内容头部区域的title字段值
     */
    private String title = "";

    /**
     * 文章发布日期，即为文章内容头部区域的date字段值的时间戳
     */
    private Long date = -1L;

    /**
     * tag列表，即为文章内容头部区域的tags字段值转换成的列表
     */
    private List<String> tagList = new ArrayList<>();

    /**
     * 所属分类，即为文章内容头部区域的categories字段值
     */
    private String category = "";

    private List<ArticleAdduceEntity> articleAdduceEntityList = new ArrayList<>();

    private CheckResult checkResult = CheckResult.success();

    /**
     * 构建失败则返回null
     * @return
     */
    public static ArticleEntity build(File article) throws FileNotFoundException {
        if (null == article) {
            throw new NullPointerException("传入article实例为null");
        }
        if (!article.exists()) {
            throw new FileNotFoundException("文件不存在,path=" + article.getPath());
        }
        if (!checkInitFile(article)) {
            log.warn("构建文章时发现传入文件非法,path=" + article.getPath());
            return null;
        }
        ArticleEntity result = new ArticleEntity();
        result.article = article;
        result.fileName = result.article.getName();
        return result;
    }

    private static boolean checkInitFile(File article) {
        if (!article.isFile() || article.isHidden()) {
            return false;
        }
        String extension = FilenameUtils.getExtension(article.getName());
        if (!"md".equals(extension)) {
            return false;
        }
        return true;
    }
}
