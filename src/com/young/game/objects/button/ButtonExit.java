package com.young.game.objects.button;

import com.young.game.ui.CanvasMain;

import java.awt.*;

public class ButtonExit extends Button{
    private static ButtonExit instance;

    private ButtonExit() {
        super("button_exit.png","button_exit_pointed.png");
    }

    public static ButtonExit getInstance() {
        if (instance == null)
            instance = new ButtonExit();
        return instance;
    }

    @Override
    public void draw(Graphics g) {
        CanvasMain observer = CanvasMain.getInstance();

        int dw = CanvasMain.DW;
        int dh = CanvasMain.DH;

        int x = 0 + 7 * dw;
        int y = 0 + 19 * dh;

        int w = 8 * dw;
        int h = 1 * dh;

        if (bPointed) {
            g.setColor(new Color(0xFF, 0xFF, 0xFF, 100));
            g.fillRect(x, y, w, h);
            g.drawImage(imagePointed, x, y, w, h, observer);
            g.setColor(Color.black);
        }
        else {
            g.setColor(new Color(0xFF, 0xFF, 0xFF, 100));
            g.fillRect(x, y, w, h);
            g.drawImage(imageDefault, x, y, w, h, observer);
            g.setColor(Color.black);
        }
    }
}
