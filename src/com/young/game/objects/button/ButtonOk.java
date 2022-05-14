package com.young.game.objects.button;

import com.young.game.ui.CanvasGameOp;
import com.young.game.ui.CanvasRankingInput;

import java.awt.*;

public class ButtonOk extends Button{
    public ButtonOk(int leftBoundaryX, int rightBoundaryX, int upperBoundaryY, int lowerBoundaryY, Canvas observer) {
        super("button_ok_default.png", "button_ok_pointed.png",
                0 + 3 / 2 * CanvasRankingInput.DW + (('Z' - 'A') % 7 + 2) * 3 * CanvasRankingInput.DW,
        0 + 3 / 2 * CanvasRankingInput.DW + (('Z' - 'A') % 7 + 2) * 3 * CanvasRankingInput.DW + 2 * CanvasRankingInput.DW - CanvasRankingInput.DW / 4,
        0 + 8 * CanvasRankingInput.DH + (('Z' - 'A') / 7) * 3 * CanvasRankingInput.DH,
                0 + 8 * CanvasRankingInput.DH + (('Z' - 'A') / 7) * 3 * CanvasRankingInput.DH + 2 * CanvasRankingInput.DH - CanvasRankingInput.DH / 4,
                observer);
    }

    public void draw(Graphics g) {
        super.draw(g, observer);
    }

}
