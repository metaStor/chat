package clients.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

/**
 * Created by 沈小水 on 2018/6/23.
 * <p>
 * 根据图片尺寸进行适应性裁剪，也可传入指定大小
 */
public class CutImage {

    public static ImageIcon MakeCut(ImageIcon icon, int width, int height) {
        //这里必须要是正方形，才可得圆形，若是长方形，则得椭圆
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //由边界矩形定义的椭圆
        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, width, height);
        Graphics2D g2 = image.createGraphics();
        g2.fill(new Rectangle(width, height));
        //设置圆形
        g2.setClip(circle);
        g2.drawImage(icon.getImage(), 0, 0, width, height, null);
        g2.dispose();//销毁Graphics2D
        icon.setImage(image);
        return icon;
    }

    public static ImageIcon AutoCut(ImageIcon icon) {
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();
        int scale = (width + height) / 2;
        //这里必须要是正方形，才可得圆形，若是长方形，则得椭圆
        BufferedImage image = new BufferedImage(scale, scale, BufferedImage.TYPE_INT_RGB);
        //由边界矩形定义的椭圆
        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, scale, scale);
        Graphics2D g2 = image.createGraphics();
        g2.fill(new Rectangle(scale, scale));
        //设置圆形
        g2.setClip(circle);
        g2.drawImage(icon.getImage(), 0, 0, scale, scale, null);
        g2.dispose();//销毁Graphics2D
        icon.setImage(image);
        return icon;
    }

}
