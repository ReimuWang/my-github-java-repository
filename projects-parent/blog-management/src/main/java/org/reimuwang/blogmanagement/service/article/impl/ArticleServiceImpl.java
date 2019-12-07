package org.reimuwang.blogmanagement.service.article.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.reimuwang.blogmanagement.entity.article.ArticleEntity;
import org.reimuwang.blogmanagement.service.article.ArticleService;
import org.reimuwang.commonability.server.CommonListResponse;
import org.reimuwang.commonability.string.StringHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Value("${reimuwang.article.articleDir}")
    private String articleDirPath;

    @Value("${reimuwang.article.imageDir}")
    private String imageDirPath;

    @Override
    public CommonListResponse<ArticleEntity> getArticleList(String logMark) throws Exception {
        CommonListResponse<ArticleEntity> result = new CommonListResponse<>();
        File articleDir = new File(this.articleDirPath);
        if (!articleDir.exists() || !articleDir.isDirectory()) {
            log.warn(logMark + "配置文件中设定的文章目录不存在，articleDirPath=" + this.articleDirPath);
            return result;
        }
        List<ArticleEntity> dataList = new ArrayList<>();
        for(File article : articleDir.listFiles()) {
            ArticleEntity articleEntity = ArticleEntity.build(article);
            if (null == articleEntity) {
                continue;
            }
            dataList.add(articleEntity);
        }
        result.setTotalCount(dataList.size());
        result.setDataList(dataList);
        return result;
    }

    @Override
    public JSONObject recoverFileName(Boolean preview, String logMark) throws Exception {
        String charSequence = " &<>.";
        JSONObject result = new JSONObject();
        result.put("preview", preview);
        // 文章
        this.recoverFileNameForArticle(result, preview, charSequence, logMark);
        // 图片
        this.recoverFileNameForImage(result, preview, charSequence, logMark);
        return result;
    }

    private void recoverFileNameForArticle(JSONObject result, Boolean preview, String charSequence, String logMark) throws Exception {
        JSONArray articleList = new JSONArray();
        result.put("articleList", articleList);
        List<ArticleEntity> dataList = this.getArticleList(logMark).getDataList();
        for (ArticleEntity articleEntity : dataList) {
            this.recoverIllegalName(articleEntity.getArticle(), articleList, charSequence, preview, null, false, logMark);
        }
    }

    private void recoverFileNameForImage(JSONObject result, Boolean preview, String charSequence, String logMark) throws Exception {
        JSONArray imageDirList = new JSONArray();
        result.put("imageDirList", imageDirList);
        File imageDir = new File(this.imageDirPath);
        if (!imageDir.exists() || !imageDir.isDirectory()) {
            log.warn(logMark + "配置文件中设定的图片目录不存在，articleDirPath=" + this.articleDirPath);
            return;
        }
        for(File imageDirCategory : imageDir.listFiles()) {
            if (!imageDirCategory.isDirectory()) {
                log.warn(logMark + "配置文件中设定的图片目录下存在非文件夹的文件。图片目录=" + this.articleDirPath + "，非法文件=" + imageDirCategory.getName());
                continue;
            }
            this.recoverIllegalName(imageDirCategory, imageDirList, charSequence, preview, null, true, logMark);
            for(File imageDirArticle : imageDirCategory.listFiles()) {
                if (!imageDirArticle.isDirectory()) {
                    log.warn(logMark + "某个分类图片目录下存在非文件夹的文件。分类图片目录=" + imageDirCategory.getName() + "，非法文件=" + imageDirArticle.getName());
                    continue;
                }
                this.recoverIllegalName(imageDirArticle, imageDirList, charSequence, preview, imageDirCategory.getName(), false, logMark);
            }
        }
    }

    /**
     * @param file 待检查的 文件/目录
     * @param msgList 记录纠错信息的列表
     * @param charSequence
     * @param preview
     * @param parentNameForShow 仅作为记录msgList用，不为null时表示有效
     * @param ifCategoryImage
     * @param logMark
     * @return true-file名称合法, false-file名称非法
     */
    private boolean recoverIllegalName(File file, JSONArray msgList, String charSequence, Boolean preview, String parentNameForShow, boolean ifCategoryImage, String logMark) throws Exception {
        String baseName = FilenameUtils.getBaseName(file.getName());
        String formatName = StringHandler.remove(baseName, charSequence);
        if (baseName.equals(formatName)) {
            return true;
        }
        String msgForLog = baseName + " -> " + formatName;
        String msgForShow = msgForLog;
        if (null != parentNameForShow) {
            msgForLog = "[" + parentNameForShow + "]" + msgForLog;
            msgForShow = "    -- " + msgForLog;
        }
        msgList.add(msgForShow);
        if (preview) {
            return false;
        }
        if (ifCategoryImage) {
            String errorMsg = "非preview时，分类图片目录不允许存在非法的情况，非法目录=" + file.getName() + "，[推荐在preview下统一调试]";
            log.error(logMark + errorMsg);
            throw new Exception(errorMsg);
        }
        String newFilePath = file.getParentFile() + File.separator + formatName;
        if (file.isFile()) {
            newFilePath = newFilePath + "." + FilenameUtils.getExtension(file.getName());
            msgForLog = "[文件]" + msgForLog;
        } else {
            msgForLog = "[图片]" + msgForLog;
        }
        file.renameTo(new File(newFilePath));
        log.info(logMark + "[更名成功]" + msgForLog);
        return false;
    }
    
    public static void main(String[] args) {
        File file = new File("/Users/reimuwang/hexo/themes/next/source/images/blog_pic/Python");
        System.out.println(file.getName());
    }
}
