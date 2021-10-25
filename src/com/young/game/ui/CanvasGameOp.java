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
    private static int width;
    private static int height;
    private static int dw;
    private static int dh;

    static {
        width = 680;
        height = 680;
        dw = width / 22;
        dh = height / 22;
    }


    private CanvasGameOp() {
        setSize(width, height);
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
    public static int getDw() {
        return dw;
    }

    public static int getDh() {
        return dh;
    }

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
        int obW = CanvasGameOp.this.getWidth();
        int obH = CanvasGameOp.this.getHeight();
        int dw = obW / 22;
        int dh = obH / 22;
        int pressedX;
        int pressedY;
        int releasedX;
        int releasedY;

        @Override
        public void mouseClicked(MouseEvent e) {
            Point p = e.getPoint();
            int mouseX = (int)p.getX();
            int mouseY = (int)p.getY();

            if (0 + 2 * dh <= mouseY && mouseY <= 0 + 3 * dh) {
                if (0 + 2 * dw <= mouseX && mouseX <= 0 + 6 * dw) {
                    playSound("click_mouse.wav");
                    CanvasMain.getInstance().setVisible(true);
                    CanvasGameOp.this.setVisible(false);
                    /* Game을 초기화 하는 코드*/
                    reset();
                }
                else if (0 + 16 * dh <= mouseX && mouseX <= 0 + 20 * dh) {
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
                if (0 + 9 * dw <= mouseX && mouseX <= 0 + 9 * dw + 4 * dw
                        && 0 + 20 * dh + dh / 2 <= mouseY && mouseY <= 0 + 20 * dh + dh / 2 + dh) {
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

            if (0 + 2 * dh <= mouseY && mouseY <= 0 + 3 * dh) {
                if (0 + 2 * dw <= mouseX && mouseX <= 0 + 6 * dw)
                    buttonBackMain.pointButton();
                else if (0 + 16 * dh <= mouseX && mouseX <= 0 + 20 * dh)
                    buttonPause.pointButton();
            }

            if (buttonNext != null) {
                buttonNext.outpointButton();
                if (0 + 9 * dw <= mouseX && mouseX <= 0 + 9 * dw + 4 * dw
                        && 0 + 20 * dh + dh / 2 <= mouseY && mouseY <= 0 + 20 * dh + dh / 2 + dh)
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

            if (!(0 + 4 * dw <= pressedX && pressedX <= 0 + 18 * dw
                    && 0 + 4 * dh <= pressedY && pressedY <= 0 + 18 * dh)) {
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
            if (!(0 + 4 * dw <= releasedX && releasedX <= 0 + 18 * dw
                    && 0 + 4 * dh <= releasedY && releasedY <= 0 + 18 * dh)) {
                initializePressedAndReleasedXY();
                return;
            }

            if ((pressedX == 0 && releasedY == 0)
                    || pressedX == releasedX && pressedY == releasedY) {
                initializePressedAndReleasedXY();
                return;
            }

            /* 마우스 릴리즈가 프레스 기준으로 어떤 경계 안으로 떨어졌는지 판단하고 그에 따라 board X2, Y2를 정함 */
            int boardX1 = (pressedX - 4 * dw) / (2 * dw);
            int boardY1 = (pressedY - 4 * dh) / (2 * dh);
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
            f1.moveTo(f2);
            f2.moveTo(f1);
            gameBoard.printBoardForTest();
            playSound("move_friend.wav");
            gameBoard.swap(new Point(f1.getBoardX(), f1.getBoardY()), new Point(f2.getBoardX(), f2.getBoardY()));
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
