package org.reimuwang.blogmanagement.entity.article.articleenum;

/**
 * 文章引用类型
 */
public enum ArticleAdduceType {

    ARTICLE("文章")
    , IMAGE("图片")
    , UNKNOWN("未知");

    private String desc;

    private ArticleAdduceType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return this.desc;
    }
}
