package com.young.game.objects.button;

import com.young.game.ui.CanvasMain;

import java.awt.*;

public class ButtonGameStart extends Button{
    public ButtonGameStart() {
        super("button_game_start.png", "button_game_start_pointed.png",
                0 + 7 * CanvasMain.DW, 0 + 15 * CanvasMain.DW,
                0 + 15 * CanvasMain.DH, 0 + 16 * CanvasMain.DH);
    }

    public void draw(Graphics g) {
        super.draw(g, CanvasMain.getInstance());
    }
}
