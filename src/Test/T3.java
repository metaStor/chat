package Test;

public class T3 {

    static int a = 3;

    public static void main(String[] args) {
        //can not
        T3 t = new T3();

        //can
        System.out.println(T3.a);
    }
}