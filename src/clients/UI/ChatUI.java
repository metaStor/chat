package clients.UI;

import clients.clientSocket.Client;
import clients.clientSocket.ClientThread;
import controller.Control;
import eachOther.Command;
import eachOther.Transporter;
import servers.JDBC.GetNameByqq;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 沈小水 on 2018/6/6.
 */
public class ChatUI extends JFrame implements ActionListener {

    private final String PATH = Control.PATH;

    private Client client;//客户端
    private ClientThread clientThread;//客户端服务线程
    private String myQQ;//本人qq
    private String friendQQ;//好友qq
    private String name;//本人名字
    private String friend;//好友名字

    private JTextArea chatTxt;
    private JTextArea inputTxt;
    private JButton send;

    public ChatUI(String myQQ, String friendQQ, Client client) {
        this.client = client;
        this.myQQ = myQQ;
        this.friendQQ = friendQQ;
        name = GetNameByqq.getName(myQQ);//从数据库中获取名字
        friend = GetNameByqq.getName(friendQQ);
        setTitle(name + "与" + friend + "聊天");
        init();
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        //启动客户端线程
        clientThread = new ClientThread(client, chatTxt);
        clientThread.start();
    }

    public void init() {
        //init
        chatTxt = new JTextArea(15, 28);
        inputTxt = new JTextArea(2, 28);
        send = new JButton("发送", new ImageIcon(PATH + "sendIcon.png"));
        chatTxt.setEditable(false);
        chatTxt.setFont(new Font("宋体", Font.BOLD, 20));
        inputTxt.setFont(new Font("宋体", Font.BOLD, 15));
        //注册事件
        send.addActionListener(this);
        //布局
        Box v = Box.createVerticalBox();
        JPanel input = new JPanel();
        input.add(inputTxt);
        input.add(send);
        v.add(new JScrollPane(chatTxt));
        v.add(input);
        add(v);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                clientThread.setOnline(false);//标志关闭聊天
                /*
                * 问题：关闭聊天窗口后，再次打开，会出现前几次发送发送消息无效的情况
                * 解决：发送缓存消息，用于将缓存的后台线程给结束掉
                * */
                Transporter transporter = new Transporter();
                transporter.setSENDER(myQQ);
                transporter.setRECEIVER(friendQQ);
                transporter.setCOMMAND(Command.CACHE);
                client.sendData(transporter);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //如果点击了发送按钮且不为空时,就向服务器发送消息
        if (e.getSource() == send && !"".equals(inputTxt.getText())) {
            Date date = new Date();//获取时间
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置时间格式
            String message = name + "   " + format.format(date) + " :\n" + inputTxt.getText();
            chatTxt.append(message + "\n");
            Transporter transporter = new Transporter();
            //构造初始数据
            transporter.setSENDER(myQQ);
            transporter.setRECEIVER(friendQQ);
            transporter.setCOMMAND(Command.MESSAGE);
            transporter.setDATA(inputTxt.getText());
            //向服务端发送数据
            client.sendData(transporter);
            //发送之后清空输入框
            inputTxt.setText("");
        }
    }
}
