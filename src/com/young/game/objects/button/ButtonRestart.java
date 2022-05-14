package com.young.game.objects.button;

import com.young.game.ui.CanvasGameOp;

import java.awt.*;

public class ButtonRestart extends Button {

    public ButtonRestart(int leftBoundaryX, int rightBoundaryX, int upperBoundaryY, int lowerBoundaryY, Canvas observer) {
        super("button_restart.png", "button_restart_pointed.png",
                leftBoundaryX, rightBoundaryX, upperBoundaryY, lowerBoundaryY, observer);
    }

    public void draw(Graphics g) {
        super.draw(g, observer);
    }
}
