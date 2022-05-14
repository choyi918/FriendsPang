package com.young.game.objects.button;

import com.young.game.ui.CanvasMain;

import java.awt.*;

public class ButtonExit extends Button{
    public ButtonExit(int leftBoundaryX, int rightBoundaryX, int upperBoundaryY, int lowerBoundaryY, Canvas observer) {
        super("button_exit.png","button_exit_pointed.png",
                leftBoundaryX, rightBoundaryX, upperBoundaryY, lowerBoundaryY, observer);
    }

    public void draw(Graphics g) {
        super.draw(g, observer);
    }
}
