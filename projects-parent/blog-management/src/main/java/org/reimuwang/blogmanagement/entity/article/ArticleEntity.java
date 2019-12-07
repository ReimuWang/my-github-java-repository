package org.reimuwang.blogmanagement.entity.article;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.reimuwang.blogmanagement.entity.article.articleenum.ArticleAdduceSource;
import org.reimuwang.blogmanagement.entity.article.articleenum.ArticleAdduceType;
import org.reimuwang.blogmanagement.utils.ArticleParseHandler;
import org.reimuwang.commonability.file.FileIOUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
     * 文章发布日期，即为文章内容头部区域的date字段值对应的时间
     */
    private Date createTime;

    /**
     * tag列表，即为文章内容头部区域的tags字段值转换成的列表
     */
    private List<String> tagList = new ArrayList<>();

    /**
     * 所属分类，即为文章内容头部区域的categories字段值
     */
    private String category = "";

    /**
     * 文章引用列表，默认排序为文章中的出现顺序
     */
    private List<ArticleAdduceEntity> articleAdduceEntityList = new ArrayList<>();

    /**
     * 构建失败则返回null
     * @return
     */
    public static ArticleEntity build(File article) throws Exception {
        if (!checkInitFile(article)) {
            log.warn("构建文章时发现传入文件非法,name=" + article.getName());
            return null;
        }
        ArticleEntity result = new ArticleEntity();
        result.article = article;
        result.fileName = result.article.getName();
        ArticleParseHandler.init(FileIOUtils.readByLine(result.article.getPath()), result).parse();
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

    public void setTagArray(String[] tags) {
        this.tagList = new ArrayList<>(Arrays.asList(tags));
    }

    public void addArticleAdduceEntity(String showName, String path, ArticleAdduceType type) {
        this.articleAdduceEntityList.add(new ArticleAdduceEntity(showName, path, type));
    }

    public boolean containsAdduceSource(ArticleAdduceSource articleAdduceSource) {
        if (null == articleAdduceSource) {
            throw new NullPointerException("articleAdduceSource为空");
        }
        return articleAdduceEntityList.stream().filter(a -> a.getSource().equals(articleAdduceSource)).count() > 0;
    }
}
