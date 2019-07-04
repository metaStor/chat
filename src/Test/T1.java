package Test;

import java.io.IOException;
import java.util.Date;

/**
 * Created by 沈小水 on 2018/6/7.
 * <p>
 * 测试类
 * 测试不同的线程调用ClientMap类hashmap的方法是否会影响其存储
 */
public class T1 {

    public static void main(String[] args) throws IOException, InterruptedException {
        Date date = new Date();
        myMap.addClient("123", date);
        while (true) {
            System.out.println(myMap.getByqq("123"));
            Thread.sleep(1000);
        }
    }
}
