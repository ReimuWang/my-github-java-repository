package org.reimuwang.blogmanagement.entity.article.request;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.reimuwang.blogmanagement.entity.article.articleenum.ArticleAdduceSource;
import org.reimuwang.blogmanagement.entity.article.articleenum.ArticleAdduceStatus;
import org.reimuwang.commonability.http.CommonRequest;

@Data
@Slf4j
public class ArticleQueryRequest extends CommonRequest {

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

    /**
     * ArticleAdduceStatus.index
     */
    private Integer adduceStatus;

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
        if (null != adduceStatus && !ArticleAdduceStatus.effective(adduceStatus)) {
            this.throwCheckException("adduceStatus=" + adduceStatus + "，非法，合法值=" + ArticleAdduceStatus.SHOW);
        }
    }

    private void throwCheckException(String errMsg) {
        log.error(errMsg);
        throw new IllegalArgumentException(errMsg);
    }
}
