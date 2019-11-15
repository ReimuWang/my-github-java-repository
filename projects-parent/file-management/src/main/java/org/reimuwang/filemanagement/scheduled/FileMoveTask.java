package org.reimuwang.filemanagement.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.reimuwang.commonability.file.FileMatchMultipleException;
import org.reimuwang.commonability.file.FileMoveUtils;
import org.reimuwang.commonability.file.IllegalFileNameException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;

@Component
@Slf4j
public class FileMoveTask {

    @Value("${reimuwang.fileMove.sourceDir}")
    private String sourceDir;

    @Value("${reimuwang.fileMove.targetDir}")
    private String targetDir;

    @Value("${reimuwang.fileMove.prefix}")
    private String prefix;

    /**
     * 文件发生移动：打印日志
     * 无需移动：不打印日志
     * 发生异常以至于无法移动(文件夹不存在，符合规则的待移动文件有多个等)：每隔logErrorDelayTimeSecond秒打印一次异常信息
     */
    @Value("${reimuwang.fileMove.errorLogIntervalSecond}")
    private Integer errorLogIntervalSecond = 5;

    private long lastPringLogTime = System.currentTimeMillis();

    private FileMoveUtils fileMoveUtils;

    @PostConstruct
    public void init() {
        this.fileMoveUtils = FileMoveUtils.init(this.sourceDir, this.targetDir, this.prefix).build();
    }

    @Scheduled(fixedDelay=500)
    public void moveFile() {
        String[] result = null;
        try {
            result = this.fileMoveUtils.moveFile();
        } catch (FileNotFoundException e) {
            this.printErrorLog(e);
        } catch (FileMatchMultipleException e1) {
            this.printErrorLog(e1);
        } catch (IllegalFileNameException e2) {
            this.printErrorLog(e2);
        }
        log.info("文件移动成功,dir:[" + this.sourceDir + "]->[" + this.targetDir + "].name:[" + result[0] + "]->[" + result[1] + "]");
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
