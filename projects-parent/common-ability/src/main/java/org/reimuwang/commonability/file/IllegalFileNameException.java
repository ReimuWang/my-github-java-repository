package org.reimuwang.commonability.file;

/**
 * 文件名非法时抛出本异常
 */
public class IllegalFileNameException extends Exception {

    public IllegalFileNameException(String message) {
        super(message);
    }
}
