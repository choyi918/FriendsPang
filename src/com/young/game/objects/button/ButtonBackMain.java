package com.young.game.objects.button;

import com.young.game.ui.CanvasGameOp;

import java.awt.*;

public class ButtonBackMain extends Button {
    private static ButtonBackMain instance;

    private ButtonBackMain() {
        super("button_back.png", "button_back_pointed.png");
    }

    public static ButtonBackMain getInstance() {
        if (instance == null)
            instance = new ButtonBackMain();
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    @Override
    public void draw(Graphics g) {
        CanvasGameOp observer = CanvasGameOp.getInstance();

        int dw = CanvasGameOp.DW;
        int dh = CanvasGameOp.DH;

        int x = 0 + 2 * dw;
        int y = 0 + 2 * dh;

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
