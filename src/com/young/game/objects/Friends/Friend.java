package com.young.game.objects.Friends;

import com.young.game.objects.GameBoard;
import com.young.game.ui.CanvasGameOp;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.io.File;

public abstract class Friend {
    private String name;
    private Image image;
    private GameBoard inBoard;
    private int speed;
    private int boardX;
    private int boardY;
    private int canvasX;
    private int canvasY;
    private int validCanvasX;
    private int validCanvasY;

    public Friend(int boardX, int boardY, String name, String imageFileName) {
        this.boardX = boardX;
        this.boardY = boardY;
        this.name = name;
        speed = 10;
        image = Toolkit.getDefaultToolkit().getImage(String.format("res/images/%s", imageFileName));

        inBoard = GameBoard.getInstance();

        int dw = inBoard.getDw();
        int dh = inBoard.getDh();
        int defaultX = inBoard.getDefaultX();
        int defaultY = inBoard.getDefaultY();

        canvasX = defaultX + boardX * 2 * dw;
        canvasY = defaultY + boardY * 2 * dh;
        validCanvasX = canvasX;
        validCanvasY = canvasY;
    }

    /* getters */
    public String getName() {
        return name;
    }

    public int getBoardX() {
        return boardX;
    }

    public int getBoardY() {
        return boardY;
    }

    public int getCanvasX() {
        return canvasX;
    }

    public int getCanvasY() {
        return canvasY;
    }

    /* setters */
    public void setBoardX(int boardX) {
        this.boardX = boardX;
    }

    public void setBoardY(int boardY) {
        this.boardY = boardY;
    }

    public void setCanvasX(int canvasX) {
        this.canvasX = canvasX;
    }

    public void setCanvasY(int canvasY) {
        this.canvasY = canvasY;
    }

    public void setValidCanvasX(int validCanvasX) {
        this.validCanvasX = validCanvasX;
    }

    public void setValidCanvasY(int validCanvasY) {
        this.validCanvasY = validCanvasY;
    }

    /* implemented in Canvas Thread*/
    public void update() {
        if (boardY != inBoard.getBoard().length - 1 && inBoard.getBoard()[boardY + 1][boardX] == null)
            slideDown();
        else
            move();
    }

    /* for called by paint method of GameCanvas class*/
    public void draw(Graphics g) {
        CanvasGameOp observer = CanvasGameOp.getInstance();

        int dw = inBoard.getDw();
        int dh = inBoard.getDh();

        g.drawImage(image, canvasX, canvasY, 2 * dw, 2 * dh, observer);
    }

    public boolean isCompleteToMove() {
        return canvasX == validCanvasX && canvasY == validCanvasY;
    }

    /* for moving smoothly objects in GameCanvas */
    public void moveTo(Friend friend) {
        int fBoardX = friend.getBoardX();
        int fBoardY = friend.getBoardY();
        int dw = inBoard.getDw();
        int dh = inBoard.getDh();

        if ((boardY < inBoard.getBoard().length - 1 && boardX == fBoardX && boardY + 1 == fBoardY)
                || (boardY > 0 && boardX == fBoardX && boardY - 1 == fBoardY))
            validCanvasY = friend.getBoardY() * (2 * dh) + (4 * dh);
//            validCanvasY = friend.getCanvasY();
        else if ((boardX < inBoard.getBoard().length - 1 && boardY == fBoardY && boardX + 1 == fBoardX)
                || (boardX > 0 && boardY == fBoardY && boardX - 1 == fBoardX))
            validCanvasX = friend.getBoardX() * (2 * dw) + (4 * dw);
//            validCanvasX = friend.getCanvasX(); // -> 버그 : 연속적으로 마우스이벤트에 의한 교환이 일어났을 때 화면상으로 돌이 제자리를 찾아 그려지지 않음
    }

    private void slideDown() {
        Friend[][] board = inBoard.getBoard();
        int defaultY = inBoard.getDefaultY();
        int dh = inBoard.getDh();

        for (int i = 0; i < speed; i++) {
            canvasY += 1;
            if (canvasY == defaultY + (boardY + 1) * 2 * dh) {
                board[boardY][boardX] = null;
                board[boardY + 1][boardX] = this;
                setBoardX(boardX);
                setBoardY(boardY + 1);
                setValidCanvasX(canvasX);
                setValidCanvasY(canvasY);
            }
        }
    }

    private void move() {
        for (int i = 0; i < speed; i++) {
            if (canvasX < validCanvasX)
                canvasX += 1;
            else if (canvasX > validCanvasX)
                canvasX -= 1;
            else
                setValidCanvasX(canvasX);

            if (canvasY < validCanvasY)
                canvasY += 1;
            else if (canvasY > validCanvasY)
                canvasY -= 1;
            else
                setValidCanvasY(canvasY);
        }
    }

    private void playSound(String fileName) {
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
