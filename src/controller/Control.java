package controller;

import clients.UI.Backbroad;
import clients.UI.LoginUser;
import clients.UI.ReshapeIcon;
import servers.serverSocket.MyServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * Created by 沈小水 on 2018/6/2.
 */
public class Control {

    public static String PATH = "/my/chat/src/Icon/";

    public static int DEFAULT_PORT = 8888;//默认端口号为 8888

    private JFrame jFrame;
    private final int WIDTH = 520;
    private final int HEIGHT = 300;
    private JButton serverOpenButon;//开启服务端按钮
    private JButton clientButon;//客户端按钮
    private JMenuBar menuBar;//菜单栏
    private JMenu jMenu;
    private JMenuItem setPort;//设置端口
    MyServer server = new MyServer();

    /*
    * 打开服务端按钮事件
    * 不可重复运行
    *
    * */
    Action serverAction = new AbstractAction("服务器", new ImageIcon(PATH + "serverIcon.png")) {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (server.isAlive()) {
                JOptionPane.showMessageDialog(jFrame, "服务端已运行", "消息", JOptionPane.INFORMATION_MESSAGE);
            } else {
                server = new MyServer(DEFAULT_PORT);
                server.start();
            }
        }
    };

    /*
    * 客户端按钮事件
    * 不可重复运行
    *
    * */
    Action clientAction = new AbstractAction("客户端", new ImageIcon(PATH + "clientIcon.png")) {
        @Override
        public void actionPerformed(ActionEvent e) {
            new LoginUser().start();
        }
    };

    public Control() {
        jFrame = new JFrame("主控制台");
        JLabel tip = new JLabel();
        tip.setFont(new Font("楷体", Font.BOLD, 30));
        tip.setText("欢迎使用网络聊天室");
        tip.setForeground(new Color(120, 165, 241));
        tip.setHorizontalAlignment(JLabel.CENTER);//字体居中
        JPanel buttonPanel = new JPanel();//按钮容器
        serverOpenButon = new JButton(serverAction);
        clientButon = new JButton(clientAction);
//        按钮透明
//        serverOpenButon.setContentAreaFilled(false);
//        clientButon.setContentAreaFilled(false);
        buttonPanel.add(serverOpenButon);
        buttonPanel.add(clientButon);
        buttonPanel.setOpaque(false);//设置透明
        //设置背景
//        JLabel l1 = new JLabel(new ImageIcon(PATH + "backbroad.jpg"));
//        l1.setBounds(0, 0, WIDTH, HEIGHT);
        // 把内容窗格转化为JPanel，并且使内容窗格透明
//        JPanel p1 = (JPanel) jFrame.getContentPane();
//        p1.setOpaque(false);
        // 把背景图片添加到分层窗格的最底层作为背景
//        jFrame.getLayeredPane().add(l1, new Integer(Integer.MIN_VALUE));
        Backbroad backbroad = new Backbroad(new ImageIcon(PATH + "backbroad.jpg").getImage());
        backbroad.setLayout(new BorderLayout());
        jFrame.getContentPane().add(backbroad, BorderLayout.CENTER);
        //使关闭按钮无效
        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //设置窗口关闭提示
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //“是”——0，“否”——1，“取消”——2
                int choice = JOptionPane.showConfirmDialog(jFrame, "确定离开？", "提示", JOptionPane.YES_NO_CANCEL_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        //添加菜单
        menuBar = new JMenuBar();
        jMenu = new JMenu("设置");
        setPort = new JMenuItem("端口", new ImageIcon(PATH + "port.png"));
        jMenu.add(setPort);
        menuBar.add(jMenu);
        //添加设置端口事件
        setPort.addActionListener(e -> {
            if (server.isAlive()) {
                JOptionPane.showMessageDialog(jFrame, "服务端已运行,无法修改", "消息", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String port = JOptionPane.showInputDialog(jFrame, "请输入新设置的端口：", "设置", JOptionPane.QUESTION_MESSAGE);
                DEFAULT_PORT = (port != null) ? Integer.parseInt(port) : 8888;
                JOptionPane.showMessageDialog(jFrame, "端口修改为:" + DEFAULT_PORT, "消息", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        jFrame.setJMenuBar(menuBar);
        jFrame.setSize(new Dimension(WIDTH, HEIGHT));//设置窗口大小
        backbroad.add(tip, BorderLayout.CENTER);
        backbroad.add(buttonPanel, BorderLayout.SOUTH);
//        jFrame.getContentPane().setBackground(new Color(204, 235, 251));//背景颜色
        jFrame.setLocationRelativeTo(null);//窗口居中
        jFrame.setResizable(false);//不可最大化
        jFrame.setVisible(true);
    }

    public void setBackBroad() {
        // 设置背景
        try {
            //将背景图裁剪成窗口大小
            new ReshapeIcon(WIDTH, HEIGHT, PATH + "backbroad.jpg").reshape();
        } catch (IOException e) {
            System.out.println("背景设置失败");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new Control();
    }

}
