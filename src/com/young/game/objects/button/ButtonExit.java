package com.young.game.objects.button;

import com.young.game.ui.CanvasMain;

import java.awt.*;

public class ButtonExit extends Button{
    public ButtonExit() {
        super("button_exit.png","button_exit_pointed.png",
                0 + 7 * CanvasMain.DW, 0 + 15 * CanvasMain.DW,
                0 + 19 * CanvasMain.DH, 0 + 20 * CanvasMain.DH);
    }

    public void draw(Graphics g) {
        super.draw(g, CanvasMain.getInstance());
    }
}
