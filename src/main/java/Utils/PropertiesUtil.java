package Utils;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Properties;


public class PropertiesUtil {
    private static final Logger log = Logger.getLogger(PropertiesUtil.class);

    public static String getString(Properties properties, String key) {
        return getString(properties, key, null);
    }

    private static String getString(Properties properties, String key,
                                    String defaultValue) {
        String value = properties.getProperty(key);

        return (isNullOrEmpty(value) ? defaultValue : value.trim());
    }

    public static String getRequiredString(Properties properties, String key) {
        String value = getString(properties, key);

        if (isNullOrEmpty(value)) {
            throw new IllegalArgumentException(getErrMsg(key));
        }

        return value;
    }

    public static Boolean getBoolean(Properties properties, String key) {
        String value = getString(properties, key);

        return isNullOrEmpty(value) ? null : Boolean.valueOf(value);
    }

    public static boolean getOptionalBoolean(Properties properties, String key,
                                             boolean defaultValue) {
        Boolean value = getBoolean(properties, key);

        if (value == null) {
            return defaultValue;
        }

        return value;
    }

    public static boolean getRequiredBoolean(Properties properties, String key) {
        Boolean value = getBoolean(properties, key);

        if (value == null) {
            throw new IllegalArgumentException(getErrMsg(key));
        }

        return value;
    }

    public static Integer getInteger(Properties properties, String key) {
        String value = getString(properties, key);

        return isNullOrEmpty(value) ? null : Integer.valueOf(value);
    }

    public static int getOptionalInt(Properties properties, String key,
                                     int defaultValue) {
        Integer value = getInteger(properties, key);

        if (value == null) {
            return defaultValue;
        }

        return value;
    }

    public static int getRequiredInt(Properties properties, String key) {
        Integer value = getInteger(properties, key);

        if (value == null) {
            throw new IllegalArgumentException(getErrMsg(key));
        }

        return value;
    }

    private static Long getLong(Properties properties, String key) {
        String value = getString(properties, key);

        return isNullOrEmpty(value) ? null : Long.valueOf(value);
    }

    public static long getOptionalLong(Properties properties, String key,
                                       long defaultValue) {
        Long value = getLong(properties, key);

        if (value == null) {
            return defaultValue;
        }

        return value;
    }

    public static long getRequiredLong(Properties properties, String key) {
        Long value = getLong(properties, key);

        if (value == null) {
            throw new IllegalArgumentException(getErrMsg(key));
        }

        return value;
    }

    private static Float getFloat(Properties properties, String key) {
        String value = getString(properties, key);

        return isNullOrEmpty(value) ? null : Float.valueOf(value);
    }

    public static float getOptionalFloat(Properties properties, String key,
                                         float defaultValue) {
        Float value = getFloat(properties, key);

        if (value == null) {
            return defaultValue;
        }

        return value;
    }

    public static float getRequiredFloat(Properties properties, String key) {
        Float value = getFloat(properties, key);

        if (value == null) {
            throw new IllegalArgumentException(getErrMsg(key));
        }

        return value;
    }

    private static File getFile(Properties properties, String key) {
        String value = getString(properties, key);

        return value == null ? null : new File(value);
    }

    public static File getRequiredFile(Properties properties, String key) {
        File value = getFile(properties, key);

        if (value == null) {
            throw new IllegalArgumentException(getErrMsg(key));
        }

        return value;
    }

    public static String[] getMultiValueString(Properties properties,
                                               String key, String delimiterRegex) {
        String combinedValue = getString(properties, key);
        String[] parsedValue = null;

        if (combinedValue != null) {
            parsedValue = combinedValue.split(delimiterRegex);
        }

        return parsedValue;
    }

    /**
     * Creates a ReplacementProperties object based on the configuration file
     * path
     *
     * @param inputFilePath
     * @return The ReplacementProperties object
     */
    public static ReplacementProperties loadProperties(String inputFilePath) {
        ReplacementProperties properties = null;
        try {

            properties = new ReplacementProperties(new File(inputFilePath));
        }
        catch (IOException ioe) {
            log.error(ioe.getMessage());
        }
        return properties;
    }

    /**
     * Reads a system property to setup environment
     *
     * @return Path to the config file that is specific for that environment
     */
    public static String setConfigPath(String propertFileName ) {
        String propFilePath;

        propFilePath = "src" + File.separator + "main" + File.separator
                + "resources" + File.separator + propertFileName;
        return propFilePath;
    }

    private static String getErrMsg(String key) {
        return "No entry for key '" + key + "'";
    }

    private static boolean isNullOrEmpty(String string) {
        return ((string == null) || (string.trim().length() == 0));
    }

}
