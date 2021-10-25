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


    private CanvasMain() {
        setSize(680, 702);
        setBackground(new Color(0xFD, 0xDC, 0x2F));

        imageMain = new ImageMain();
        buttonGameStart = ButtonGameStart.getInstance();
        buttonRankingShowing = ButtonRankingShowing.getInstance();
        buttonExit = ButtonExit.getInstance();

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
        int obW = CanvasMain.this.getWidth();
        int obH = CanvasMain.this.getHeight();
        int dw = obW / 22;
        int dh = obH / 22;
        int mouseX;
        int mouseY;

        @Override
        public void mouseClicked(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();

            if (0 + 7 * dw <= mouseX && mouseX <= 0 + 15 * dw) {
                if (0 + 15 * dh <= mouseY && mouseY <= 0 + 16 * dh) {
                    System.out.println("Game Start!!");
                    playSound("click_mouse.wav");
                    CanvasMain.this.setVisible(false);
                    GameFrame gameFrame = GameFrame.getInstance();
                    CanvasGameOp canvasGameOp = CanvasGameOp.getInstance();
                    gameFrame.add(canvasGameOp);
                    canvasGameOp.setVisible(true);
                    canvasGameOp.validate();
                }
                else if (0 + 17 * dh <= mouseY && mouseY <= 0 + 18 * dh) {
                    System.out.println("Ranking!!");
                    playSound("click_mouse.wav");
                    CanvasMain.this.setVisible(false);
                    CanvasRanking canvasRanking = CanvasRanking.getInstance();
                    GameFrame.getInstance().add(canvasRanking);
                }
                else if (0 + 19 * dh <= mouseY && mouseY <= 0 + 20 * dh) {
                    System.out.println("go home!!");
                    playSound("click_mouse.wav");
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();

            buttonGameStart.outpointButton();
            buttonRankingShowing.outpointButton();
            buttonExit.outpointButton();

            if (0 + 7 * dw <= mouseX && mouseX <= 0 + 15 * dw) {
                if (0 + 15 * dh <= mouseY && mouseY <= 0 + 16 * dh)
                    buttonGameStart.pointButton();
                else if (0 + 17 * dh <= mouseY && mouseY <= 0 + 18 * dh)
                    buttonRankingShowing.pointButton();
                else if (0 + 19 * dh <= mouseY && mouseY <= 0 + 20 * dh)
                    buttonExit.pointButton();
            }
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
