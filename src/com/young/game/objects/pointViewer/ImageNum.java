package com.young.game.objects.pointViewer;

import com.young.game.ui.CanvasGameOp;
import com.young.game.ui.CanvasMain;

import java.awt.*;

public class ImageNum {
    private Image imageNumSprite;
    private int dw;
    private int dh;
    private int index;
    private int speed;

    private int sy1;

    public ImageNum(int index) {
        imageNumSprite = Toolkit.getDefaultToolkit().getImage("res/images/nums_50_700.png");
        dw = 50;
        dh = 70;
        speed = 10;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void update() {
        if (sy1 != 0 + index * dh)
            for (int i = 0; i < 10; i++) {
                sy1 += 1;
                sy1 %= 700;
            }
    }

    public void draw(int x, int y, int w, int h, Graphics g) {
        CanvasMain observer = CanvasMain.getInstance();

        int sx1 = 0;
        int sx2 = dw;
        int sy2 = sy1 + dh;

        g.drawImage(imageNumSprite,
                x, y, x + w, y + h,
                sx1, sy1, sx2, sy2,
                observer);
    }
}
