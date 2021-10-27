package com.young.game.objects.button;

import com.young.game.ui.CanvasGameOp;

import java.awt.*;

public class ButtonBackMain extends Button {
    private int x;
    private int y;
    private Canvas observer;

    public ButtonBackMain(int x, int y, Canvas observer) {
        super("button_back.png", "button_back_pointed.png");
        this.x = x;
        this.y = y;
        this.observer = observer;
    }

    @Override
    public void draw(Graphics g) {
        int dw = CanvasGameOp.DW;
        int dh = CanvasGameOp.DH;

        int w = 4 * dw;
        int h = 1 * dh;

        g.setColor(new Color(0xFF, 0xFF, 0xFF, 100));
        g.fillRect(x, y, w, h);
        g.setColor(Color.BLACK);

        if (bPointed)
            g.drawImage(imagePointed, x, y, w, h, observer);
        else
            g.drawImage(imageDefault, x, y, w, h, observer);
    }
}
