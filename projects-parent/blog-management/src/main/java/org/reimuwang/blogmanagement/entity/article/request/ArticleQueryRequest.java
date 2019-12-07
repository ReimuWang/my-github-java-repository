package org.reimuwang.blogmanagement.entity.article.request;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.reimuwang.blogmanagement.entity.article.articleenum.ArticleAdduceSource;

@Data
@Slf4j
public class ArticleQueryRequest {

    /**
     * 分页页码，自1开始
     */
    private Integer page = 1;

    /**
     * 每页个数
     */
    private Integer pageSize = 10;

    /**
     * true-文章中存在引用
     * false-文章中不存在引用
     * null-不查询该字段
     */
    private Boolean existAdduce;

    /**
     * ArticleAdduceSource.index
     */
    private Integer adduceSource;

    public void check(String logMark) {
        if (null == page || page <= 0) {
            this.throwCheckException(logMark + "page必须为大于等于1的整数,page=" + this.page);
        }
        if (null == pageSize || pageSize <= 0) {
            this.throwCheckException(logMark + "pageSize必须为大于等于1的整数,pageSize=" + this.pageSize);
        }
        if (null != adduceSource && !ArticleAdduceSource.effective(adduceSource)) {
            this.throwCheckException("adduceSource=" + adduceSource + "，非法，合法值=" + ArticleAdduceSource.SHOW);
        }
    }

    private void throwCheckException(String errMsg) {
        log.error(errMsg);
        throw new IllegalArgumentException(errMsg);
    }
}
