package com.young.game.ui;

import com.young.game.GameFrame;
import com.young.game.objects.button.ButtonExit;
import com.young.game.objects.button.ButtonGameStart;
import com.young.game.objects.button.ButtonRankingShowing;
import com.young.game.objects.others.ImageMain;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class CanvasMain extends Canvas implements Runnable {
    private static CanvasMain instance;
    private ImageMain imageMain;
    private ButtonGameStart buttonGameStart;
    private ButtonRankingShowing buttonRankingShowing;
    private ButtonExit buttonExit;
    private MouseAdapterForCanvasMain mouseAdapterForCanvasMain;

    private static final int WIDTH;
    private static final int HEIGHT;
    public static final int DW;
    public static final int DH;

    static  {
        WIDTH = 680;
        HEIGHT = 680;
        DW = WIDTH / 22;
        DH = HEIGHT / 22;
    }


    private CanvasMain() {
        setSize(WIDTH, HEIGHT);
        setBackground(new Color(0xFD, 0xDC, 0x2F));

        imageMain = new ImageMain();
        buttonGameStart = new ButtonGameStart(0 + 7 * CanvasMain.DW, 0 + 15 * CanvasMain.DW,
                0 + 15 * CanvasMain.DH, 0 + 16 * CanvasMain.DH, this);
        buttonRankingShowing = new ButtonRankingShowing(0 + 7 * CanvasMain.DW, 0 + 15 * CanvasMain.DW,
                0 + 17 * CanvasMain.DH, 0 + 18 * CanvasMain.DH, this);
        buttonExit = new ButtonExit(0 + 7 * CanvasMain.DW, 0 + 15 * CanvasMain.DW,
                0 + 19 * CanvasMain.DH, 0 + 20 * CanvasMain.DH, this);

        mouseAdapterForCanvasMain = new MouseAdapterForCanvasMain();
        addMouseListener(mouseAdapterForCanvasMain);
        addMouseMotionListener(mouseAdapterForCanvasMain);

        new Thread(this).start();
    }

    public static CanvasMain getInstance() {
        if (instance == null)
            instance = new CanvasMain();

        return instance;
    }

    /* Double Buffering */
    @Override
    public void paint(Graphics g) {
        Image buff = createImage(getWidth(), getHeight());
        Graphics buffG = buff.getGraphics();

        imageMain.draw(buffG);
        buttonGameStart.draw(buffG);
        buttonRankingShowing.draw(buffG);
        buttonExit.draw(buffG);

        g.drawImage(buff, 0, 0, this);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void run() {
        while (true) {
            repaint();
            validate();
            try {
                Thread.sleep(1000 / 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /* nested class */
    private class MouseAdapterForCanvasMain extends MouseAdapter {
        int mouseX;
        int mouseY;

        @Override
        public void mouseClicked(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();

            if (buttonGameStart.clickedByMouse(mouseX, mouseY)) {
                    System.out.println("Game Start!!");
                    playSound("click_mouse.wav");
                    CanvasMain.this.setVisible(false);
                    GameFrame gameFrame = GameFrame.getInstance();
                    CanvasGameOp canvasGameOp = CanvasGameOp.getInstance();
                    gameFrame.add(canvasGameOp);
                    canvasGameOp.setVisible(true);
                    canvasGameOp.validate();
            }

            if (buttonRankingShowing.clickedByMouse(mouseX, mouseY)) {
                    System.out.println("Ranking!!");
                    playSound("click_mouse.wav");
                    CanvasMain.this.setVisible(false);
                    CanvasRanking canvasRanking = CanvasRanking.getInstance();
                    GameFrame.getInstance().add(canvasRanking);
            }

            if (buttonExit.clickedByMouse(mouseX, mouseY)) {
                    System.out.println("go home!!");
                    playSound("click_mouse.wav");
            }

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();

            buttonGameStart.pointedByMouse(mouseX, mouseY);
            buttonRankingShowing.pointedByMouse(mouseX, mouseY);
            buttonExit.pointedByMouse(mouseX, mouseY);
        }
    }

    public void playSound(String fileName) {
        try
        {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(String.format("res/effectSound/%s", fileName)));
            Clip clip = AudioSystem.getClip();
            clip.stop();
            clip.open(ais);
            clip.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
