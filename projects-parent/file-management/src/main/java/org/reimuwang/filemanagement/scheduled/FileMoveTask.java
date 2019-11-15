package org.reimuwang.filemanagement.scheduled;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * sourceDir：源文件夹
 *     其中有一个前缀为markPrefix.num.xxx的文件，其中num为希望插入到targetDir的位置，不妨将其命名为"待移动文件"
 *     以该规则匹配出的待移动文件同一时刻只能有一个，否则不会执行定时移动任务
 * targetDir：目标文件夹，其中的文件格式为：
 *     0.xxx
 *     1.xxx
 *     2.xxx
 *     3.xxx
 *     ...
 *     忽略该目录下的文件夹及不能被该规则选出的文件
 * 将sourceDir中的待移动文件移动至targetDir，同时去掉该文件的markPrefix.前缀。如有必要，targetDir中的原有文件会改名
 */
@Component
@Slf4j
public class FileMoveTask {

    @Value("${reimuwang.fileMove.sourceDir}")
    private String sourceDir;

    @Value("${reimuwang.fileMove.targetDir}")
    private String targetDir;

    @Value("${reimuwang.fileMove.markPrefix}")
    private String markPrefix;

    private static final String SEPARATOR = "\\.";

    /**
     * 文件发生移动：打印日志
     * 无需移动：不打印日志
     * 发生异常以至于无法移动(文件夹不存在，符合规则的待移动文件有多个等)：每隔logErrorDelayTimeSecond秒打印一次异常信息
     */
    @Value("${reimuwang.fileMove.errorLogIntervalSecond}")
    private Integer errorLogIntervalSecond = 5;

    private long lastPringLogTime = System.currentTimeMillis();

    @Scheduled(fixedDelay=500)
    public void moveFile() {
        // 源文件夹
        File sourceDirFile = new File(this.sourceDir);
        if (!this.checkDir(sourceDirFile)) {
            this.printErrorLog("源文件夹" + this.sourceDir + "不存在");
            return;
        }
        // 目标文件夹
        File targetDirFile = new File(this.targetDir);
        if (!this.checkDir(targetDirFile)) {
            this.printErrorLog("目标文件夹" + this.targetDir + "不存在");
            return;
        }
        // 待移动的文件
        File fileToMove = this.getFileToMove(sourceDirFile);
        if (null == fileToMove) {
            return;
        }
        // 待移动文件的新名字和排序
        String oldName = fileToMove.getName();
        String[] oldNameSplit = oldName.split(FileMoveTask.SEPARATOR, 3);
        String errLogForMoveName = "待移动文件名称非法,name=" + fileToMove.getName();
        if (oldNameSplit.length != 3 || StringUtils.isBlank(oldNameSplit[1]) || !StringUtils.isNumeric(oldNameSplit[1])) {
            this.printErrorLog(errLogForMoveName);
            return;
        }
        Integer newIndex = Integer.parseInt(oldNameSplit[1]);
        if (newIndex < 0) {
            this.printErrorLog(errLogForMoveName);
            return;
        }
        // 移动
        String mvNewName = this.moveFile(fileToMove, newIndex, oldNameSplit[2], targetDirFile);
        log.info("文件移动成功,dir:[" + this.sourceDir + "]->[" + this.targetDir + "].name:[" + oldName + "]->[" + mvNewName + "]");
    }

    private String moveFile(File fileToMove, Integer mvNewIndex, String mvOldName, File targetDirFile) {
        List<FileForSort> fileList = Arrays.asList(targetDirFile.listFiles())
                                     .stream()
                                     .filter(file -> file.isFile())
                                     .map(file -> {
                                         String[] oldNameSplit = file.getName().split(FileMoveTask.SEPARATOR, 2);
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
                                                   .filter(fileForSort -> fileForSort.getIndex() >= mvNewIndex)
                                                   .collect(Collectors.toList());
        Integer finalIndex = fileToMoveList.isEmpty() ? fileList.size() : mvNewIndex.intValue();
        fileToMoveList.forEach(fileForSort ->
                               fileForSort.getFile().renameTo(new File(targetDirFile, (fileForSort.getIndex() + 1) + "." + fileForSort.getName())));
        String result = finalIndex + "." + mvOldName;
        fileToMove.renameTo(new File(targetDirFile, result));
        return result;
    }

    private File getFileToMove(File sourceDirFile) {
        List<File> fileToMoveList = Arrays.asList(sourceDirFile.listFiles())
                                          .stream()
                                          .filter(file -> file.isFile() && file.getName().startsWith(this.markPrefix))
                                          .collect(Collectors.toList());
        if (fileToMoveList.isEmpty()) {
            return null;
        }
        if (fileToMoveList.size() > 1) {
            String fileToMoveListStr = fileToMoveList.stream().map(File::getName).collect(Collectors.joining(","));
            this.printErrorLog("匹配到可移动前缀的文件有多个,markPrefix=" + this.markPrefix + ",fileToMoveListStr=" + fileToMoveListStr);
            return null;
        }
        return fileToMoveList.get(0);
    }

    private void printErrorLog(String msg) {
        this.printErrorLog(msg, null);
    }

    private void printErrorLog(String msg, Exception e) {
        long now = System.currentTimeMillis();
        if ((now - this.lastPringLogTime) / 1000 <= this.errorLogIntervalSecond) {
            return;
        }
        if (null == e) {
            log.error(msg);
        } else {
            log.error(msg, e);
        }
        this.lastPringLogTime = now;
    }

    private boolean checkDir(File dir) {
        return dir.exists() && dir.isDirectory();
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