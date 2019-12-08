package org.reimuwang.blogmanagement.entity.article;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.reimuwang.blogmanagement.constant.ArticleConstant;
import org.reimuwang.blogmanagement.entity.article.articleenum.ArticleAdduceSource;
import org.reimuwang.blogmanagement.entity.article.articleenum.ArticleAdduceStatus;
import org.reimuwang.blogmanagement.entity.article.articleenum.ArticleAdduceType;

import java.io.File;

/**
 * 文章引用
 *
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
@Data
public class ArticleAdduceEntity {

    @JSONField(serialzeFeatures = SerializerFeature.WriteEnumUsingToString)
    private ArticleAdduceSource source = ArticleAdduceSource.UNKNOWN;

    @JSONField(serialzeFeatures = SerializerFeature.WriteEnumUsingToString)
    private ArticleAdduceType type;

    @JSONField(serialzeFeatures = SerializerFeature.WriteEnumUsingToString)
    private ArticleAdduceStatus status = ArticleAdduceStatus.UNKNOWN;

    /**
     * 即[]中的内容
     */
    private String showName;

    /**
     * 即()中的内容
     */
    private String path;

    public ArticleAdduceEntity(String showName, String path, ArticleAdduceType type) {
        this.showName = showName;
        this.path = path;
        this.type = type;
        this.judgeAndSetSource();
        this.judgeAndSetStatus();
    }

    private void judgeAndSetSource() {
        if (StringUtils.isBlank(this.path)) {
            return;
        }
        this.source = path.startsWith("http") ? ArticleAdduceSource.WEB : ArticleAdduceSource.INTERIOR;
    }

    private void judgeAndSetStatus() {
        if (!ArticleAdduceSource.INTERIOR.equals(this.source)) {
            return;
        }
        switch (this.type) {
            case ARTICLE:
                break;
            case IMAGE:
                if (!this.path.startsWith(ArticleConstant.IMAGE_PREFIX)) {
                    this.status = ArticleAdduceStatus.INACCESSIBLE;
                    break;
                }
                String realPath = ArticleConstant.IMAGE_DIR_PATH + path.replaceFirst(ArticleConstant.IMAGE_PREFIX, "");
                File image = new File(realPath);
                if (!image.exists() || !image.isFile()) {
                    this.status = ArticleAdduceStatus.INACCESSIBLE;
                    break;
                }
                this.status = ArticleAdduceStatus.ACCESSIBLE;
                break;
        }
    }
}
