package com.young.game.ui;

import com.young.game.GameFrame;
import com.young.game.objects.Box.BoxGameEnd;
import com.young.game.objects.button.ButtonBackMain;
import com.young.game.objects.button.ButtonNext;
import com.young.game.objects.button.ButtonPause;
import com.young.game.objects.Friends.Friend;
import com.young.game.objects.GameBoard;
import com.young.game.objects.button.ButtonRestart;
import com.young.game.objects.pointViewer.LabelPoint;
import com.young.game.objects.timer.LabelTimer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class CanvasGameOp extends Canvas implements Runnable {
    private boolean bPause;
    private boolean bRunning;
    private boolean bPossibleMouseEvent;
    private final Image imageBackground;
    private final ButtonBackMain buttonBackMain;
    private ButtonPause buttonPause;
    private ButtonRestart buttonRestart;
    private ButtonNext buttonNext;
    private final LabelPoint labelPoint;
    private final LabelTimer labelTimer;
    private final BoxGameEnd boxGameEnd;
    private final GameBoard gameBoard;

    /* Canvas size */
    private static final int WIDTH;
    private static final int HEIGHT;

    /*Canvas 22등분 한 후 단위 길이  */
    public static final int DW;
    public static final int DH;

    private static CanvasGameOp instance;

    static {
        WIDTH = 680;
        HEIGHT = 680;
        DW = WIDTH / 22;
        DH = HEIGHT / 22;
    }

    private CanvasGameOp() {
        setSize(WIDTH, HEIGHT);
        setBackground(new Color(0xFD, 0xDC, 0x2F));

        buttonBackMain = new ButtonBackMain(2 * DW, 0 + 2 * DW + 4 * DW,
                2 * DH, 2 * DH + DH, this);
        buttonPause = new ButtonPause(0 + 16 * DW, 0 + 20 * DW,
                0 + 2 * DH, 0 + 3 * DH, this);
        labelTimer = new LabelTimer();
        labelPoint = new LabelPoint();
        boxGameEnd = new BoxGameEnd();
        gameBoard = GameBoard.getInstance();
        imageBackground = Toolkit.getDefaultToolkit().getImage("res/images/autumn_story.png");

        MouseAdapterForCanvasGameOp mouseAdapterForCanvasGameOp = new MouseAdapterForCanvasGameOp();
        addMouseListener(mouseAdapterForCanvasGameOp);
        addMouseMotionListener(mouseAdapterForCanvasGameOp);

        bRunning = true;
        new Thread(this).start();
    }

    public static CanvasGameOp getInstance() {
        if (instance == null)
            instance = new CanvasGameOp();
        return instance;
    }

    public static void reset() {
        GameBoard.reset();
        instance = null;
        System.out.println("Game Reset");
    }

    @Override
    public void paint(Graphics g) {
        Image buff = createImage(getWidth(), getHeight());
        Graphics buffG = buff.getGraphics();

        buffG.drawImage(imageBackground, 0, 0, getWidth(), getHeight(), this);
        buttonBackMain.draw(buffG);
        labelPoint.draw(buffG);
        labelTimer.draw(buffG);
        gameBoard.draw(buffG);

        if (buttonPause != null)
            buttonPause.draw(buffG);

        if (buttonRestart != null)
            buttonRestart.draw(buffG);

        if (buttonNext != null)
            buttonNext.draw(buffG);

        if (labelTimer.isTimeout())
            boxGameEnd.draw(buffG);

        g.drawImage(buff, 0, 0, this);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void run(){
        while (bRunning) {
            if (!labelTimer.isTimeout()) {

                if (gameBoard.getLinkedListOfBoardForThreadUpdating().size() == 0)
                    gameBoard.makeQueueOfVerifiedInitializedBoard();

                gameBoard.fillTopLine();

                for (Friend f : gameBoard.getLinkedListOfBoardForThreadUpdating())
                    f.update();

                /* 49개 다 꽉 차고, Friends들이 다 제자리에 온전히 위치한 상태에서만 실행되도록.*/
                if (gameBoard.isFull() && isAllCompleteToMove()) {
                    gameBoard.update();
                    bPossibleMouseEvent = true;
                }

                if (gameBoard.isFull() && isAllCompleteToMove() && !gameBoard.checkValidationBoardAndClearBoard())
                    continue;

                labelTimer.update();
            }
            else {
                boxGameEnd.update();

                if (buttonNext == null && boxGameEnd.isCompleteToMove())
                    buttonNext = new ButtonNext(0 + 9 * CanvasGameOp.DW, 0 + 13 * CanvasGameOp.DW,
                            0 + 20 * CanvasGameOp.DH + 5 * CanvasGameOp.DH, 0 + 20 * CanvasGameOp.DH + 6 * CanvasGameOp.DH, this);

                if (buttonNext != null)
                    buttonNext.update();
            }

            labelPoint.setFinalPoint(gameBoard.getPoint());
            labelPoint.update();

            repaint();
            validate();

            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isAllCompleteToMove() {
        for (Friend f : gameBoard.getLinkedListOfBoardForThreadUpdating()) {
            if (!f.isCompleteToMoveOnCanvas())
                return false;
        }
        return true;
    }

    private class MouseAdapterForCanvasGameOp extends MouseAdapter{
        int pressedX;
        int pressedY;
        int releasedX;
        int releasedY;

        @Override
        public void mouseClicked(MouseEvent e) {
            int mouseX = e.getX();
            int mouseY = e.getY();

            if (buttonBackMain.clickedByMouse(mouseX, mouseY)) {
                playSound("click_mouse.wav");
                CanvasMain.getInstance().setVisible(true);
                CanvasGameOp.this.setVisible(false);
                /* Game을 초기화 하는 코드*/
                bRunning = false;
                reset();
            }

            if (buttonPause != null && buttonPause.clickedByMouse(mouseX, mouseY)) {
//                System.out.println("Pause!!");
                playSound("click_mouse.wav");
                /* Game을 일시정지 하는 코드 : 마우스 안 먹게하기, 타이머 일시정지, 서브스레드에 업데이트관련 동작들 동작 ㄴ*/
                labelTimer.stop();
                bPause = true;
                buttonPause = null;
                buttonRestart = new ButtonRestart(0 + 16 * DW, 0 + 20 * DW,
                        0 + 2 * DH, 0 + 3 * DH, CanvasGameOp.getInstance());
            } else if (buttonRestart != null && buttonRestart.clickedByMouse(mouseX, mouseY)) {
//                System.out.println("ReStart!!");
                playSound("click_mouse.wav");
                labelTimer.start();
                bPause = false;
                buttonRestart = null;
                buttonPause = new ButtonPause(0 + 16 * DW, 0 + 20 * DW,
                        0 + 2 * DH, 0 + 3 * DH, CanvasGameOp.getInstance());
            }

            if (buttonNext != null && buttonNext.clickedByMouse(mouseX, mouseY)) {
                playSound("click_mouse.wav");
                setVisible(false);
                GameFrame.getInstance().add(CanvasRankingInput.getInstance());
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            int mouseX = e.getX();
            int mouseY = e.getY();

            buttonBackMain.pointedByMouse(mouseX, mouseY);

            if (buttonPause != null)
                buttonPause.pointedByMouse(mouseX, mouseY);

            if (buttonRestart != null)
                buttonRestart.pointedByMouse(mouseX, mouseY);

            if (buttonNext != null)
                buttonNext.pointedByMouse(mouseX, mouseY);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (bPause)
                return;
            if (!bPossibleMouseEvent)
                return;

            pressedX = e.getX();
            pressedY = e.getY();

            if (!(0 + 4 * DW <= pressedX && pressedX <= 0 + 18 * DW
                    && 0 + 4 * DH <= pressedY && pressedY <= 0 + 18 * DH)) {
                pressedX = 0;
                pressedY = 0;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (bPause)
                return;
            if (!bPossibleMouseEvent)
                return;

            bPossibleMouseEvent = false;
            releasedX  = e.getX();
            releasedY = e.getY();

            /* 조건에 맞지 않는 경우 그냥 return */
            if (!(0 + 4 * DW <= releasedX && releasedX <= 0 + 18 * DW
                    && 0 + 4 * DH <= releasedY && releasedY <= 0 + 18 * DH)) {
                initializePressedAndReleasedXY();
                return;
            }

            if ((pressedX == 0 && releasedY == 0)
                    || pressedX == releasedX && pressedY == releasedY) {
                initializePressedAndReleasedXY();
                return;
            }

            /* 마우스 릴리즈가 프레스 기준으로 어떤 경계 안으로 떨어졌는지 판단하고 그에 따라 board X2, Y2를 정함 */
            int boardX1 = (pressedX - 4 * DW) / (2 * DW);
            int boardY1 = (pressedY - 4 * DH) / (2 * DH);
            int boardX2 = 0;
            int boardY2 = 0;
            int px = pressedX;
            int py = pressedY;
            int rx = releasedX;
            int ry = releasedY;

            if (ry <= rx - px + py && ry >= -rx + px + py) {
                boardX2 = boardX1 + 1;
                boardY2 = boardY1;
            } else if (ry < rx - px + py && ry < -rx + px + py){
                boardX2 = boardX1;
                boardY2 = boardY1 - 1;
            } else if (ry >= rx - px + py && ry <= -rx + px + py) {
                boardX2 = boardX1 - 1;
                boardY2 = boardY1;
            } else {
                boardX2 = boardX1;
                boardY2 = boardY1 + 1;
            }

            if (Math.abs(boardX2 - boardX1) > 1
                    || Math.abs(boardY2 - boardY1) > 1
                    || (Math.abs(boardX2 - boardX1) == 1 && Math.abs(boardY2 - boardY1) == 1)) {
                initializePressedAndReleasedXY();
                return;
            }

            Friend f1 = gameBoard.getFriend(boardX1, boardY1);
            Friend f2 = gameBoard.getFriend(boardX2, boardY2);
            gameBoard.swap(f1, f2);
            playSound("move_friend.wav");
            initializePressedAndReleasedXY();
        }

        private void initializePressedAndReleasedXY() {
            pressedX = 0;
            pressedY = 0;
            releasedX = 0;
            releasedY = 0;
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
