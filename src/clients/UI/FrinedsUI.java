package clients.UI;

import clients.clientSocket.Client;
import controller.Control;
import eachOther.Command;
import eachOther.Transporter;
import servers.JDBC.GetNameByqq;
import user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by 沈小水 on 2018/6/6.
 * <p>
 * qq好友界面
 */
public class FrinedsUI implements MouseListener {

    private final String PATH = Control.PATH;
    private final int FRIENDS = 30;//自定义30个好友

    private User user;
    private Client client;

    private JFrame jFrame;
    private JLabel QQhead, Name, Sign;//qq头像、名字以及个性签名
    private JLabel[] Frinds;//qq好友
    private JLabel[] Stranger;//qq陌生人
    private ImageIcon MyHead, FrindsHead;//我的头像、好友头像
    private JTabbedPane Choose;
    private JScrollPane Friends_scrollPane;//好友列表滑动条
    private JScrollPane Stranger_scrollPane;//陌生人列表滑动条
    private Object previous = null;//保存上一个被点击的好友

    public FrinedsUI() {
    }

    public FrinedsUI(User user, Client client) {
        this.user = user;
        this.client = client;
        init();
    }

    public void init() {
        //init
        String name = GetNameByqq.getName(user.getQq());
        jFrame = new JFrame(name);
        MyHead = new ImageIcon(PATH + "QQhead.jpg");
        MyHead.setImage(MyHead.getImage().getScaledInstance(95, 88, Image.SCALE_DEFAULT));
        MyHead = CutImage.AutoCut(MyHead);
        QQhead = new JLabel(MyHead);
        Name = new JLabel(name);
        Sign = new JLabel("大家好，我是" + name, JLabel.LEFT);
        //利用UIManager类将JTabbedPane设置为透明
        UIManager.put("TabbedPane.contentOpaque", false);
        Choose = new JTabbedPane();
        Frinds = new JLabel[FRIENDS];
        FrindsHead = new ImageIcon(PATH + "frindHead.jpg");
        FrindsHead.setImage(FrindsHead.getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT));
        Box North = Box.createHorizontalBox();//头像部分
        JPanel north = new JPanel();
        north.setLayout(new GridLayout(2, 1, 0, 10));
        north.setOpaque(false);
        north.add(Name);
        north.add(Sign);
        North.add(QQhead);
        North.add(new JLabel("           "));//增加头像与名字的距离，美观
        North.add(north);
        North.setOpaque(false);
// ----------------------------------------------------------
        Box frind_list = Box.createVerticalBox();
        Box stranger_list = Box.createVerticalBox();
        //背景
        Backbroad backbroad = new Backbroad(new ImageIcon(PATH + "FriendBG.jpg").getImage());
        backbroad.setBounds(0, 0, 320, 666);
        backbroad.setLayout(new BoxLayout(backbroad, BoxLayout.Y_AXIS));
        backbroad.setOpaque(false);
        jFrame.getContentPane().add(backbroad, BorderLayout.CENTER);
        for (int i = 0; i < FRIENDS; i++) {
            Frinds[i] = new JLabel("                      " + (i + 10086) + "                                    "
                    , FrindsHead, JLabel.CENTER);
            Frinds[i].setOpaque(false);//设置透明
            Frinds[i].addMouseListener(this);
            frind_list.add(Frinds[i]);
        }
        frind_list.setOpaque(false);
        //好友列表
        Friends_scrollPane = new JScrollPane(frind_list);
        Friends_scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);//设置垂直滚动条总是出现
        Friends_scrollPane.getVerticalScrollBar().setUnitIncrement(10);//设置滑动速度
        //两步设置透明
        Friends_scrollPane.setOpaque(false);
        Friends_scrollPane.getViewport().setOpaque(false);
        //陌生人列表（未添加）
        Stranger_scrollPane = new JScrollPane();
        Stranger_scrollPane.setOpaque(false);
        Stranger_scrollPane.getViewport().setOpaque(false);
        //添加到JTabbedPane
        Choose.addTab("我的好友", Friends_scrollPane);
        Choose.addTab("陌生人", Stranger_scrollPane);
        backbroad.add(North);
        backbroad.add(Choose);
        jFrame.setVisible(true);
        jFrame.setSize(new Dimension(320, 666));
        jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(jFrame, "是否下线？？", "提示", JOptionPane.YES_NO_CANCEL_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    jFrame.dispose();
                    //向服务器发送下线消息
                    Transporter transporter = new Transporter();
                    transporter.setSENDER(user.getQq());
                    transporter.setRECEIVER(user.getQq());
                    transporter.setDATA(user);
                    transporter.setCOMMAND(Command.EXIT);
                    client.sendData(transporter);
                    transporter = client.getData();
                    JOptionPane.showMessageDialog(jFrame, transporter.getRESULT(), "消息", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //如果鼠标点击了1次,此好友变色(特殊色)
        if (e.getClickCount() == 1) {
            if (previous != null) {
                JLabel label = (JLabel) previous;
                label.setOpaque(false);//设置透明
                label.updateUI();//更新label状态
            }
            JLabel label = (JLabel) e.getSource();
            label.setOpaque(true);//设置不透明
            label.updateUI();//更新label状态
            label.setBackground(new Color(216, 217, 213));
            previous = e.getSource();
        }
        //如果鼠标点击了2次,打开好友对话框
        if (e.getClickCount() == 2) {
            JLabel label = (JLabel) e.getSource();
            /* *************此处遇到巨坑**********************
            *  由于之前为了居中显示好友名字，就在label前后注入了许多空格
            *  导致后面ServerThread中的transporter.getRECEIVER()的字符串前后也有大量空格
            *  所以一直导致查询对方失败
            *  这里加个trim()即可
            * */
            new ChatUI(user.getQq(), label.getText().trim(), client);//启动聊天界面
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    //鼠标进入，变色
    @Override
    public void mouseEntered(MouseEvent e) {
        JLabel label = (JLabel) e.getSource();
        Color color = label.getBackground();
        if (color.getRed() == 216 && color.getGreen() == 217 && color.getBlue() == 213) {
            return;
        }
        label.setOpaque(true);//设置不透明
        label.updateUI();//更新label状态
        label.setBackground(new Color(215, 217, 213));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        JLabel label = (JLabel) e.getSource();
        Color color = label.getBackground();
        if (color.getRed() == 216 && color.getGreen() == 217 && color.getBlue() == 213) {
            return;
        }
        label.setOpaque(false);//设置透明
        label.updateUI();//更新label状态
    }

}
