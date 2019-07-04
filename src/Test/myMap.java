package Test;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by 沈小水 on 2018/6/7.
 */
public class myMap {
    private static HashMap<String, Date> map =new HashMap<>();

    //根据qq查找socket
    public static Date getByqq(String qq) {
        return map.get(qq);
    }

    //实现put，其中value不重复的
    public static void addClient(String qq, Date date) {
        map.put(qq, date);
    }

    //根据qq删除
    public static void removeByqq(String qq) {
        map.remove(qq);
    }

    //统计人数
    public static int getCount() {
        return map.size();
    }
}
