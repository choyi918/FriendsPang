package com.young.game.objects.others;

import com.young.game.ui.CanvasMain;

import java.awt.*;

public class ImageMain {
    private Image image;

    public ImageMain() {
        image = Toolkit.getDefaultToolkit().getImage("res/images/main.png");
    }

    public void update() {
    }

    public void draw(Graphics g) {
        CanvasMain observer = CanvasMain.getInstance();

        int obW = observer.getWidth();
        int obH = observer.getHeight();

        int dw = obW / 22;
        int dh = obH / 22;

        int x = 0 + dw;
        int y = 0 + dh;

        int w = 20 * dw;
        int h = 12 * dh;

        g.drawImage(image, x, y, w, h, observer);
    }
}
