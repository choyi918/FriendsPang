package com.young.game.objects.button;

import com.young.game.ui.CanvasMain;

import java.awt.*;

public class ButtonGameStart extends Button{
    private static ButtonGameStart instance;

    private ButtonGameStart() {
        super("button_game_start.png", "button_game_start_pointed.png");
    }

    public static ButtonGameStart getInstance() {
        if (instance == null)
            instance = new ButtonGameStart();
        return instance;
    }

    @Override
    public void draw(Graphics g) {
        CanvasMain observer = CanvasMain.getInstance();

        int obW = observer.getWidth();
        int obH = observer.getHeight();

        int dw = obW / 22;
        int dh = obH / 22;

        int x = 0 + 7 * dw;
        int y = 0 + 15 * dh;

        int w = 8 * dw;
        int h = 1 * dh;

        if (bPointed) {
            g.setColor(new Color(0xFF, 0xFF, 0xFF, 100));
            g.fillRect(x, y, w, h);
            g.drawImage(imagePointed, x, y, w, h, observer);
            g.setColor(Color.BLACK);
        }
        else {
            g.setColor(new Color(0xFF, 0xFF, 0xFF, 100));
            g.fillRect(x, y, w, h);
            g.drawImage(imageDefault, x, y, w, h, observer);
            g.setColor(Color.BLACK);
        }
    }
}
