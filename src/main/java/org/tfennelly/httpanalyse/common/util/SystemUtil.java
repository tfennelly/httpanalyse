package org.tfennelly.httpanalyse.common.util;

/**
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class SystemUtil {

    private SystemUtil() {
    }

    public static int getPropertyInt(String propertyName, int defaultVal) {
        return (int) getPropertyLong(propertyName, defaultVal);
    }

    public static long getPropertyLong(String propertyName, long defaultVal) {
        String propertyVal = System.getProperty(propertyName);

        if (propertyVal == null) {
            return defaultVal;
        }
        try {
            return Long.parseLong(propertyVal);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
}
