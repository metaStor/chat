package servers.JDBC;

import user.User;
import servers.JDBC.JDBCPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by 沈小水 on 2018/6/5.
 * <p>
 * 检查用户在数据库中是否存在
 */
public class CheckUser {

    public static boolean checkUser(User user) {
        Connection connection = JDBCPool.getConnection();//获取数据连接
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT * FROM CLIENTSTABLE WHERE QQ = ? AND PASSWORD = ? AND IP = ?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getQq());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getIp());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;//如果存在用户信息就返回true
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
