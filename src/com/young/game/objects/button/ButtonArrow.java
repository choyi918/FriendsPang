package com.young.game.objects.button;

import com.young.game.ui.CanvasGameOp;
import com.young.game.ui.CanvasRankingInput;

import java.awt.*;

public class ButtonArrow extends Button{
    public ButtonArrow(int leftBoundaryX, int rightBoundaryX, int upperBoundaryY, int lowerBoundaryY, Canvas observer) {
        super("button_arrow_default.png", "button_arrow_pointed.png",
                leftBoundaryX, rightBoundaryX, upperBoundaryY, lowerBoundaryY, observer);
    }

    public void draw(Graphics g) {
        super.draw(g, observer);
    }

}
