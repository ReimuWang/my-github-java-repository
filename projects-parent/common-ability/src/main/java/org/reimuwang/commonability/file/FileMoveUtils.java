package org.reimuwang.commonability.file;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.reimuwang.commonability.file.exception.FileMatchMultipleException;
import org.reimuwang.commonability.file.exception.IllegalFileNameException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 核心方法fileMove实现如下两步：
 * 1.${targetDir}下的[文件列表A]按[规则1]更名
 * 2.将${sourceDir}下的[文件B]移动至${targetDir}。移动后：
 *   - [文件B]按[规则2]更名
 *   - [文件列表A]按[规则3]更名
 *
 *   [文件列表A]中的元素的筛选规则为：
 *   1.是文件而非目录
 *   2.文件简单名称格式如下：
 *     [index].[name]
 *     其中：
 *       - [index]：用于排序的非负整数
 *       - [name]：文件的有实际含义的名称(含文件格式部分)
 *
 *   [规则1]为：
 *   [文件列表A]的元素的[index]调整为：
 *     1.保证最小值自0开始
 *     2.每两个连续[index]之间的差<=1
 *   简单来说，[规则1]就是确保[文件列表A]中的文件按0,1,2,3,4,5...紧密排序
 *   需要注意是，整理后[index]是允许重复的，即允许形如0,1,2,3,3,3,4,5...的存在
 *
 *   [文件B]的筛选规则为：
 *   文件简单名称格式如下：
 *     ${prefix}.[expectIndex].[name]
 *     其中
 *     - ${prefix}：表明[文件B]身份的前缀
 *     - [expectIndex]：期望移入${targetDir}后的排序值
 *     - [name]：文件的有实际含义的名称(含文件格式部分)
 *   筛选[文件B]时，只使用如下规则：
 *   ${prefix}[other]
 *   若按该规则匹配到了多个文件，则抛出异常
 *
 *   [规则2]与[规则3]解释，用几个例子来说明更名规则：
 *   ex.1
 *     - [文件B][expectIndex]=2
 *     - [文件列表A][index]=[0, 1, 2, 3, 4, 5]
 *     则最终[文件B]写入[index]=2,[文件列表A]中原有文件中的3~5均向后挪一个，相当于将[文件B]插入到了[文件列表A]中的2号位置
 *   ex.2
 *     - [文件B][expectIndex]=2
 *     - [文件列表A]为空
 *     则最终[文件B][index]=0
 *   ex.3
 *     - [文件B][expectIndex]=18
 *     - [文件列表A][index]=[0, 1, 2, 3, 4, 5]
 *   则最终[文件B][index]=6，[文件列表A]无需移动
 *
 *
 * 按如下步骤使用本工具类:
 * 1.调用init方法初始化一个新的实例o。后续的一切操作都是基于该实例
 * 2.调用build方法完成o的构建，若此前设置有误会抛出相应异常
 * 3.执行fileMove方法，注意该实例方法不是线程安全的；若要进行并发操作，建议每次均生成新的实例。不同实例间的fileMove方法是线程安全的
 */
public class FileMoveUtils {

    /**
     * 待移动文件所在的源文件夹路径
     */
    private String sourceDirPath;

    /**
     * 待移动文件需移动到的目标文件夹路径
     */
    private String targetDirPath;

    /**
     * 匹配待移动文件的前缀
     */
    private String prefix;

    /**
     * 用于分割文件名称，其值与SEPARATOR_FOR_JOIN相等
     */
    private static final String SEPARATOR_FOR_SPLIT = "\\.";

    /**
     * 用于连接文件名称，其值与SEPARATOR_FOR_SPLIT相等
     */
    private static final String SEPARATOR_FOR_JOIN = ".";

    /**
     * 禁止外部调用
     * @param sourceDirPath 待移动文件所在的源文件夹路径
     * @param targetDirPath 待移动文件需移动到的目标文件夹路径
     * @param prefix 自${sourceDir}中识别出待移动文件的前缀
     */
    private FileMoveUtils(String sourceDirPath, String targetDirPath, String prefix) {
        this.sourceDirPath = sourceDirPath;
        this.targetDirPath = targetDirPath;
        this.prefix = prefix;
    }

    /**
     * 本类唯一对外暴露的静态类
     * 初始化一个新的对象并返回
     * 本方法并不会对sourceDir及targetDir做校验，一切校验均推迟至build方法中
     * @param sourceDirPath 待移动文件所在的源文件夹路径
     * @param sourceDirPath 待移动文件需移动到的目标文件夹路径
     * @param prefix 自${sourceDir}中识别出待移动文件的前缀
     * @return 基于sourceDir及targetDir生成的实例
     */
    public static FileMoveUtils init(String sourceDirPath, String targetDirPath, String prefix) {
        return new FileMoveUtils(sourceDirPath, targetDirPath, prefix);
    }

    /**
     * 完成本对象的构建操作，当前只有字段校验
     * 对于${sourceDir}及${targetDir}，只判空，文件夹是否存在推迟至每次调用fileMove方法时判断
     * 这样做的原因在于，文件的存在性可能会在程序的运行过程中发生变化，初始build时判断一次没有意义
     *
     * @return 自身
     */
    public FileMoveUtils build() {
        if (StringUtils.isBlank(this.sourceDirPath)) {
            throw new NullPointerException("sourceDirPath为空");
        }
        if (StringUtils.isBlank(this.targetDirPath)) {
            throw new NullPointerException("targetDirPath为空");
        }
        if (StringUtils.isBlank(this.prefix)) {
            throw new NullPointerException("prefix为空");
        }
        if (this.prefix.contains(SEPARATOR_FOR_SPLIT) || this.prefix.contains(SEPARATOR_FOR_JOIN)) {
            throw new IllegalArgumentException("prefix中包含用于分割名称的特殊分隔符[" + SEPARATOR_FOR_JOIN + "]");
        }
        return this;
    }

    /**
     * 移动文件
     * @return null-无需移动 否则固定长度为2：
     *         0：被移动文件的旧名称
     *         1：被移动文件移动后的新名称
     * @throws FileNotFoundException
     * @throws FileMatchMultipleException
     * @throws IllegalFileNameException
     */
    public String[] moveFile() throws FileNotFoundException, FileMatchMultipleException, IllegalFileNameException {
        // 目标文件夹
        File targetDir = new File(this.targetDirPath);
        if (!targetDir.exists() || !targetDir.isDirectory()) {
            throw new FileNotFoundException("目标文件夹" + this.targetDirPath + "不存在");
        }
        // 整理目标文件夹
        this.formatTargetDir(targetDir);
        // 源文件夹
        File sourceDir = new File(this.sourceDirPath);
        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            throw new FileNotFoundException("源文件夹" + this.sourceDirPath + "不存在");
        }
        // 待移动的文件
        File fileToMove = this.getFileToMove(sourceDir);
        if (null == fileToMove) {
            return null;
        }
        // 分割出待移动文件的名字和排序
        String oriName = fileToMove.getName();
        String[] oriNameSplit = oriName.split(SEPARATOR_FOR_SPLIT, 3); // ${prefix}.[expectIndex].[name]

        String illegalFileNameMsg = "待移动文件的文件名非法，文件名=" + oriName;
        if (oriNameSplit.length != 3 || StringUtils.isBlank(oriNameSplit[1]) || !StringUtils.isNumeric(oriNameSplit[1])) {
            throw new IllegalFileNameException(illegalFileNameMsg);
        }
        Integer expectIndex = Integer.parseInt(oriNameSplit[1]);
        if (expectIndex < 0) {
            throw new IllegalFileNameException(illegalFileNameMsg);
        }
        // 移动
        return new String[]{oriName, this.moveFileInner(fileToMove, expectIndex, oriNameSplit[2], targetDir)};
    }

    private void formatTargetDir(File targetDir) {
        // [文件列表A]
        List<FileForSort> fileForSortList = this.getFileListInTargetDir(targetDir);
        List<NumMerge> numMergeList = new ArrayList<>();
        NumMerge current = null;
        for (FileForSort fileForSort : fileForSortList) {
            if (null == current) {
                current = new NumMerge(fileForSort);
                continue;
            }
            if (fileForSort.getIndex() == current.getValue()) {
                current.getFileForSortList().add(fileForSort);
                continue;
            }
            numMergeList.add(current);
            current = new NumMerge(fileForSort);
        }
        if (null != current) {
            numMergeList.add(current);
        }
        for (int i = 0; i < numMergeList.size(); i++) {
            List<FileForSort> currentValueList = numMergeList.get(i).getFileForSortList();
            for (FileForSort fileForSort : currentValueList) {
                fileForSort.getFile().renameTo(new File(targetDir, i + SEPARATOR_FOR_JOIN + fileForSort.getName()));
            }
        }
    }

    /**
     * 获得待移动文件
     * @param sourceDir 源文件
     * @throws FileMatchMultipleException
     * @return null-未找到
     */
    private File getFileToMove(File sourceDir) throws FileMatchMultipleException {
        List<File> fileToMoveList = Arrays.asList(sourceDir.listFiles())
                                    .stream()
                                    .filter(file -> file.isFile() && file.getName().startsWith(this.prefix))
                                    .collect(Collectors.toList());
        if (fileToMoveList.isEmpty()) {
            return null;
        }
        if (fileToMoveList.size() > 1) {
            String fileToMoveListStr = fileToMoveList.stream().map(File::getName).collect(Collectors.joining(","));
            throw new FileMatchMultipleException("prefix=" + this.prefix + ",fileToMoveListStr=" + fileToMoveListStr);
        }
        return fileToMoveList.get(0);
    }

    private String moveFileInner(File fileToMove, Integer expectIndex, String expectName, File targetDir) {
        // [文件列表A]
        List<FileForSort> fileList = this.getFileListInTargetDir(targetDir);
        // [文件列表A]中需移动的文件
        List<FileForSort> fileToMoveList = fileList.stream()
                                           .filter(fileForSort -> fileForSort.getIndex() >= expectIndex)
                                           .collect(Collectors.toList());
        // 倒序
        Collections.reverse(fileToMoveList);
        // 待移动文件夹最终确认的index
        Integer finalIndex = fileToMoveList.isEmpty() ? fileList.size() : expectIndex.intValue();
        fileToMoveList.forEach(fileForSort -> fileForSort.getFile().renameTo(new File(targetDir, (fileForSort.getIndex() + 1) + SEPARATOR_FOR_JOIN + fileForSort.getName())));
        String result = finalIndex + SEPARATOR_FOR_JOIN + expectName;
        fileToMove.renameTo(new File(targetDir, result));
        return result;
    }

    /**
     * 获得${targetDir}下的[文件列表A]
     * 该列表按index增序排列
     * @param targetDir 目标文件夹
     * @return [文件列表A]
     */
    private List<FileForSort> getFileListInTargetDir(File targetDir) {
        return Arrays.asList(targetDir.listFiles())
                .stream()
                .filter(file -> file.isFile())
                .map(file -> {
                    String[] nameSplit = file.getName().split(SEPARATOR_FOR_SPLIT, 2);
                    Integer index = null;
                    if (nameSplit.length == 2
                            && StringUtils.isNotBlank(nameSplit[0])
                            && StringUtils.isNumeric(nameSplit[0])) {
                        index = Integer.parseInt(nameSplit[0]);
                    }
                    return new FileForSort(file, index, nameSplit[1]);
                })
                .filter(fileForSort -> null != fileForSort.getIndex()
                        && fileForSort.getIndex() >= 0
                        && StringUtils.isNotBlank(fileForSort.getName()))
                .sorted()
                .collect(Collectors.toList());
    }
}

/**
 * 完成对${targetDir}中的[文件列表A]进行排序的辅助类
 */
@Data
class FileForSort implements Comparable<FileForSort> {

    /**
     * [文件列表A]中的元素
     */
    private File file;

    /**
     * 文件简单名称格式为:[index].[name]
     * 本字段即为其中的[index]
     */
    private Integer index;

    /**
     * 文件简单名称格式为:[index].[name]
     * 本字段即为其中的[name]
     */
    private String name;

    public FileForSort(File file, Integer index, String name) {
        this.file = file;
        this.index = index;
        this.name = name;
    }

    /**
     * 按index排序
     * @param o 待比较的对象
     * @return 遵Comparable规范
     */
    @Override
    public int compareTo(FileForSort o) {
        return this.index.compareTo(o.index);
    }
}

/**
 * 用来将诸如：
 * 1,1,1,2,2,9,9,10
 * 转换为
 * 0,0,0,1,1,2,2,3
 */
@Data
class NumMerge {

    private int value;

    private List<FileForSort> fileForSortList = new ArrayList<>();

    public NumMerge(FileForSort fileForSort) {
        this.value = fileForSort.getIndex();
        this.fileForSortList.add(fileForSort);
    }
}
