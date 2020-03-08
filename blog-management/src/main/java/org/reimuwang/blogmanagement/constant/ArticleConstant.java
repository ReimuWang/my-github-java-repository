package org.reimuwang.blogmanagement.constant;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.ConfigurationException;
import org.reimuwang.commonability.config.ConfigUtils;

@Slf4j
public class ArticleConstant {

    public static String ARTICLE_DIR_PATH;

    public static String IMAGE_DIR_PATH;

    public static String IMAGE_PREFIX;

    static {
        try {
            ARTICLE_DIR_PATH = ConfigUtils.getString("reimuwang.article.articleDir", "");
            IMAGE_DIR_PATH = ConfigUtils.getString("reimuwang.article.imageDir", "");
            IMAGE_PREFIX = ConfigUtils.getString("reimuwang.article.imagePrefix", "/images/blog_pic");
        } catch (ConfigurationException e) {
            log.error("通过ConfigUtils获取配置文件中的值时失败", e);
            System.exit(0);
        }
    }
}
