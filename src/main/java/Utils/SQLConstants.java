package Utils;

/**
 * Created by andrei Filip on 7/10/19.
 */
public class SQLConstants {

    private static final String PROP_FILE_PATH = PropertiesUtil.setConfigPath("sql.properties");
    public static final ReplacementProperties PROPERTIES = PropertiesUtil.loadProperties(PROP_FILE_PATH);

    public static final String GET_VALUE_FOR_ID = PropertiesUtil.getString(PROPERTIES, "get_value_for_id");
}
