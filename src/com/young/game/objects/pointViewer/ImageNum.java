package com.young.game.objects.pointViewer;

import com.young.game.ui.CanvasMain;

import java.awt.*;

public class ImageNum {
    private Image imageNumSprite;
    private int index;
    private int sy1;

    private static final int DW;
    private static final int DH;
    private static final int SPEED;

    static {
        DW = 50;
        DH = 70;
        SPEED = 10;
    }


    public ImageNum(int index) {
        imageNumSprite = Toolkit.getDefaultToolkit().getImage("res/images/nums_50_700.png");
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void update() {
        if (sy1 != 0 + index * DH)
            for (int i = 0; i < SPEED; i++) {
                sy1 += 1;
                sy1 %= 700;
            }
    }

    public void draw(int x, int y, int w, int h, Graphics g) {
        CanvasMain observer = CanvasMain.getInstance();

        int sx1 = 0;
        int sx2 = DW;
        int sy2 = sy1 + DH;

        g.drawImage(imageNumSprite,
                x, y, x + w, y + h,
                sx1, sy1, sx2, sy2,
                observer);
    }
}
