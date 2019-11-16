package org.reimuwang.filemanagement.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.reimuwang.commonability.file.FileMoveUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class FileMoveTask {

    @Value("${reimuwang.fileMove.sourceDirs}")
    private String sourceDirs;

    @Value("${reimuwang.fileMove.targetDirs}")
    private String targetDirs;

    @Value("${reimuwang.fileMove.prefix}")
    private String prefix;

    private String[] sourceDirArray;

    private String[] targetDirArray;

    /**
     * 文件发生移动：打印日志
     * 无需移动：不打印日志
     * 发生异常以至于无法移动(文件夹不存在，符合规则的待移动文件有多个等)：每隔logErrorDelayTimeSecond秒打印一次异常信息
     */
    @Value("${reimuwang.fileMove.errorLogIntervalSecond}")
    private Integer errorLogIntervalSecond = 5;

    private long lastPringLogTime = System.currentTimeMillis();

    private List<FileMoveUtils> fileMoveUtilsList = new ArrayList<>();

    @PostConstruct
    public void init() {
        String separator = ";";
        this.sourceDirArray = this.sourceDirs.split(separator);
        this.targetDirArray = this.targetDirs.split(separator);
        if (this.sourceDirArray.length != this.targetDirArray.length) {
            throw new IllegalArgumentException(this.sourceDirs + "与" + this.targetDirs + "按" + separator + "切分出的个数不一致");
        }
        for (int i = 0; i < this.sourceDirArray.length; i++) {
            this.fileMoveUtilsList.add(FileMoveUtils.init(this.sourceDirArray[i], this.targetDirArray[i], this.prefix).build());
        }
    }

    @Scheduled(fixedDelay=500)
    public void moveFile() {
        for (int i = 0; i < this.fileMoveUtilsList.size(); i++) {
            String[] result = null;
            try {
                result = this.fileMoveUtilsList.get(i).moveFile();
            } catch (Exception e) {
                this.printErrorLog(e);
            }
            if (null == result) {
                continue;
            }
            log.info("文件移动成功,dir:[" + this.sourceDirArray[i] + "]->[" + this.targetDirArray[i] + "].name:[" + result[0] + "]->[" + result[1] + "]");
        }
    }

    private void printErrorLog(Exception e) {
        this.printErrorLog("", e);
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
}
