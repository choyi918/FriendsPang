package com.young.game.objects.button;

import com.young.game.ui.CanvasGameOp;

import java.awt.*;

public class ButtonRestart extends Button {

    public ButtonRestart() {
        super("button_restart.png", "button_restart_pointed.png",
                0 + 16 * CanvasGameOp.DW, 0 + 20 * CanvasGameOp.DW,
                0 + 2 * CanvasGameOp.DH, 0 + 3 * CanvasGameOp.DH);
    }

    public void draw(Graphics g) {
        super.draw(g, CanvasGameOp.getInstance());
    }
}
