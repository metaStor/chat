package user;

import java.io.Serializable;

/**
 * Created by 沈小水 on 2018/6/2.
 * <p>
 * 用户信息类
 */
public class User implements Serializable{

    private String name;//名字
    private String qq;//账号
    private String password;//密码
    private String ip;

    public User() {
    }

    public User(String name, String qq, String password, String ip) {
        this.name = name;
        this.qq = qq;
        this.password = password;
        this.ip = ip;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

}
