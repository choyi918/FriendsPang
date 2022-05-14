package com.young.game.objects.button;

import com.young.game.ui.CanvasGameOp;

import java.awt.*;

public class ButtonNext extends Button {
    private int x;
    private int y;

    public ButtonNext(int leftBoundaryX, int rightBoundaryX, int upperBoundaryY, int lowerBoundaryY, Canvas observer) {
        super("button_next_default.png", "button_next_pointed.png",
                leftBoundaryX, rightBoundaryX, upperBoundaryY, lowerBoundaryY, observer);
        x = 0 + 9 * CanvasGameOp.DW;
        y = 0 + 20 * CanvasGameOp.DH + 5 * CanvasGameOp.DH;
    }

    public void update() {
        for (int i = 0; i < 10; i++)
            if (upperBoundaryY != 0 + 20 * CanvasGameOp.DH + CanvasGameOp.DH / 2) {
                upperBoundaryY--;
                lowerBoundaryY--;
            }
    }

    @Override
    public boolean clickedByMouse(int mouseX, int mouseY) {
        int dw = CanvasGameOp.DW;
        int dh = CanvasGameOp.DH;

        if (upperBoundaryY != 0 + 20 * dh + dh / 2)
            return false;

        if (leftBoundaryX <= mouseX && mouseX <= rightBoundaryX
                && upperBoundaryY <= mouseY && mouseY <= lowerBoundaryY)
            return true;

        return false;
    }

    @Override
    public void pointedByMouse(int mouseX, int mouseY) {
        int dw = CanvasGameOp.DW;
        int dh = CanvasGameOp.DH;

        if (upperBoundaryY != 0 + 20 * dh + dh / 2)
            return;

        if (leftBoundaryX <= mouseX && mouseX <= rightBoundaryX
                && upperBoundaryY <= mouseY && mouseY <= lowerBoundaryY)
            bPointed = true;
        else
            bPointed = false;
    }

    public void draw(Graphics g) {
        super.draw(g, observer);
    }
}
