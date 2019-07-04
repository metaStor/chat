package servers.map;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by 沈小水 on 2018/6/24.
 * <p>
 * 保存已登录用户的集合
 */
public class LoginMap {

    public static Set<String> LoginUsers = new HashSet<>();

    //添加
    public static void addUser(String qq) {
        LoginUsers.add(qq);
    }

    //删除
    public static void removeUser(String qq) {
        LoginUsers.remove(qq);
    }

    //查询
    public static boolean queryUser(String qq) {
        return LoginUsers.contains(qq);
    }

    //在线个数
    public static int getCount() {
        return LoginUsers.size();
    }
}
