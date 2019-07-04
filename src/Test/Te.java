package Test;

import clients.UI.Backbroad;
import clients.UI.CutImage;
import clients.clientSocket.Client;
import controller.Control;
import user.User;

import javax.swing.*;
import java.awt.*;

/**
 * Created by 沈小水 on 2018/6/23.
 */
public class Te extends JFrame {

    private final String PATH = Control.PATH;
    private final int FRIENDS = 30;//自定义30个好友

    private User user;
    private Client client;

    private JFrame jFrame;
    private JLabel QQhead, Name, Sign;//qq头像、名字以及个性签名
    private JLabel[] Frinds;//qq好友
    private JLabel[] Stranger ;//qq陌生人
    private ImageIcon MyHead, FrindsHead;//我的头像、好友头像
    private JTabbedPane Choose;
    private JScrollPane Friends_scrollPane;//好友列表滑动条
    private JScrollPane Stranger_scrollPane;//陌生人列表滑动条

    public void init() {
        //init
        jFrame = new JFrame("Test");
        MyHead = new ImageIcon(PATH + "QQhead.jpg");
        MyHead.setImage(MyHead.getImage().getScaledInstance(95, 88, Image.SCALE_DEFAULT));
        MyHead = CutImage.AutoCut(MyHead);
        QQhead = new JLabel(MyHead);
        Name = new JLabel("admin");
        Sign = new JLabel("大家好，我是 admin", JLabel.LEFT);
        UIManager.put("TabbedPane.contentOpaque", false);
        Choose = new JTabbedPane();
        Frinds = new JLabel[FRIENDS];
        FrindsHead = new ImageIcon(PATH + "frindHead.jpg");
        FrindsHead.setImage(FrindsHead.getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT));
        Box North = Box.createHorizontalBox();//头像部分
        JPanel north = new JPanel();
        north.setLayout(new GridLayout(2, 1, 0, 10));
        north.setOpaque(false);
//        north.setBackground(new Color(255, 255, 255));
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
//            Frinds[i].setOpaque(false);
//            Frinds[i].setBackground(new Color(255, 255, 255));
//            Frinds[i].addMouseListener(this);
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
        Choose.addTab("我的好友", Friends_scrollPane);
        Choose.addTab("陌生人", Stranger_scrollPane);
        backbroad.add(North);
        backbroad.add(Choose);
        jFrame.setVisible(true);
        jFrame.setSize(new Dimension(320, 666));
        jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Te().init();
    }
}
