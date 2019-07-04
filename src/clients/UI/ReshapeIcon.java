package clients.UI;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by 490G33 on 2018/6/3.
 *
 * 对背景图片进行指定大小的裁剪
 */
public class ReshapeIcon {

    private int WIDTH;
    private int HEIGHT;
    private String target;

    public ReshapeIcon() { }

    public ReshapeIcon(int width, int height, String target) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.target = target;
    }

    public void reshape() throws IOException {
        BufferedImage initImage = ImageIO.read(new File(target));
        if (initImage.getWidth() == WIDTH && initImage.getHeight() == HEIGHT){
            System.out.println("已为适合大小，无需裁剪");
            return;
        }
        BufferedImage resultImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = resultImage.getGraphics();
        graphics.drawImage(initImage, 0, 0, WIDTH, HEIGHT, null);//裁剪
        ImageIO.write(resultImage, "jpg", new File(target));//输出并覆盖源文件
    }

}
