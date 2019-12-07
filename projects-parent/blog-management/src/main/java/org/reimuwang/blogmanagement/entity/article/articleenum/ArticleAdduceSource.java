package org.reimuwang.blogmanagement.entity.article.articleenum;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 文章引用来源
 */
public enum ArticleAdduceSource {

    INTERIOR("内部", 1)
    , WEB("网络", 2)
    , UNKNOWN("未知", 0);

    private String desc;

    private Integer index;

    private static Map<Integer, ArticleAdduceSource> INDEX_MAP = new HashMap<>();

    private static List<Integer> INDEX_LIST = new ArrayList<>();

    public static String SHOW;

    static {
        List<String> showList = new ArrayList<>();
        for (ArticleAdduceSource e : ArticleAdduceSource.values()) {
            INDEX_MAP.put(e.index, e);
            INDEX_LIST.add(e.index);
            showList.add(e.toString());
        }
        SHOW = showList.stream()
                       .map(index -> index + "")
                       .collect(Collectors.joining(",", "[", "]"));
    }

    ArticleAdduceSource(String desc, Integer index) {
        this.desc = desc;
        this.index = index;
    }

    public static ArticleAdduceSource getEnumByIndex(Integer indexToSearch) {
        return INDEX_MAP.get(indexToSearch);
    }

    public static boolean effective(Integer indexToCheck) {
        return INDEX_MAP.containsKey(indexToCheck);
    }

    public String getDesc() {
        return desc;
    }

    public Integer getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return this.name() + "(" + desc + "," + index + ")";
    }
}
