package servers.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by 沈小水 on 2018/6/5.
 *
 * 获取数据库连接对象
 *
 */
public class JDBCPool {

    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String url = "jdbc:mysql://127.0.0.1:3306/shenhao?useUnicode=true&useSSL=false&charcterEncoding=UTF-8";
    private static final String user = "root";
    private static final String password = "123456";
    private static Connection connection = null;

    //静态块加载驱动
    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
