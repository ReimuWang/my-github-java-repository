package org.reimuwang.blogmanagement.entity.article.articleenum;

/**
 * 文章引用状态
 */
public enum ArticleAdduceStatus {

    ACCESSIBLE("可访问")
    , INACCESSIBLE("不可访问")
    , UNKNOWN("未知");

    private String desc;

    ArticleAdduceStatus(String desc) {
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
