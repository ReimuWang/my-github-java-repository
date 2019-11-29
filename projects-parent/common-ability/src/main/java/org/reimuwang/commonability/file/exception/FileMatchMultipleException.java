package org.reimuwang.commonability.file.exception;

/**
 * 当规则匹配到多个文件时抛出本异常
 */
public class FileMatchMultipleException extends Exception {

    public FileMatchMultipleException(String message) {
        super(message);
    }
}
