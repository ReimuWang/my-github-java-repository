package org.reimuwang.blogmanagement.entity.article.articleenum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文章引用状态
 */
public enum ArticleAdduceStatus {

    ACCESSIBLE("可访问", 1)
    , INACCESSIBLE("不可访问", -1)
    , UNKNOWN("未知", 0);

    private String desc;

    private Integer index;

    private static Map<Integer, ArticleAdduceStatus> INDEX_MAP = new HashMap<>();

    private static List<Integer> INDEX_LIST = new ArrayList<>();

    public static String SHOW;

    static {
        List<String> showList = new ArrayList<>();
        for (ArticleAdduceStatus e : ArticleAdduceStatus.values()) {
            INDEX_MAP.put(e.index, e);
            INDEX_LIST.add(e.index);
            showList.add(e.toString());
        }
        SHOW = showList.stream()
                .map(index -> index + "")
                .collect(Collectors.joining(",", "[", "]"));
    }

    ArticleAdduceStatus(String desc, Integer index) {
        this.desc = desc;
        this.index = index;
    }

    public static ArticleAdduceStatus getEnumByIndex(Integer indexToSearch) {
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
