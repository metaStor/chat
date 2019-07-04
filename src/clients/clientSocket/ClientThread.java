package clients.clientSocket;


import eachOther.Command;
import eachOther.Transporter;
import servers.JDBC.GetNameByqq;
import servers.serverSocket.MyServer;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 沈小水 on 2018/6/5.
 * <p>
 * 在双击好友后开启该聊天进程
 * 主要完成接受服务器的消息，并且打印到聊天框
 */
public class ClientThread extends Thread {

    private Client client;
    private boolean isOnline;
    private JTextArea chatTxt;

    public ClientThread(Client client, JTextArea chatTxt) {
        this.client = client;
        this.chatTxt = chatTxt;
        isOnline = true;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        this.isOnline = online;
    }

    //一直阻塞，等待服务器消息
    @Override
    public void run() {
        //如果服务器被关闭，就拒收消息
//        if (!MyServer.isOpen) {
//            return;
//        }
        while (isOnline && MyServer.isOpen) {
            Transporter transporter = client.getData();
            if (transporter != null) {
                //是缓存消息就直接结束
                if (transporter.getCOMMAND() == Command.CACHE) {
                    break;
                }
                //如果对方是在线
                if (transporter.isCAN()) {
                    Date date = new Date();//获取时间
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置时间格式
                    //判断是服务器的消息还是用户的消息
                    String name = (transporter.getSENDER().equals("来自服务器的消息:")) ? "来自服务器的消息:" : GetNameByqq.getName(transporter.getSENDER());
                    String message = name + "   " + format.format(date) + " :\n" + transporter.getDATA();
                    chatTxt.append(message + "\n");
                } else {
                    JOptionPane.showMessageDialog(chatTxt, transporter.getRESULT(), "来自服务器的消息", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
}
