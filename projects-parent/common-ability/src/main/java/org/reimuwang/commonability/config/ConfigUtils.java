package org.reimuwang.commonability.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * 提供通过静态方法获得配置文件(.properties)中字段的能力
 *
 * 若要使用本类，欲解析的根配置文件需满足：
 * 1.必须名为application.properties，注意:不能使用.yaml
 * 2.必须置于sysPath的根目录下
 */
public class ConfigUtils {

    /**
     * 懒加载
     * 即，若不调用本类的静态功能方法，则不会初始化
     */
    private static PropertiesConfiguration CONFIG;

    public static int getInt(String key) throws ConfigurationException {
        checkAndSetConfig();
        return CONFIG.getInt(key);
    }

    public static int getInt(String key, int defaultValue) throws ConfigurationException {
        checkAndSetConfig();
        return CONFIG.getInt(key, defaultValue);
    }

    public static String getString(String key) throws ConfigurationException {
        checkAndSetConfig();
        return CONFIG.getString(key);
    }

    public static String getString(String key, String defaultValue) throws ConfigurationException {
        checkAndSetConfig();
        return CONFIG.getString(key, defaultValue);
    }

    private static void checkAndSetConfig() throws ConfigurationException {
        if (null == CONFIG) {
            CONFIG = new PropertiesConfiguration("application.properties");
        }
    }
}
