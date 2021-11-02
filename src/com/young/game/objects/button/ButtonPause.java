package com.young.game.objects.button;

import com.young.game.ui.CanvasGameOp;

import java.awt.*;

public class ButtonPause extends Button{

    public ButtonPause() {
        super("button_pause.png", "button_pause_pointed.png",
                0 + 16 * CanvasGameOp.DW, 0 + 20 * CanvasGameOp.DW,
                0 + 2 * CanvasGameOp.DH, 0 + 3 * CanvasGameOp.DH);
    }

    public void draw(Graphics g) {
        super.draw(g, CanvasGameOp.getInstance());
    }
}
