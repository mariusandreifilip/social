package Utils;

/**
 * Created by andrei Filip on 7/10/19.
 */

public class ConfigConstants {
    private static final String PROP_FILE_PATH = PropertiesUtil.setConfigPath("config.properties");
    public static final ReplacementProperties PROPERTIES = PropertiesUtil.loadProperties(PROP_FILE_PATH);

    public static final String STAGE1_TOKEN = PropertiesUtil.getString(PROPERTIES, "stage1token");

    public static final boolean SHOW_TEST_RESULTS = PropertiesUtil.getBoolean(PROPERTIES, "showTestResults");
    public static final String[] SKIPPED_TESTS = PropertiesUtil.getString(PROPERTIES, "skippedTests").split(",");



    //DB

    public static final String JDBC_DRIVER = PropertiesUtil.getString(PROPERTIES, "jdbc_drive");
    public static final String DB_URL = PropertiesUtil.getString(PROPERTIES, "DB_URL");
    public static final String USER = PropertiesUtil.getString(PROPERTIES, "USER");
    public static final String PASS  = PropertiesUtil.getString(PROPERTIES, "PASS");











}
