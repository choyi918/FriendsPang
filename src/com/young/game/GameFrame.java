package com.young.game;

import com.young.game.ui.CanvasGameOp;
import com.young.game.ui.CanvasMain;
import com.young.game.ui.CanvasRankingInput;

import javax.swing.JFrame;

public class GameFrame extends JFrame {
    private static GameFrame instance;
    private CanvasMain canvasMain;

    public GameFrame() {
        setSize(680, 702);
        setResizable(false);
        setTitle("Friends Pang");
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        canvasMain = CanvasMain.getInstance();
        canvasMain.requestFocus();
        add(canvasMain);

        validate();
    }

    public static GameFrame getInstance() {
        if (instance == null)
            instance = new GameFrame();

        return instance;
    }
}
