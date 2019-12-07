package org.reimuwang.blogmanagement.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reimuwang.blogmanagement.entity.article.articleenum.ArticleAdduceType;
import org.reimuwang.blogmanagement.entity.article.ArticleEntity;
import org.reimuwang.commonability.file.FileIOUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 解析文章原始内容
 */
@Slf4j
public class ArticleParseHandler {

    /**
     * 头部区域行数
     */
    private static final int HEADLINE_COUNT = 6;

    private static final String CODE_MARK = "```";

    /**
     * 待解析的原始内容
     * content = headContent + bodyContent
     */
    private List<String> content;

    /**
     * 将content解析后填入articleEntity
     */
    private ArticleEntity articleEntity;

    /**
     * 头部区，见ArticleEntity顶部文档
     */
    private List<String> headContent;

    /**
     * content中除了headContent之外的区域
     */
    private List<String> bodyContent;

    private ArticleParseHandler(List<String> content, ArticleEntity articleEntity) {
        this.content = content;
        this.articleEntity = articleEntity;
    }

    public static ArticleParseHandler init(List<String> content, ArticleEntity articleEntity) {
        return new ArticleParseHandler(content, articleEntity);
    }

    public void parse() throws Exception {
        this.splitContent();
        this.parseHead();
        this.parseBody();
    }

    /**
     * 初步解析原始文件内容，从原始文件内容中提取头部区域及实际内容
     */
    private void splitContent() throws Exception {
        try {
            headContent = content.subList(0, HEADLINE_COUNT);
            bodyContent = content.subList(HEADLINE_COUNT, content.size());
        } catch (Exception e) {
            throwException("从原始文件内容中提取头部区域及实际内容时失败");
        }
    }

    /**
     * 解析头部区域
     */
    private void parseHead() throws Exception {
        String mark = "---";    // 头部区域的开始结束标志
        String head = this.trim(headContent.get(0));
        String tail = this.trim(headContent.get(HEADLINE_COUNT - 1));
        if (!mark.equals(head) || !mark.equals(tail)) {
            throwException("文章头部区域开始结束标志不正确，正确的标志应=" + mark);
        }
        // title,date,tags,categories 从索引1开始共需要取4次
        for (int i = 1; i <= 4; i++) {
            String line = this.headContent.get(i);
            String exceptionMsg = "解析头部区域时失败，失败的内容为=" + line;
            try {
                String[] lineArray = line.split(": ");
                if (lineArray.length != 2) {
                    throwException(exceptionMsg);
                }
                switch (lineArray[0]) {
                    case "title":
                        this.articleEntity.setTitle(lineArray[1]);
                        break;
                    case "date":
                        this.articleEntity.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lineArray[1]));
                        break;
                    case "tags":
                        String tags = lineArray[1];
                        String tagsReal = tags.substring(1, tags.length() - 1);
                        this.articleEntity.setTagArray(tagsReal.split(","));
                        break;
                    case "categories":
                        this.articleEntity.setCategory(lineArray[1]);
                        break;
                    default:
                        throwException(exceptionMsg);
                }
            } catch (Exception e) {
                throwException(exceptionMsg);
            }
        }
    }

    private void parseBody() throws Exception {
        this.checkCode();
        boolean ignoreCode = false;    // 应忽略被代码块包围的内容
        int moreMarkCount = 0;    // more标记个数
        for (String line : this.bodyContent) {
            if (StringUtils.isBlank(line)) {
                continue;
            }
            if (CODE_MARK.equals(line)) {
                ignoreCode = !ignoreCode;
                continue;
            }
            if (ignoreCode) {
                continue;
            }
            if ("<!-- more -->".equals(line)) {
                moreMarkCount++;
                continue;
            }
            line = trim(line);
            this.setArticleAdduce(line, 0, line.length() - 1);
        }
        if (moreMarkCount != 1) {
            throwException("more标记个数为" + moreMarkCount + "个");
        }
    }

    /**
     * 设置引用
     * 用正则其实更好一些，可惜我不熟，以后要学
     *
     * 本方法的核心思路：
     * 找出line中第一个满足如下规则的子串：
     * [xx](xx)
     * 随后将引用信息填加至文章的articleAdduceEntityList中.
     * 若line仍有剩余，则用剩下的部分继续递归调用本方法
     *
     * 核心功能为通过遍历line中的char，筛选出第一个[xx](xx)。这个过程会经历如下状态：
     * status=0：未匹配到第一个[ --> 初始状态
     * status=1：已匹配到第一个[
     * status=2：匹配到配对的]
     * status=3：紧随]匹配到(
     * status=4：匹配到配对的)  --> 匹配完成
     *
     * originalIndex表示line在原始字符串中的位置
     * originalMaxIndex为原始字符串最后一个字符的位置
     */
    private void setArticleAdduce(String line, int originalIndex, int originalMaxIndex) throws Exception {
        char firstLeft = '[';
        char firstRight = ']';
        char secondLeft = '(';
        char secondRight = ')';
        int status = 0;
        StringBuilder showNameBuilder = new StringBuilder();
        StringBuilder pathBuilder = new StringBuilder();
        int startIndex = -1;    // 匹配到的[]()在line中的开始位置
        int endIndex = -1;    // 匹配到的[]()在line中的结束位置
        char[] lineArray = line.toCharArray();
        for (int i = 0; i < lineArray.length; i++) {
            if (status == 4) {
                break;
            }
            char current = lineArray[i];
            switch (status) {
                case 0:    // 未匹配到第一个[
                    if (current == firstLeft) {    // 找到了第一个[
                        status = 1;    // 变更为状态1
                        startIndex = i;    // 记录第一个[的位置
                    }
                    break;
                case 1:    // 已匹配到第一个[
                    if (current == firstLeft) {    // 又找到一个[
                        startIndex = i;    // 更新第一个[的位置
                        showNameBuilder = new StringBuilder();    // 重新开始累计showName
                        break;
                    }
                    if (current == firstRight) {    // 匹配到配对的]
                        status = 2;    // 变更为状态2
                        break;
                    }
                    showNameBuilder.append(current);    // 其他字符，添加showName
                    break;
                case 2:    // 匹配到配对的]
                    if (current == secondLeft) {    // 紧随]匹配到(
                        status = 3;    // 变更为状态3
                        break;
                    }
                    // 其他字符说明]之后没有紧跟着(，所有状态清零重来
                    status = 0;
                    startIndex = -1;
                    showNameBuilder = new StringBuilder();
                    if (current == firstLeft) {    // 找到了第一个[
                        status = 1;    // 变更为状态1
                        startIndex = i;    // 记录第一个[的位置
                    }
                    break;
                case 3:    // 紧随]匹配到(
                    if (current == secondRight) {    // 匹配到配对的)
                        status = 4;    // 匹配完成
                        endIndex = i;    // 记录结束字符位置
                        break;
                    }
                    pathBuilder.append(current);    // 其他字符，添加path
                    break;
                default:
                    throwException(line + "在匹配[]()时status=" + status + "，非法");
            }
        }
        if (status != 4) {    // line到最后也没打到status=4，说明未匹配成功[]()
            return;
        }
        String showName = showNameBuilder.toString();
        String path = pathBuilder.toString();
        int originalStartIndex = originalIndex + startIndex;    // 匹配到的[]()在原始字符串中的开始位置
        int originalEndtIndex = originalIndex + endIndex;    // 匹配到的[]()在原始字符串中的结束位置
        if (StringUtils.isBlank(showName)) {
            throwException("匹配到[]()发现showName为空，当前line=" + line);
        }
        if (startIndex != 0 && line.charAt(startIndex - 1) == '!') {    // 需要被最为图片处理
            if (originalStartIndex > 1 || originalEndtIndex != originalMaxIndex) {
                throwException("匹配到[]()且判定为图片，但该图片未占满一行，当前line=" + line + ",originalStartIndex=" + originalStartIndex);
            }
            this.articleEntity.addArticleAdduceEntity(showName, path, ArticleAdduceType.IMAGE);
        } else {    // 需要被作为文章处理
            this.articleEntity.addArticleAdduceEntity(showName, path, ArticleAdduceType.ARTICLE);
        }
        if (originalEndtIndex == originalMaxIndex) {    // 已遍历到最后
            return;
        }
        this.setArticleAdduce(line.substring(endIndex + 1), originalEndtIndex + 1, originalMaxIndex);
    }

    /**
     * 1.检查行级代码块及行内代码块是否合法
     * 2.不准存在行内代码块
     * @throws Exception
     */
    private void checkCode() throws Exception {
        int countLine = 0;    // 行级```个数，应该为偶数才对
        for (String line : this.bodyContent) {
            if (StringUtils.isBlank(line)) {
                continue;
            }
            if (CODE_MARK.equals(line.trim())) {    // 行级
                if (!CODE_MARK.equals(line)) {
                    throwException(CODE_MARK + "独自占据一行时有留白");
                }
                countLine++;
                continue;
            }
            // 行内
            char[] lineCharArray = line.toCharArray();
            int countInner = 0;
            for (int i = 0; i < lineCharArray.length; i++) {
                char c = lineCharArray[i];
                if ('`' == c) {
                    countInner++;
                }
            }
            if (countInner > 0 && countInner % 6 == 0) {    // ```xxx```是一组，6个字符，不考虑拼错的情况
                throwException(CODE_MARK + "存在行内代码块");
            }
        }
        if (countLine % 2 != 0) {
            throwException(CODE_MARK + "独自占据一行时没有完全配对");
        }
    }

    /**
     * 有个很诡异的问题，部分文章第一行会插入char65279
     * 查了一下又是windows的锅，兼容了吧
     * @param str
     * @return
     */
    private String trim(String str) {
        return new Character(str.charAt(0)).hashCode() == 65279 ? str.substring(1) : str;
    }

    private void throwException(String msg) throws Exception {
        msg = msg + "，file=" + this.articleEntity.getArticle().getName();
        log.error(msg);
        throw new Exception(msg);
    }
    
    public static void main(String[] args) throws IOException {
        String path1 = "/Users/reimuwang/hexo/source/_posts/Java JDK7源码-javautilIteratorE.md";
        List<String> content1 = FileIOUtils.readByLine(path1);
        System.out.println(content1.get(0).length());

        String path2 = "/Users/reimuwang/hexo/source/_posts/Java JDK7源码-javautilSetE.md";
        List<String> content2 = FileIOUtils.readByLine(path2);
        System.out.println(new Character(content2.get(0).toCharArray()[0]).hashCode());
    }
}
