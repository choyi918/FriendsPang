package com.young.game.objects.button;

import com.young.game.ui.CanvasMain;

import java.awt.*;

public class ButtonGameStart extends Button{
    public ButtonGameStart(int leftBoundaryX, int rightBoundaryX, int upperBoundaryY, int lowerBoundaryY, Canvas observer) {
        super("button_game_start.png", "button_game_start_pointed.png",
                leftBoundaryX, rightBoundaryX, upperBoundaryY, lowerBoundaryY, observer);
    }

    public void draw(Graphics g) {
        super.draw(g, observer);
    }
}
