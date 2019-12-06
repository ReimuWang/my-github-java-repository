package org.reimuwang.commonability.file;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileIOUtils {

    /**
     * 按行读取文件
     * @param path
     * @return
     */
    public static List<String> readByLine(String path) throws IOException {
        FileInputStream inputStream = new FileInputStream(path);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        List<String> result = new ArrayList<>();
        try {
            String str;
            while((str = bufferedReader.readLine()) != null) {
                result.add(str);
            }
        } finally {
            bufferedReader.close();
            inputStream.close();
        }
        return result;
    }

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
