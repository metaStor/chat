package clients.UI;

import clients.clientSocket.Client;
import controller.Control;
import eachOther.Command;
import eachOther.Transporter;
import javafx.scene.input.KeyCode;
import user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by 沈小水 on 2018/6/5.
 * <p>
 * 登录界面，仿QQ
 * 登录成功后直接打开好友界面
 */
public class LoginUser extends Thread implements ActionListener, MouseListener {

    private final String PATH = Control.PATH;
    private String ip = Client.LOCALHOST;

    private final int WIDTH = 540;
    private final int HEIGHT = 400;

    private JFrame jFrame;
    private ImageIcon QQ_backgroung;
    private JTextField QQ_name;
    private JPasswordField QQ_password;
    private JLabel North, QQ, Password, Apply_QQ, find_QQ;
    private JButton login;
    private JPanel South, inputArea;

    public LoginUser() {

    }

    public void Login() {
        jFrame = new JFrame("登录");
        //qq背景
        QQ_backgroung = new ImageIcon(PATH + "background.jpg");
        QQ_backgroung.setImage(QQ_backgroung.getImage().getScaledInstance(WIDTH, HEIGHT / 2, Image.SCALE_DEFAULT));
        North = new JLabel(QQ_backgroung);
        //输入区
        QQ_name = new JTextField(10);
        QQ_password = new JPasswordField(10);
        QQ = new JLabel("           QQ账号: ");
        QQ.setFont(new Font("宋体", Font.BOLD, 15));
        Password = new JLabel("           QQ密码: ");
        Password.setFont(new Font("宋体", Font.BOLD, 15));
        Apply_QQ = new JLabel("    申请账号");
        Apply_QQ.setFont(new Font("宋体", Font.BOLD, 15));
        Apply_QQ.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//改变光标是手
        find_QQ = new JLabel("    找回密码");
        find_QQ.setFont(new Font("宋体", Font.BOLD, 15));
        find_QQ.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//改变光标是手
        //登录按钮
        ImageIcon buttonIcon = new ImageIcon(PATH + "loginButton.png");
        buttonIcon.setImage(buttonIcon.getImage().getScaledInstance(250, 50, Image.SCALE_DEFAULT));
        login = new JButton();
        login.setIcon(buttonIcon);
        login.setBorderPainted(false);
        login.setBorder(null);//去边框
        login.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//改变光标是手
        South = new JPanel();
        South.add(login);
        //布局
        inputArea = new JPanel(new GridLayout(2, 3, 0, 20));
        inputArea.add(QQ);
        inputArea.add(QQ_name);
        inputArea.add(Apply_QQ);
        inputArea.add(Password);
        inputArea.add(QQ_password);
        inputArea.add(find_QQ);
        //添加事件
        login.addActionListener(this);
        Apply_QQ.addMouseListener(this);
        find_QQ.addMouseListener(this);
        //按钮绑定回车键
//        South.getInputMap().put(KeyStroke.getKeyStroke(String.valueOf(KeyCode.ENTER)), "login");
//        South.getActionMap().put("login", (Action) this);
        jFrame.add(North, BorderLayout.NORTH);
        jFrame.add(inputArea, BorderLayout.CENTER);
        jFrame.add(South, BorderLayout.SOUTH);
        jFrame.setSize(new Dimension(WIDTH, HEIGHT));
        jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jFrame.dispose();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //如果点击了登录
        if (e.getSource() == login) {
            //首先判断账号密码是否为空
            String qq = QQ_name.getText().trim();
            String password = new String(QQ_password.getPassword()).trim();
            if ("".equals(qq) || qq == null) {
                JOptionPane.showMessageDialog(jFrame, "请输入账号！", "消息", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if ("".equals(password) || password == null) {
                JOptionPane.showMessageDialog(jFrame, "请输入密码！", "消息", JOptionPane.WARNING_MESSAGE);
                return;
            }
            //登录成功后
            //实例化用户对象
            User user = new User();
            user.setQq(qq);
            user.setPassword(password);
            user.setIp(ip);
            //实例化传输对象
            Transporter transporter = new Transporter();
            transporter.setDATA(user);//用于将user对象发送给服务器检验
            transporter.setCOMMAND(Command.LOGIN);
            transporter.setSENDER(qq);
            transporter.setRECEIVER(qq);
            //实例化客户端,此客户端登录之后就一直存在
            Client client = new Client();
            client.sendData(transporter);
            transporter = client.getData();
            if (transporter != null) {
                if (transporter.isCAN()) {//如果登录成功
                    JOptionPane.showMessageDialog(jFrame, transporter.getRESULT(), "消息", JOptionPane.INFORMATION_MESSAGE);
                    jFrame.dispose();//关闭登录窗口
                    new FrinedsUI(user, client);//开启好友界面
                } else {
                    JOptionPane.showMessageDialog(jFrame, transporter.getRESULT(), "消息", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == Apply_QQ) {
            browse("https://ssl.zc.qq.com/v3/index-chs.html");
        } else if (e.getSource() == find_QQ) {
            browse("https://aq.qq.com/v2/uv_aq/html/reset_pwd/pc_reset_pwd_input_account.html?v=4.0");
        }
    }

    //用于用浏览器打开网页
    public void browse(String url) {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(new URI(url));//打开网址
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        JLabel label = (JLabel) e.getSource();
        label.setForeground(new Color(7, 188, 252));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        JLabel label = (JLabel) e.getSource();
        label.setForeground(new Color(0, 0, 0));
    }

    @Override
    public void run() {
        Login();
    }
}
