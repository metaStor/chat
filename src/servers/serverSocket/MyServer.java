package servers.serverSocket;

import controller.Control;
import eachOther.Command;
import eachOther.Transporter;
import servers.map.ClientMap;
import servers.map.LoginMap;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by 沈小水 on 2018/6/2.
 */
public class MyServer extends Thread implements ActionListener {

    public static boolean isOpen;//公共接口，查看服务器是否关闭

    private ServerSocket serverSocket;

    private final String PATH = Control.PATH;
    private int DEFAULT_PORT;//端口号
//    private final int timeOut = 1000 * 30; //设置超时

    private JFrame serverFrame;
    private JTextArea showMessage;
    private JLabel portLabel;
    private JLabel iconLabel;
    private JLabel countLabel;
    private JButton staff;

    //服务客户端的线程集合
    private Set<ServerThread> servers = new HashSet<>();

    public MyServer() {
    }

    public MyServer(int DEFAULT_PORT) {
        this.DEFAULT_PORT = DEFAULT_PORT;
        isOpen = true;
    }

    public void init() {
        //初始化界面
        serverFrame = new JFrame("服务器");
        showMessage = new JTextArea(20, 30);
        portLabel = new JLabel("当前端口号为：" + DEFAULT_PORT, new ImageIcon(PATH + "port.png"), JLabel.LEFT);
        iconLabel = new JLabel(new ImageIcon(PATH + "clients.png"), JLabel.LEFT);
        countLabel = new JLabel("0");
        staff = new JButton("全体消息", new ImageIcon(PATH + "staff.png"));
        staff.addActionListener(this);
        showMessage.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "服务器消息"));
        showMessage.setFont(new Font("宋体", Font.BOLD, 15));
        showMessage.setBackground(new Color(255, 255, 255));
        showMessage.setEditable(false);
        showMessage.setText("正等待用户连接...\n");
        //设置布局
        Box vertical = Box.createVerticalBox();
        JPanel jPanel = new JPanel();
        jPanel.add(portLabel);
        jPanel.add(new JLabel("       "));//分隔
        jPanel.add(iconLabel);
        jPanel.add(countLabel);
        vertical.add(jPanel);
        vertical.add(showMessage);
        serverFrame.add(vertical);
        serverFrame.add(staff, BorderLayout.SOUTH);
        serverFrame.setSize(new Dimension(400, 550));
        serverFrame.setResizable(false);
        serverFrame.setVisible(true);
        serverFrame.setLocationRelativeTo(null);
        serverFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        serverFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(serverFrame, "如果关闭服务器，则所有用户将断开连接？", "是否关闭服务器？", JOptionPane.YES_NO_CANCEL_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    serverFrame.dispose();
                    //服务器关闭，拒收客户端消息
                    isOpen = false;
                    for (ServerThread temp : servers) {
                        temp.setRunning(false);
                    }
                    try {
                        serverSocket.close();//强制断开连接
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (LoginMap.getCount() == 0) {
            JOptionPane.showMessageDialog(serverFrame, "无在线用户", "消息", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String content = JOptionPane.showInputDialog(serverFrame, "请输入要发送的消息", "全体成员"
                , JOptionPane.QUESTION_MESSAGE);
        if (content != null) {
            new ToStaffServer(content.trim()).start();
        }
    }

    @Override
    public void run() {
        try {
            init();
            serverSocket = new ServerSocket(DEFAULT_PORT);
            while (true) {
                try {
//                    serverSocket.setSoTimeout(timeOut);//30秒内无用户连接则退出
                    //此线程一直阻塞，直到用户连接
                    Socket socket = serverSocket.accept();
                    //设置为无超时
//                    serverSocket.setSoTimeout(0);
                    //当有客户连接就启动一个线程为他服务
                    ServerThread server = new ServerThread(socket, showMessage, countLabel);
                    servers.add(server);
                    new Thread(server).start();
                }
//                catch (SocketTimeoutException e) {
//                    serverFrame.dispose();
//                    JOptionPane.showMessageDialog(serverFrame, "超过一分钟无用户链接，服务器已断开", "消息", JOptionPane.INFORMATION_MESSAGE);
//                    serverSocket.close();
//                    break;
//                }
                catch (SocketException e1) {
                    JOptionPane.showMessageDialog(serverFrame, "服务器已断开", "消息", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(serverFrame, "服务器初始失败，请检查端口" + DEFAULT_PORT + "是否被占用？"
                    , "消息", JOptionPane.INFORMATION_MESSAGE);
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
