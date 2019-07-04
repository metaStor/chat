package eachOther;

import java.io.Serializable;

/**
 * Created by 沈小水 on 2018/6/5.
 *
 * 服务器与客户端之间交流的数据
 */
public class Transporter implements Serializable {

    private String SENDER;//发送者
    private String RECEIVER;//接受者
    private int COMMAND;//消息的指令
    private Object DATA;//传输的数据
    private boolean CAN;//是否能发送消息，如果对方不在线就不能
    private String RESULT;//处理结果

    public String getSENDER() {
        return SENDER;
    }

    public String getRECEIVER() {
        return RECEIVER;
    }

    public int getCOMMAND() {
        return COMMAND;
    }

    public Object getDATA() {
        return DATA;
    }

    public boolean isCAN() {
        return CAN;
    }

    public String getRESULT() {
        return RESULT;
    }

    public void setSENDER(String SENDER) {
        this.SENDER = SENDER;
    }

    public void setRECEIVER(String RECEIVER) {
        this.RECEIVER = RECEIVER;
    }

    public void setCOMMAND(int COMMAND) {
        this.COMMAND = COMMAND;
    }

    public void setDATA(Object DATA) {
        this.DATA = DATA;
    }

    public void setCAN(boolean CAN) {
        this.CAN = CAN;
    }

    public void setRESULT(String RESULT) {
        this.RESULT = RESULT;
    }
}
