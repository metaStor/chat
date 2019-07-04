package servers.map;

import java.net.Socket;
import java.util.*;

/**
 * Created by 490G33 on 2018/6/4.
 * <p>
 * 登录用户集合
 * 保存登录成功的qq和socket
 */
public class ClientMap {

    private static HashMap<String, Socket> clients = new HashMap<>();

    //根据qq查找socket
    public static Socket getSocketByqq(String qq) {
        return clients.get(qq);
    }

    //根据socket查找qq
    public static String getQQBySocket(Socket socket) {
        for (String qq : clients.keySet()) {
            if (clients.get(qq) == socket || clients.get(qq).equals(socket)) {
                return qq;
            }
        }
        return null;
    }

    //实现put，其中value不重复的
    public static void addClient(String qq, Socket socket) {
        clients.put(qq, socket);
    }

    //根据qq删除
    public static void removeByqq(String qq) {
        clients.remove(qq);
    }

    //统计人数
    public static int getCount() {
        return clients.size();
    }

}
