package org.reimuwang.commonability.string;

/**
 * 之所以不叫StringUtils是不想和org.apache.commons.lang3.StringUtils重名
 */
public class StringHandler {

    /**
     * 将str中出现的charSequence中的所有char均移除
     * 例如 charSequence = "& <>[]"
     * 则会移除str中出现的所有如下字符：
     * '&', ' ', '<', '>', '[', ']'
     * @param str
     * @param charSequence
     * @return
     */
    public static String remove(String str, String charSequence) {
        if (null == str || str.length() == 0 || null == charSequence || charSequence.length() == 0) {
            return str;
        }
        for (int i = 0; i < charSequence.length(); i++) {
            str = remove(str, charSequence.charAt(i));
            if (null == str || str.length() == 0) {
                break;
            }
        }
        return str;
    }

    public static String remove(String str, char c) {
        if (null == str || str.length() == 0) {
            return str;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                continue;
            }
            result.append(str.charAt(i));
        }
        return result.toString();
    }
    
    public static void main(String[] args) {
        System.out.println(remove("我 啊 <ss> .& 不止[啊", " &<>."));
    }
}
