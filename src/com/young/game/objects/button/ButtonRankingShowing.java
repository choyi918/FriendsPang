package com.young.game.objects.button;

import com.young.game.ui.CanvasMain;

import java.awt.*;

public class ButtonRankingShowing extends Button{

    public ButtonRankingShowing() {
        super("button_ranking.png", "button_ranking_pointed.png",
                0 + 7 * CanvasMain.DW, 0 + 15 * CanvasMain.DW,
                0 + 17 * CanvasMain.DH, 0 + 18 * CanvasMain.DH);
    }

    public void draw(Graphics g) {
        super.draw(g, CanvasMain.getInstance());
    }

}
