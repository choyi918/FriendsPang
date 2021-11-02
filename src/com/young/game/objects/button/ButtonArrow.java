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

}
