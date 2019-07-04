package servers.JDBC;

import user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by 沈小水 on 2018/6/7.
 * <p>
 * 通过qq获取用户的名字
 */
public class GetNameByqq {

    public static String getName(String qq) {
        Connection connection = JDBCPool.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT * FROM CLIENTSTABLE WHERE QQ = ?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, qq);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
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
        return "";
    }
}
