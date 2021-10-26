package com.young.game.ui;

import com.young.game.GameFrame;
import com.young.game.objects.Box.BoxGameEnd;
import com.young.game.objects.button.ButtonBackMain;
import com.young.game.objects.button.ButtonNext;
import com.young.game.objects.button.ButtonPause;
import com.young.game.objects.Friends.Friend;
import com.young.game.objects.GameBoard;
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
    private static CanvasGameOp instance;
    private ButtonBackMain buttonBackMain;
    private ButtonPause buttonPause;
    private ButtonNext buttonNext;
    private LabelPoint labelPoint;
    private LabelTimer labelTimer;
    private GameBoard gameBoard;
    private Image imageBackGround;
    private BoxGameEnd boxGameEnd;

    private MouseAdapterForCanvasGameOp mouseAdapterForCanvasGameOp;

    private boolean bPause;
    private boolean bRunning;
    private boolean bPossibleMouseEvent;

    /*Canvas 22등분 한 후 단위 길이  */
    private static final int WIDTH;
    private static final int HEIGHT;
    public static final int DW;
    public static final int DH;

    static {
        WIDTH = 680;
        HEIGHT = 680;
        DW = WIDTH / 22;
        DH = HEIGHT / 22;
    }


    private CanvasGameOp() {
        setSize(WIDTH, HEIGHT);
        setBackground(new Color(0xFD, 0xDC, 0x2F));

        buttonBackMain = ButtonBackMain.getInstance();
        buttonPause = ButtonPause.getInstance();
        labelPoint = LabelPoint.getInstance();
        labelTimer = LabelTimer.getInstance();
        gameBoard = GameBoard.getInstance();
        boxGameEnd = new BoxGameEnd();
        imageBackGround = Toolkit.getDefaultToolkit().getImage("res/images/autumn_story.png");

        mouseAdapterForCanvasGameOp = new MouseAdapterForCanvasGameOp();
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
        ButtonBackMain.reset();
        ButtonPause.reset();
        LabelPoint.reset();
        LabelTimer.reset();
        GameBoard.reset();
        instance = null;
        System.out.println("Game Reset");
    }

    /* Getters */
    public boolean isPause() {
        return bPause;
    }

    @Override
    public void paint(Graphics g) {
        Image buff = createImage(getWidth(), getHeight());
        Graphics buffG = buff.getGraphics();

        buffG.drawImage(imageBackGround, 0, 0, getWidth(), getHeight(), this);
        buttonBackMain.draw(buffG);
        buttonPause.draw(buffG);
        labelPoint.draw(buffG);
        labelTimer.draw(buffG);
        gameBoard.draw(buffG);

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
        while(bRunning){

            if (!labelTimer.isTimeout()) {

                if (gameBoard.getFriendsArrayList().size() == 0)
                    gameBoard.initializeToVerifiedBoard();

                gameBoard.fillTopLine();

                for (Friend f : gameBoard.getFriendsArrayList())
                    f.update();

                /* 49개 다 꽉 차고, Friends들이 다 제자리에 온전히 위치한 상태에서만 실행되도록.*/
                if (isAllCompleteToMove() && gameBoard.isFull()) {
                    gameBoard.update();
                    bPossibleMouseEvent = true;
                }
                if (isAllCompleteToMove() && gameBoard.isFull() && !gameBoard.checkValidationBoardAndClearBoard())
                    continue;

                labelTimer.update();
            }
            else {
                boxGameEnd.update();

                if (buttonNext == null && boxGameEnd.isCompleteMoving())
                    buttonNext = new ButtonNext();

                if (buttonNext != null)
                    buttonNext.update();
            }

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
        for (Friend f : gameBoard.getFriendsArrayList()) {
            if (!f.isCompleteToMove())
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
            Point p = e.getPoint();
            int mouseX = (int)p.getX();
            int mouseY = (int)p.getY();

            if (0 + 2 * DH <= mouseY && mouseY <= 0 + 3 * DH) {
                if (0 + 2 * DW <= mouseX && mouseX <= 0 + 6 * DW) {
                    playSound("click_mouse.wav");
                    CanvasMain.getInstance().setVisible(true);
                    CanvasGameOp.this.setVisible(false);
                    /* Game을 초기화 하는 코드*/
                    bRunning = false;
                    reset();
                }
                else if (0 + 16 * DH <= mouseX && mouseX <= 0 + 20 * DH) {
                    System.out.println("Pause!!");
                    playSound("click_mouse.wav");
                    /* Game을 일시정지 하는 코드 : 마우스 안 먹게하기, 타이머 일시정지, 서브스레드에 업데이트관련 동작들 동작 ㄴ*/
                    if (!bPause) {
                        labelTimer.stop();
                        bPause = true;
                    } else {
                        labelTimer.start();
                        bPause = false;
                    }
                }
            }

            if (buttonNext != null)
                if (0 + 9 * DW <= mouseX && mouseX <= 0 + 9 * DW + 4 * DW
                        && 0 + 20 * DH + DH / 2 <= mouseY && mouseY <= 0 + 20 * DH + DH / 2 + DH) {
                    playSound("click_mouse.wav");
                    setVisible(false);
                    GameFrame.getInstance().add(CanvasRankingInput.getInstance());
                }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Point p = e.getPoint();
            int mouseX = (int)p.getX();
            int mouseY = (int)p.getY();

            buttonBackMain.outpointButton();
            buttonPause.outpointButton();

            if (0 + 2 * DH <= mouseY && mouseY <= 0 + 3 * DH) {
                if (0 + 2 * DW <= mouseX && mouseX <= 0 + 6 * DW)
                    buttonBackMain.pointButton();
                else if (0 + 16 * DH <= mouseX && mouseX <= 0 + 20 * DH)
                    buttonPause.pointButton();
            }

            if (buttonNext != null) {
                buttonNext.outpointButton();
                if (0 + 9 * DW <= mouseX && mouseX <= 0 + 9 * DW + 4 * DW
                        && 0 + 20 * DH + DH / 2 <= mouseY && mouseY <= 0 + 20 * DH + DH / 2 + DH)
                    buttonNext.pointButton();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (bPause)
                return;
            if (!bPossibleMouseEvent)
                return;

            Point p = e.getPoint();
            pressedX = (int)p.getX();
            pressedY = (int)p.getY();

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
            /*동기화문제를 해결해야할 수도 있음
             * game 판단은 서브스레드에서
             * 현 메서드에서 moveTo나 swap같은 경우는 메인 스레드에서 진행됨.
             * 두 스레드간 데이터 간섭이 있는지 생각해봐야함.
             * */
            bPossibleMouseEvent = false;
            Point p = e.getPoint();
            releasedX  = (int)p.getX();
            releasedY = (int)p.getY();

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

            Friend f1 = gameBoard.getBoard()[boardY1][boardX1];
            Friend f2 = gameBoard.getBoard()[boardY2][boardX2];
            gameBoard.printBoardForTest();
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
