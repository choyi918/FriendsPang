package com.young.game.objects.Box;

import com.young.game.objects.button.ButtonChar;
import com.young.game.ui.CanvasRankingInput;

import java.awt.*;
import java.util.LinkedList;

public class BoxRankingInputNameViewer {
    private LinkedList<ButtonChar> buttonChars;

    public BoxRankingInputNameViewer(LinkedList<ButtonChar> buttonChars) {
        this.buttonChars = buttonChars;
    }

    public void draw(Graphics g) {
        CanvasRankingInput observer = CanvasRankingInput.getInstance();

        int dw = observer.DW;
        int dh = observer.DH;

        int defaultX = 0 + 3 * dw;
        int defaultY = 0 + 5 * dh;

        for (int i = 0; i < buttonChars.size(); i++) {
            Image image = buttonChars.get(i).getImage();
            int x = defaultX + (i * 2 * dw - dw / 4);
            g.drawRect(x, defaultY, 2 * dw - dw / 4, 2 * dh - dh / 4);
            g.drawImage(image, x, defaultY, 2 * dw - dw / 4, 2 * dh - dh / 4, observer);
        }

    }
}
