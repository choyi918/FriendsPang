package com.young.game.objects.button;

import com.young.game.ui.CanvasGameOp;
import com.young.game.ui.CanvasRanking;
import com.young.game.ui.CanvasRankingInput;

import java.awt.*;

public class ButtonBackMainTwo extends Button {
    private static ButtonBackMainTwo instance;

    private ButtonBackMainTwo() {
        super("button_back.png", "button_back_pointed.png");
    }

    public static ButtonBackMainTwo getInstance() {
        if (instance == null)
            instance = new ButtonBackMainTwo();
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    @Override
    public void draw(Graphics g) {
        CanvasRanking observer = CanvasRanking.getInstance();

        int obW = observer.getWidth();
        int obH = observer.getHeight();

        int dw = obW / 22;
        int dh = obH / 22;

        int x = 0 + 9 * dw;
        int y = 0 + 20 * dh;

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
