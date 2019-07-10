package Utils;


import net.sf.json.JSONObject;
import org.postgresql.core.Encoding;

import java.io.IOException;
import java.sql.*;

import static Utils.ConfigConstants.*;

/**
 * Created by andrei Filip on 7/10/19.
 */
public class DBConnection {
    private static Connection conn = null;

    public DBConnection() {
        conn = connectTODataBase();
    }

    /**
     * Close DB connection
     */
    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
        }

    }

    /**
     * Connect to  DB
     * @return DB connection
     */
    private Connection connectTODataBase() {
        try {
            Class.forName(JDBC_DRIVER);//Register JDBC driver
            return DriverManager.getConnection(DB_URL, USER, PASS);//Open a connection
        } catch (Exception e) {}
        return null;
    }

    /**
     * Execute SQL query
     * @param sql (PreparedStatement) sql query
     * @return (ResultSet) data produced by the query
     */
    private ResultSet executeQuery(PreparedStatement sql) {
        try {
            return sql.executeQuery();
        } catch (SQLException e) {}
        return null;
    }

    /**
     * Executes query and returns result
     * @param key (String) filter key in query
     * @return (JsonObject) query result
     */
    public JSONObject getResult(String key)
    {
        ResultSet rs = null;
        PreparedStatement statement;
        try {
            statement = conn.prepareStatement(SQLConstants.GET_VALUE_FOR_ID);
            statement.setString(1, key);
            rs = executeQuery(statement);

            if (rs != null) {
                while (rs.next()) {
                    String value = Encoding.defaultEncoding().decode(rs.getBytes("value"))
                            .replaceAll("\\{\\{", "{").replaceAll("\\}\\}", "}");
                    return JSONObject.fromObject(
                            value.substring(value.indexOf("{"), value.lastIndexOf("}") + 1));
                }
            }
        } catch (SQLException | IOException ignored) {
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException ignored) {
            }

        }

        return null;
    }



}
