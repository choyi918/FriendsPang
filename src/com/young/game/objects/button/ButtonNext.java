package com.young.game.objects.button;

import com.young.game.ui.CanvasGameOp;

import java.awt.*;

public class ButtonNext extends Button{
    private int x;
    private int y;
    public ButtonNext() {
        super("button_next_default.png", "button_next_pointed.png");
        x = 0 + 9 * CanvasGameOp.DW;
        y = 0 + 20 * CanvasGameOp.DH + 5 * CanvasGameOp.DH;
    }

    public void update() {
        for (int i = 0; i < 10; i++)
            if (y != 0 + 20 * CanvasGameOp.DH + CanvasGameOp.DH / 2)
                y--;
    }

    @Override
    public void draw(Graphics g) {
        CanvasGameOp observer = CanvasGameOp.getInstance();

        int dw = CanvasGameOp.DW;
        int dh = CanvasGameOp.DH;

        g.setColor(new Color(0xFF, 0xFF, 0xFF, 100));
        g.fillRect(x, y, 4 * dw, dh);

        g.setColor(Color.BLACK);
        if (bPointed)
            g.drawImage(imagePointed, x, y, 4 * dw, dh, observer);
        else
            g.drawImage(imageDefault, x, y, 4 * dw, dh, observer);
    }
}
