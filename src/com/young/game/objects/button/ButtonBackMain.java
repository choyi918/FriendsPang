package com.young.game.objects.button;

import com.young.game.ui.CanvasGameOp;

import java.awt.*;

public class ButtonBackMain extends Button {
    private Canvas observer;

    public ButtonBackMain(int leftBoundaryX, int rightBoundaryX, int upperBoundaryY, int lowerBoundaryY, Canvas observer) {
        super("button_back.png", "button_back_pointed.png",
                leftBoundaryX, rightBoundaryX, upperBoundaryY, lowerBoundaryY);
        this.observer = observer;
    }

    public void draw(Graphics g) {
        super.draw(g, observer);
    }
}
