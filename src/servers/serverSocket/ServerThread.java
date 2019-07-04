package servers.serverSocket;

import servers.JDBC.GetNameByqq;
import servers.map.LoginMap;
import user.User;
import eachOther.Command;
import eachOther.Transporter;
import servers.map.ClientMap;
import servers.JDBC.CheckUser;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

/**
 * Created by 沈小水 on 2018/6/5.
 * <p>
 * 一个用户得到一个线程
 */

public class ServerThread implements Runnable {

    private boolean isRunning;

    private JTextArea showMessage;
    private JLabel countLabel;

    private Socket socket;
    private ObjectInputStream reader;
    private ObjectOutputStream write;

    public ServerThread(Socket socket, JTextArea showMessage, JLabel countLabel) {
        this.showMessage = showMessage;
        this.countLabel = countLabel;
        this.socket = socket;
        isRunning = true;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (socket != null && isRunning) {
            try {
                reader = new ObjectInputStream(socket.getInputStream());
                Transporter transporter = (Transporter) reader.readObject();//读取用户的数据
                transporter = dealWith(transporter);//处理消息
                //如果是登录的消息,发送给本人
                if (transporter.getCOMMAND() == Command.LOGIN) {
                    write = new ObjectOutputStream(socket.getOutputStream());
                }
                //如果是要发送消息,获取接受者的socket,对方不在线就发送给自己
                if (transporter.getCOMMAND() == Command.MESSAGE || transporter.getCOMMAND() == Command.CACHE) {
                    if (transporter.isCAN()) {
                        write = new ObjectOutputStream(ClientMap.getSocketByqq(transporter.getRECEIVER()).getOutputStream());
                    } else {
                        write = new ObjectOutputStream(socket.getOutputStream());
                    }
                }
                //如果是下线消息，发送给本人
                if (transporter.getCOMMAND() == Command.EXIT) {
                    write = new ObjectOutputStream(socket.getOutputStream());
                }
                write.writeObject(transporter);
            } catch (IOException e) {
                socket = null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //处理用户发过来的消息
    public Transporter dealWith(Transporter transporter) {
        //如果是登录口令
        if (transporter.getCOMMAND() == Command.LOGIN) {
            //查询用户是否存在数据库
            User user = (User) transporter.getDATA();
            boolean isExist = CheckUser.checkUser(user);
            //查询用户是否已登录
            boolean isLogin = LoginMap.queryUser(user.getQq());
            if (isExist && !isLogin) {
                transporter.setCAN(true);
            } else {
                transporter.setCAN(false);
            }
            if (transporter.isCAN()) {
                //如果存在，加入集合
                ClientMap.addClient(transporter.getSENDER(), socket);
                LoginMap.addUser(user.getQq());
                transporter.setRESULT("登录成功");
                //更新在线用户数量
                countLabel.setText(String.valueOf(ClientMap.getCount()));
                //将登陆成功的用户显示在showMessage
                String UserMessage = "账号:" + user.getQq() + ",名字:" + GetNameByqq.getName(transporter.getSENDER()) + ",ip:" + user.getIp() + "---上线";
                showMessage.append(UserMessage + "\n");
            } else {
                if (!isExist) {
                    transporter.setRESULT("登录失败");
                } else if (isLogin) {
                    transporter.setRESULT("用户已登录");
                }
            }
        }
        //如果是发送消息的口令
        if (transporter.getCOMMAND() == Command.MESSAGE || transporter.getCOMMAND() == Command.CACHE) {
            //直接判断接受者是否在线即可
            if (ClientMap.getSocketByqq(transporter.getRECEIVER()) != null) {
                transporter.setCAN(true);
            } else {
                transporter.setCAN(false);
                transporter.setRESULT("当前用户不在线");
            }
        }
        if (transporter.getCOMMAND() == Command.EXIT) {
            //存在用户就删除并下线
            User user = (User) transporter.getDATA();
            boolean isExist = CheckUser.checkUser(user);
            //查询用户是否已登录
            boolean isLogin = LoginMap.queryUser(user.getQq());
            if (isExist && isLogin) {
                transporter.setCAN(true);
            } else {
                transporter.setCAN(false);
            }
            if (transporter.isCAN()) {
                ClientMap.removeByqq(transporter.getSENDER());
                LoginMap.removeUser(user.getQq());
                transporter.setRESULT("下线成功");
                //更新在线用户数量
                countLabel.setText(String.valueOf(ClientMap.getCount()));
                //将用户下线的信息显示在showMessage
                String UserMessage = "账号:" + user.getQq() + ",名字:" + GetNameByqq.getName(transporter.getSENDER()) + ",ip:" + user.getIp() + "---下线";
                showMessage.append(UserMessage + "\n");
            } else {
                transporter.setRESULT("下线失败");
            }
        }
        return transporter;
    }
}
