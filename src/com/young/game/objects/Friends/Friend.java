package com.young.game.objects.Friends;

import com.young.game.objects.GameBoard;
import com.young.game.ui.CanvasGameOp;

import java.awt.*;

public abstract class Friend {
    private String name;
    private Image image;
    private int movingSpeed;
    private int boardX;
    private int boardY;
    private int presentCanvasX;
    private int presentCanvasY;
    private int targetCanvasX;
    private int targetCanvasY;

    public Friend(int boardX, int boardY, String name, String imageFileName) {
        this.boardX = boardX;
        this.boardY = boardY;
        this.name = name;
        movingSpeed = 10;
        image = Toolkit.getDefaultToolkit().getImage(String.format("res/images/%s", imageFileName));

        int dw = CanvasGameOp.DW;
        int dh = CanvasGameOp.DH;
        int defaultX = GameBoard.DEFAULT_X;
        int defaultY = GameBoard.DEFAULT_Y;

        presentCanvasX = defaultX + boardX * 2 * dw;
        presentCanvasY = defaultY + boardY * 2 * dh;
        targetCanvasX = presentCanvasX;
        targetCanvasY = presentCanvasY;
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

    /* setters */
    public void setBoardX(int boardX) {
        this.boardX = boardX;
    }

    public void setBoardY(int boardY) {
        this.boardY = boardY;
    }

    public void setPresentCanvasX(int presentCanvasX) {
        this.presentCanvasX = presentCanvasX;
    }

    public void setPresentCanvasY(int presentCanvasY) {
        this.presentCanvasY = presentCanvasY;
    }

    public void setTargetCanvasX(int targetCanvasX) {
        this.targetCanvasX = targetCanvasX;
    }

    public void setTargetCanvasY(int targetCanvasY) {
        this.targetCanvasY = targetCanvasY;
    }

    /* implemented in Canvas Thread*/
    public void update() {
        if (GameBoard.getInstance().isEmptyBelow(this))
            slipDown();
        else
            move();
    }

    /* for called by paint method of GameCanvas class*/
    public void draw(Graphics g) {
        CanvasGameOp observer = CanvasGameOp.getInstance();

        int dw = CanvasGameOp.DW;
        int dh = CanvasGameOp.DH;

        g.drawImage(image, presentCanvasX, presentCanvasY, 2 * dw, 2 * dh, observer);
    }

    public boolean isCompleteToMove() {
        return presentCanvasX == targetCanvasX && presentCanvasY == targetCanvasY;
    }

    /* for moving smoothly objects in GameCanvas */
    public void moveTo(Friend friend) {
        int fBoardX = friend.getBoardX();
        int fBoardY = friend.getBoardY();
        int dw = CanvasGameOp.DW;
        int dh = CanvasGameOp.DH;

        if ((boardY < GameBoard.BOARD_LENGTH - 1 && boardX == fBoardX && boardY + 1 == fBoardY)
                || (boardY > 0 && boardX == fBoardX && boardY - 1 == fBoardY))
            targetCanvasY = friend.getBoardY() * (2 * dh) + (4 * dh);
//            validCanvasY = friend.getCanvasY();
        else if ((boardX < GameBoard.BOARD_LENGTH - 1 && boardY == fBoardY && boardX + 1 == fBoardX)
                || (boardX > 0 && boardY == fBoardY && boardX - 1 == fBoardX))
            targetCanvasX = friend.getBoardX() * (2 * dw) + (4 * dw);
//            validCanvasX = friend.getCanvasX(); // -> 버그 : 연속적으로 마우스이벤트에 의한 교환이 일어났을 때 화면상으로 돌이 제자리를 찾아 그려지지 않음
    }

    private void slipDown() {
        GameBoard inBoard = GameBoard.getInstance();
        int defaultY = GameBoard.DEFAULT_Y;
        int dh = CanvasGameOp.DH;

        for (int i = 0; i < movingSpeed; i++) {
            presentCanvasY += 1;
            if (presentCanvasY == defaultY + (boardY + 1) * 2 * dh) {
                inBoard.alteredSlipDown(this);
                this.boardY += 1;
                targetCanvasY = presentCanvasY;
            }
        }
    }

    private void move() {
        for (int i = 0; i < movingSpeed; i++) {
            if (presentCanvasX < targetCanvasX)
                presentCanvasX += 1;
            else if (presentCanvasX > targetCanvasX)
                presentCanvasX -= 1;

            if (presentCanvasY < targetCanvasY)
                presentCanvasY += 1;
            else if (presentCanvasY > targetCanvasY)
                presentCanvasY -= 1;
        }
    }
}
