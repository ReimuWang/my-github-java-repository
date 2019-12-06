package org.reimuwang.testspring.test;

public class Test2 {

    public static void main(String[] args) {
        String content = "![0.jpg](/images/blog_pic/Java 设计模式/2Adapter模式/0.jpg)";
        System.out.println(content.startsWith("!["));
        System.out.println(content.endsWith(")"));
        String[] array = content.split("]");
        System.out.println(array.length);
        System.out.println(array[0].substring(2));
        System.out.println(array[1]);
    }
}
