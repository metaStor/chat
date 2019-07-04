package eachOther;

/**
 * Created by 沈小水 on 2018/6/5.
 */
public interface Command {

    int LOGIN = 1;//登录
    int MESSAGE = 2;//消息
    int EXIT = -1;//退出登录
    int CACHE = 3;//缓存消息（发送的缓存消息）
    int ALL_MESSAGE = 4;//全体消息
}
