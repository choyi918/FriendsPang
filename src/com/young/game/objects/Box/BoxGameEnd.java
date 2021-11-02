package com.young.game.objects.Box;

import com.young.game.ui.CanvasGameOp;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.io.File;

public class BoxGameEnd {
    private int x;
    private int y;
    private Image image;
    private int imageY;
    private boolean bCompleteMoving;
    private boolean bSoundPlay;

    public BoxGameEnd() {
        x = 0 + 4 * CanvasGameOp.DW;
        y = 0 + 4 * CanvasGameOp.DH;

        image = Toolkit.getDefaultToolkit().getImage("res/images/end.png");
        imageY = 0 - 7 * 2 * CanvasGameOp.DH;
    }

    public boolean isCompleteToMove() {
        return bCompleteMoving;
    }

    public void update() {
        int dh = CanvasGameOp.DH;
        if (imageY != 0 + 4 * dh)
            for (int i = 0; i < 20; i++)
                imageY++;
        else {
            playSound("alter_end.wav");
            bCompleteMoving = true;
        }
    }

    public void draw(Graphics g) {
        CanvasGameOp observer = CanvasGameOp.getInstance();
        int dw = CanvasGameOp.DW;
        int dh = CanvasGameOp.DH;

        g.setColor(new Color(0xFF, 0xFF, 0xFF, 150));
        g.fillRect(x, y, 7 * 2 * dw, 7 * 2 * dh);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, 7 * 2 * dw, 7 * 2 * dh);
        g.drawImage(image, 0 + 4 * dw, imageY, 7 * 2 * dw, 7 * 2 * dw, observer);
    }

    public void playSound(String fileName) {
        try
        {
            if (!bSoundPlay) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File(String.format("res/effectSound/%s", fileName)));
                Clip clip = AudioSystem.getClip();
                clip.stop();
                clip.open(ais);
                clip.start();
                bSoundPlay = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
