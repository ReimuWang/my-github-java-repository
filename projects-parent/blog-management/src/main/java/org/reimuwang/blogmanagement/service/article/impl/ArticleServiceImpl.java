package org.reimuwang.blogmanagement.service.article.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.reimuwang.blogmanagement.constant.ArticleConstant;
import org.reimuwang.blogmanagement.entity.article.ArticleEntity;
import org.reimuwang.blogmanagement.entity.article.articleenum.ArticleAdduceSource;
import org.reimuwang.blogmanagement.entity.article.request.ArticleQueryRequest;
import org.reimuwang.blogmanagement.service.article.ArticleService;
import org.reimuwang.commonability.server.CommonListResponse;
import org.reimuwang.commonability.string.StringHandler;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Override
    public CommonListResponse<ArticleEntity> getArticleList(ArticleQueryRequest articleQueryRequest, String logMark) throws Exception {
        articleQueryRequest.check(logMark);
        CommonListResponse<ArticleEntity> result = new CommonListResponse<>();
        List<ArticleEntity> dataList = this.getAllArticle(logMark).stream()
                                       // 过滤：是否存在引用
                                       .filter(articleEntity -> null == articleQueryRequest.getExistAdduce() ? true : articleQueryRequest.getExistAdduce() ^ articleEntity.getArticleAdduceEntityList().isEmpty())
                                       // 过滤：引用来源
                                       .filter(articleEntity -> {
                                           if (null == articleQueryRequest.getAdduceSource()) {
                                               return true;
                                           }
                                           ArticleAdduceSource articleAdduceSource = ArticleAdduceSource.getEnumByIndex(articleQueryRequest.getAdduceSource());
                                           articleEntity.filterAdduceSource(articleAdduceSource);
                                           return !articleEntity.getArticleAdduceEntityList().isEmpty();
                                       })
                                       .collect(Collectors.toList());
        result.setTotalCount(dataList.size());
        dataList = this.paging(dataList, articleQueryRequest.getPage(), articleQueryRequest.getPageSize(), logMark);
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

    private List<ArticleEntity> getAllArticle(String logMark) throws Exception {
        List<ArticleEntity> result = new ArrayList<>();
        File articleDir = new File(ArticleConstant.ARTICLE_DIR_PATH);
        if (!articleDir.exists() || !articleDir.isDirectory()) {
            log.warn(logMark + "配置文件中设定的文章目录不存在，articleDirPath=" + ArticleConstant.ARTICLE_DIR_PATH);
            return result;
        }
        for(File article : articleDir.listFiles()) {
            ArticleEntity articleEntity = ArticleEntity.build(article);
            if (null == articleEntity) {
                continue;
            }
            result.add(articleEntity);
        }
        return result;
    }

    /**
     * 分页
     * @param dataList
     * @param page 自1起
     * @param pageSize
     * @param logMark
     * @return
     */
    private List<ArticleEntity> paging(List<ArticleEntity> dataList, Integer page, Integer pageSize, String logMark) {
        logMark = "[分页]" + logMark;
        if (dataList.isEmpty()) {
            log.info(logMark + "传入数据为空，无需分页");
            return dataList;
        }
        int totalSize = dataList.size();
        int startIndex = (page - 1) * pageSize;
        if (startIndex >= totalSize) {
            log.info(logMark + "totalSize={},startIndex={},startIndex>=totalSize,无需分页", totalSize, startIndex);
            return new ArrayList<>();
        }
        int endIndex = startIndex + pageSize - 1;
        if (endIndex >= totalSize) {
            endIndex = totalSize - 1;
        }
        log.info(logMark + "totalSize={},[{},{}] -> [{},{}]", totalSize, page, pageSize, startIndex, endIndex);
        return dataList.subList(startIndex, endIndex + 1);
    }

    private void recoverFileNameForArticle(JSONObject result, Boolean preview, String charSequence, String logMark) throws Exception {
        JSONArray articleList = new JSONArray();
        result.put("articleList", articleList);
        List<ArticleEntity> dataList = this.getAllArticle(logMark);
        for (ArticleEntity articleEntity : dataList) {
            this.recoverIllegalName(articleEntity.getArticle(), articleList, charSequence, preview, null, false, logMark);
        }
    }

    private void recoverFileNameForImage(JSONObject result, Boolean preview, String charSequence, String logMark) throws Exception {
        JSONArray imageDirList = new JSONArray();
        result.put("imageDirList", imageDirList);
        File imageDir = new File(ArticleConstant.IMAGE_DIR_PATH);
        if (!imageDir.exists() || !imageDir.isDirectory()) {
            log.warn(logMark + "配置文件中设定的图片目录不存在，图片目录=" + ArticleConstant.IMAGE_DIR_PATH);
            return;
        }
        for(File imageDirCategory : imageDir.listFiles()) {
            if (!imageDirCategory.isDirectory()) {
                log.warn(logMark + "配置文件中设定的图片目录下存在非文件夹的文件。图片目录=" + ArticleConstant.IMAGE_DIR_PATH + "，非法文件=" + imageDirCategory.getName());
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
}
