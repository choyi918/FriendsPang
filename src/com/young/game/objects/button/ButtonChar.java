package com.young.game.objects.button;

import com.young.game.ui.CanvasGameOp;
import com.young.game.ui.CanvasRankingInput;

import java.awt.*;


public class ButtonChar extends Button {
    private char ch;

    public ButtonChar(char ch, String defaultFileName, String pointedFileName,
                      int leftBoundaryX, int rightBoundaryX, int upperBoundaryY, int lowerBoundaryY) {
        super(defaultFileName, pointedFileName, leftBoundaryX, rightBoundaryX, upperBoundaryY, lowerBoundaryY);
        this.ch = ch;
    }

    public char getCh() {
        return ch;
    }

    public void draw(Graphics g) {
        super.draw(g, CanvasGameOp.getInstance());
    }
}
