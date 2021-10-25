package com.young.game.objects.button;

import com.young.game.ui.CanvasRankingInput;

import java.awt.*;


public class ButtonChar extends Button {
    private char ch;

    public ButtonChar(char ch, String defaultFileName, String pointedFileName) {
        super(defaultFileName, pointedFileName);
        this.ch = ch;
    }

    public char getCh() {
        return ch;
    }

    @Override
    public void draw(Graphics g) {
        CanvasRankingInput observer = CanvasRankingInput.getInstance();

        int dw = observer.getWidth() / 22;
        int dh = observer.getHeight() / 22;

        int defaultX = 0 + 3 / 2 * dw;
        int defaultY = 0 + 8 * dh;

        int x = defaultX + ((ch - 'A') % 7) * 3 * dw;
        int y = defaultY + ((ch - 'A') / 7) * 3 * dh;

        int dw2 = dw / 4;
        int dh2 = dh / 4;

        g.setColor(new Color(0xFF, 0xFF, 0xFF, 150));
        g.fillRect(x, y, 2 * dw - dw2, 2 * dh - dh2);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, 2 * dw - dw2, 2 * dh - dh2);

        if (bPointed)
            g.drawImage(imagePointed, x, y, 2 * dw - dw2, 2 * dh - dh2, observer);
        else
            g.drawImage(imageDefault, x, y, 2 * dw - dw2, 2 * dh - dh2, observer);
    }
}
