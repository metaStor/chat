package Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 沈小水 on 2018/6/7.
 */
public class T2 {

    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        Runnable runnable = () -> {
            for (int i=0;i<50;i++) {
                System.out.println(Thread.currentThread().getName()+" "+i);
                if (i == 10) {
                    try {
                        service.shutdownNow();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        service.submit(runnable);
        service.submit(runnable);

//        System.out.println(myMap.getByqq("123"));//null
        /*
        * 不同的线程调用ClientMap类会影响其存储
        *
        * 经查证，只需将要存储的对象实体化
        * 换言之，写一个实体类来建立用户qq与用户Socket之间的联系
        * */
    }
}
