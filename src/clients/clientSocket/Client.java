package clients.clientSocket;

import controller.Control;
import eachOther.Command;
import eachOther.Transporter;
import servers.serverSocket.MyServer;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by 沈小水 on 2018/6/2.
 */
public class Client {

    public static String LOCALHOST = "127.0.0.1";
    private int DEFAULT_PORT = Control.DEFAULT_PORT;//默认端口号为服务器对应端口号，8888

    private Socket socket;

    public Client() {
        try {
            socket = new Socket(LOCALHOST, DEFAULT_PORT);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "服务未开启");
        }
    }

    //向服务器发送消息
    public void sendData(Transporter transporter) {
        //如果服务器被关闭，就拒发消息
//        if (!MyServer.isOpen) {
//            JOptionPane.showMessageDialog(null, "服务器已关闭，无法发送消息", "消息", JOptionPane.INFORMATION_MESSAGE);
//            return;
//        }
        if (socket == null) {
            return;
        }
        ObjectOutputStream writer = null;
        try {
            writer = new ObjectOutputStream(socket.getOutputStream());
            writer.writeObject(transporter);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "服务未开启");
        }
    }

    //从服务器获取消息
    public Transporter getData() {
        if (socket == null) {
            return null;
        }
        Transporter transporter = null;
        ObjectInputStream reader = null;
        try {
            reader = new ObjectInputStream(socket.getInputStream());
            transporter = (Transporter) reader.readObject();
        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
            return null;
        }
        return transporter;
    }

}
