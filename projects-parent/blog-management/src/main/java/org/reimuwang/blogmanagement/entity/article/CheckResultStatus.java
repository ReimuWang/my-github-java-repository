package org.reimuwang.blogmanagement.entity.article;

public enum CheckResultStatus {

    SUCCESS("正常")
    , WARN("轻微问题-可接受")
    , ERROR("严重问题-需解决");

    private String desc;

    private CheckResultStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
