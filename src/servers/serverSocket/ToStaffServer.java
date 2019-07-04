package servers.serverSocket;

import eachOther.Command;
import eachOther.Transporter;
import servers.map.ClientMap;
import servers.map.LoginMap;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by 沈小水 on 2018/6/24.
 * <p>
 * 向在线的用户发送指定消息
 */
public class ToStaffServer extends Thread {

    private String content;

    private ObjectOutputStream write = null;

    public ToStaffServer(String content) {
        this.content = content;
    }

    @Override
    public void run() {
        //向在线的用户依次发送消息
        for (String qq : LoginMap.LoginUsers) {
            //构造传送消息
            Transporter transporter = new Transporter();
            transporter.setCOMMAND(Command.ALL_MESSAGE);
            transporter.setDATA(content);
            transporter.setCAN(true);
            transporter.setSENDER("来自服务器的消息:");
            transporter.setRECEIVER(qq);
            //由qq号获取对应的socket
            try {
                write = new ObjectOutputStream(ClientMap.getSocketByqq(qq).getOutputStream());
                write.writeObject(transporter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
