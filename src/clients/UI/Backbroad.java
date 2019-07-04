package clients.UI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by 沈小水 on 2018/6/23.
 * <p>
 * 创建背景图
 */
public class Backbroad extends JPanel {
    Image image;

    public Backbroad(Image image) {
        this.image = image;
        this.setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }

}
