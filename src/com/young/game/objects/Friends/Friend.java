package com.young.game.objects.Friends;

import com.young.game.objects.GameBoard;
import com.young.game.ui.CanvasGameOp;

import java.awt.*;

public abstract class Friend {
    private String name;
    private Image image;
    private int movingSpeed;
    private int logicBoardX;
    private int logicBoardY;
    private int presentCanvasX;
    private int presentCanvasY;
    private int targetCanvasX;
    private int targetCanvasY;

    public Friend(int logicBoardX, int logicBoardY, String name, String imageFileName) {
        this.logicBoardX = logicBoardX;
        this.logicBoardY = logicBoardY;
        this.name = name;
        movingSpeed = 10;
        image = Toolkit.getDefaultToolkit().getImage(String.format("res/images/%s", imageFileName));

        int dw = CanvasGameOp.DW;
        int dh = CanvasGameOp.DH;
        int defaultX = GameBoard.DEFAULT_CANVAS_X;
        int defaultY = GameBoard.DEFAULT_CANVAS_Y;

        presentCanvasX = defaultX + logicBoardX * 2 * dw;
        presentCanvasY = defaultY + logicBoardY * 2 * dh;
        targetCanvasX = presentCanvasX;
        targetCanvasY = presentCanvasY;
    }

    /* getters */
    public String getName() {
        return name;
    }

    public int getLogicBoardX() {
        return logicBoardX;
    }

    public int getLogicBoardY() {
        return logicBoardY;
    }

    /* setters */
    public void setLogicBoardX(int logicBoardX) {
        this.logicBoardX = logicBoardX;
    }

    public void setLogicBoardY(int logicBoardY) {
        this.logicBoardY = logicBoardY;
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
            slipDownOnCanvas();
        else
            moveToTargetPositionOnePixelAtOnceOnCanvas();
    }

    /* for called by paint method of GameCanvas class*/
    public void draw(Graphics g) {
        CanvasGameOp observer = CanvasGameOp.getInstance();

        int dw = CanvasGameOp.DW;
        int dh = CanvasGameOp.DH;

        g.drawImage(image, presentCanvasX, presentCanvasY, 2 * dw, 2 * dh, observer);
    }

    public boolean isCompleteToMoveOnCanvas() {
        return presentCanvasX == targetCanvasX && presentCanvasY == targetCanvasY;
    }

    /* for moving smoothly objects in GameCanvas */
    public void moveToOnCanvas(Friend friend) {
        int fBoardX = friend.getLogicBoardX();
        int fBoardY = friend.getLogicBoardY();
        int dw = CanvasGameOp.DW;
        int dh = CanvasGameOp.DH;

        if ((logicBoardY < GameBoard.BOARD_LENGTH - 1 && logicBoardX == fBoardX && logicBoardY + 1 == fBoardY)
                || (logicBoardY > 0 && logicBoardX == fBoardX && logicBoardY - 1 == fBoardY))
            targetCanvasY = friend.getLogicBoardY() * (2 * dh) + (4 * dh);
//            validCanvasY = friend.getCanvasY();
        else if ((logicBoardX < GameBoard.BOARD_LENGTH - 1 && logicBoardY == fBoardY && logicBoardX + 1 == fBoardX)
                || (logicBoardX > 0 && logicBoardY == fBoardY && logicBoardX - 1 == fBoardX))
            targetCanvasX = friend.getLogicBoardX() * (2 * dw) + (4 * dw);
//            validCanvasX = friend.getCanvasX(); // -> 버그 : 연속적으로 마우스이벤트에 의한 교환이 일어났을 때 화면상으로 돌이 제자리를 찾아 그려지지 않음
    }

    private void slipDownOnCanvas() {
        GameBoard inBoard = GameBoard.getInstance();
        int defaultY = GameBoard.DEFAULT_CANVAS_Y;
        int dh = CanvasGameOp.DH;

        for (int i = 0; i < movingSpeed; i++) {
            presentCanvasY += 1;
            if (presentCanvasY == defaultY + (logicBoardY + 1) * 2 * dh) {
                inBoard.noticeSlipDownOf(this);
                this.logicBoardY += 1;
                targetCanvasY = presentCanvasY;
            }
        }
    }

    private void moveToTargetPositionOnePixelAtOnceOnCanvas() {
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
