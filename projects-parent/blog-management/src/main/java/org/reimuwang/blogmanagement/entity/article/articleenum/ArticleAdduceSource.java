package org.reimuwang.blogmanagement.entity.article.articleenum;

/**
 * 文章引用来源
 */
public enum ArticleAdduceSource {

    INTERIOR("内部", 1)
    , WEB("网络", 2)
    , UNKNOWN("未知", 0);

    private String desc;

    private Integer index;

    private ArticleAdduceSource(String desc, Integer index) {
        this.desc = desc;
        this.index = index;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return this.desc;
    }
}
