package org.reimuwang.blogmanagement.entity.article;

/**
 * 文章引用来源
 */
public enum ArticleAdduceSource {

    INTERIOR("内部")
    , WEB("网络")
    , UNKNOWN("未知");

    private String desc;

    private ArticleAdduceSource(String desc) {
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
