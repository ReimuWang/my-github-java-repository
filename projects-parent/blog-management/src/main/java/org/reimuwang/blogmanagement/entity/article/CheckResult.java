package org.reimuwang.blogmanagement.entity.article;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 检测结果
 */
@Data
public class CheckResult {

    private CheckResultStatus status;

    private List<String> messageList;

    public CheckResult(CheckResultStatus status, List<String> messageList) {
        this.status = status;
        this.messageList = messageList;
    }

    public static CheckResult success() {
        return new CheckResult(CheckResultStatus.SUCCESS, new ArrayList<>());
    }
}
