package com.young.game.objects.Box;

import com.young.game.objects.button.ButtonChar;
import com.young.game.ui.CanvasRankingInput;

import java.awt.*;
import java.util.LinkedList;

public class BoxNameViewer {
    private LinkedList<ButtonChar> buttonChars;

    public BoxNameViewer(LinkedList<ButtonChar> buttonChars) {
        this.buttonChars = buttonChars;
    }

    public void draw(Graphics g) {
        CanvasRankingInput observer = CanvasRankingInput.getInstance();

        int dw = observer.DW;
        int dh = observer.DH;
        int dw2 = dw / 4;
        int dh2 = dh / 4;

        int defaultX = 0 + 3 * dw;
        int defaultY = 0 + 5 * dh;

        g.setColor(new Color(0xFF, 0xFF, 0xFF, 150));
        g.fillRect(defaultX - dw2, defaultY, 9 * (2 * dw - dw2),2 * dh - dh2);
        g.setColor(Color.BLACK);

        for (int i = 0; i < buttonChars.size(); i++) {
            Image image = buttonChars.get(i).getImage();
            int x = defaultX + (i * 2 * dw - dw2);
            g.drawRect(x, defaultY, 2 * dw - dw2, 2 * dh - dh2);
            g.drawImage(image, x, defaultY, 2 * dw - dw2, 2 * dh - dh2, observer);
        }

    }
}
