package org.reimuwang.blogmanagement.entity.article;

import lombok.Data;

/**
 * 文章引用
 */
@Data
public class ArticleAdduceEntity {

    /**
     * 存储在文章中的原始字符串，形如：
     *
     * 内部文章
     * [Github-添加SSH密钥](/2017/04/12/Github-添加SSH密钥/)
     *
     * 网络文章
     * [GitHub官网](https://github.com/)
     *
     * 内部图片：
     * ![0.jpg](/images/blog_pic/Github/Markdown语法/0.jpg)
     *
     * 网络图片：
     * ![网络图片](http://5b0988e595225.cdn.sohucs.com/images/20170815/2b2b96ea6139429a9c2fcae53af5b8d3.jpeg)
     */
    private String originalStr = "";


    private ArticleAdduceSource source = ArticleAdduceSource.UNKNOWN;
}
