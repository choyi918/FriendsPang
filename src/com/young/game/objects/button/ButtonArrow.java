package com.young.game.objects.button;

import com.young.game.ui.CanvasGameOp;
import com.young.game.ui.CanvasRankingInput;

import java.awt.*;

public class ButtonArrow extends Button{
    public ButtonArrow() {
        super("button_arrow_default.png", "button_arrow_pointed.png",
                0 + 3 / 2 * CanvasRankingInput.DW + (('Z' - 'A') % 7 + 1) * 3 * CanvasRankingInput.DW,
                0 + 3 / 2 * CanvasRankingInput.DW + (('Z' - 'A') % 7 + 1) * 3 * CanvasRankingInput.DW + 2 * CanvasRankingInput.DW - CanvasRankingInput.DW / 4,
                0 + 8 * CanvasRankingInput.DH + (('Z' - 'A') / 7) * 3 * CanvasRankingInput.DH,
                0 + 8 * CanvasRankingInput.DH + (('Z' - 'A') / 7) * 3 * CanvasRankingInput.DH + 2 * CanvasRankingInput.DH - CanvasRankingInput.DH / 4);
    }

    public void draw(Graphics g) {
        super.draw(g, CanvasGameOp.getInstance());
    }
//    public ButtonArrow() {
//        super("button_arrow_default.png", "button_arrow_pointed.png");
//    }
//
//    @Override
//    public void draw(Graphics g) {
//        CanvasRankingInput observer = CanvasRankingInput.getInstance();
//
//        int dw = CanvasRankingInput.DW;
//        int dh = CanvasRankingInput.DH;
//
//        int defaultX = 0 + 3 / 2 * dw;
//        int defaultY = 0 + 8 * dh;
//
//        int x = defaultX + (('Z' - 'A') % 7 + 1) * 3 * dw;
//        int y = defaultY + (('Z' - 'A') / 7) * 3 * dh;
//
//        int dw2 = dw / 4;
//        int dh2 = dh / 4;
//
//        g.setColor(new Color(0xFF, 0xFF, 0xFF, 150));
//        g.fillRect(x, y, 2 * dw - dw2, 2 * dh - dh2);
//        g.setColor(Color.BLACK);
//        g.drawRect(x, y, 2 * dw - dw2, 2 * dh - dh2);
//
//        if (bPointed)
//            g.drawImage(imagePointed, x, y, 2 * dw - dw2, 2 * dh - dh2, observer);
//        else
//            g.drawImage(imageDefault, x, y, 2 * dw - dw2, 2 * dh - dh2, observer);
//    }
}
