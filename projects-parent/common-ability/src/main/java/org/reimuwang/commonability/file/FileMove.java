package org.reimuwang.commonability.file;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileMove {

    private FileMove() {
    }

    /**
     * 不做入参边界检查，由调用者控制
     * 将fileToMove移动到targetDirFile下的expectIndex位置，原有位置若有文件，则后移一个让位
     * 移动后的文件名为： ${expectIndex}.${expectName}
     *
     * 设targetDirFile下有如下内容：
     * 文件夹：ab/
     * 文件： dsd.txt
     * 文件：0.txt
     * 文件：1.txt
     * 文件：2.txt
     *
     * 只取如下格式的文件：数字.xx 忽略文件夹及不符合该规则的文件，则过滤后的文件集合为：
     * 文件：0.txt
     * 文件：1.txt
     * 文件：2.txt
     *
     * 若expectIndex=0，则fileToMove插入到0号位置
     * 若expectIndex=4，则expectIndex置为3，然后插入3号位置
     * 若targetDirFile下无符合规定的文件，则expectIndex=0，随后插入0号位置
     *
     * @param fileToMove
     * @param expectIndex
     * @param expectName
     * @param targetDirFile
     * @return String，生成的新文件名
     */
    public static String moveFile(File fileToMove, Integer expectIndex, String expectName, File targetDirFile) {
        List<FileForSort> fileList = Arrays.asList(targetDirFile.listFiles())
                .stream()
                .filter(file -> file.isFile())
                .map(file -> {
                    String[] oldNameSplit = file.getName().split("\\.", 2);
                    Integer index = null;
                    if (oldNameSplit.length == 2
                            && StringUtils.isNotBlank(oldNameSplit[0])
                            && StringUtils.isNumeric(oldNameSplit[0])) {
                        index = Integer.parseInt(oldNameSplit[0]);
                    }
                    return new FileForSort(file, index, oldNameSplit[1]);
                })
                .filter(fileForSort -> null != fileForSort.getIndex()
                        && fileForSort.getIndex() >= 0
                        && StringUtils.isNotBlank(fileForSort.getName()))
                .sorted()
                .collect(Collectors.toList());
        List<FileForSort> fileToMoveList = fileList.stream()
                .filter(fileForSort -> fileForSort.getIndex() >= expectIndex)
                .collect(Collectors.toList());
        Integer finalIndex = fileToMoveList.isEmpty() ? fileList.size() : expectIndex.intValue();
        fileToMoveList.forEach(fileForSort ->
                fileForSort.getFile().renameTo(new File(targetDirFile, (fileForSort.getIndex() + 1) + "." + fileForSort.getName())));
        String result = finalIndex + "." + expectName;
        fileToMove.renameTo(new File(targetDirFile, result));
        return result;
    }
}

@Data
class FileForSort implements Comparable<FileForSort> {

    private File file;

    private Integer index;

    private String name;

    public FileForSort(File file, Integer index, String name) {
        this.file = file;
        this.index = index;
        this.name = name;
    }

    /**
     * 倒序
     * @param o
     * @return
     */
    @Override
    public int compareTo(FileForSort o) {
        return o.index.compareTo(this.index);
    }
}
