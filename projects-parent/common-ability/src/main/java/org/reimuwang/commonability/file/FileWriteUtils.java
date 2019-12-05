package org.reimuwang.commonability.file;

import java.io.FileWriter;
import java.io.IOException;

public class FileWriteUtils {

    /**
     * content写入到path文件中，若文件不存在则新建
     * 若已有内容则覆盖
     * @param path
     * @param content
     * @throws IOException
     */
    public static void writeByChar(String path, String content) throws IOException {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(content);
            writer.flush();
        }
    }

    /**
     * 将content写入到path文件中，若文件不存在则新建
     * @param path
     * @param content
     * @param append true-追加到末尾,false-写入到开头
     * @throws IOException
     */
    public static void writeByChar(String path, String content, boolean append) throws IOException {
        try (FileWriter writer = new FileWriter(path, append)) {
            writer.write(content);
            writer.flush();
        }
    }
}
