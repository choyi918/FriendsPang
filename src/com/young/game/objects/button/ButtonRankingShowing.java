package com.young.game.objects.button;

import com.young.game.ui.CanvasMain;

import java.awt.*;

public class ButtonRankingShowing extends Button{

    public ButtonRankingShowing(int leftBoundaryX, int rightBoundaryX, int upperBoundaryY, int lowerBoundaryY, Canvas observer) {
        super("button_ranking.png", "button_ranking_pointed.png",
                leftBoundaryX, rightBoundaryX, upperBoundaryY, lowerBoundaryY, observer);
    }

    public void draw(Graphics g) {
        super.draw(g, observer);
    }

}
