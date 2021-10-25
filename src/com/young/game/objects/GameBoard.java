package com.young.game.objects;

import com.young.game.objects.Friends.Apeach;
import com.young.game.objects.Friends.Friend;
import com.young.game.objects.Friends.Frodo;
import com.young.game.objects.Friends.JayG;
import com.young.game.objects.Friends.Kon;
import com.young.game.objects.Friends.Muzi;
import com.young.game.objects.Friends.Neo;
import com.young.game.objects.Friends.Ryan;
import com.young.game.objects.Friends.Tube;
import com.young.game.ui.CanvasMain;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class GameBoard {
    private static GameBoard instance;
    private final int FRIENDS_COUNT = 8;
    private final int BOARD_LENGTH = 7;
    private int point;
    private Friend[][] board;
    private ArrayList<Friend> friendsArrayList; // 보드에 만들에 만든어진 Friends 기억, 처음엔 HashSet으로 했지만 순서가 중요하다는 것을 깨닫고 ArrayList씀
    private Point[] latestMovedPosition;
    private LinkedList<Friend> LLFromVerifiedTmpBoard;
    private boolean[] bLineFullsForPlaySound;

    //for test
    private int previousPoint;

    /*Canvas 22등분 한 후 단위 길이  */
    private int dw;
    private int dh;

    /* 게임보드의 Canvas 상 left - top 좌표*/
    private int defaultX;
    private int defaultY;

    private GameBoard() {
        bLineFullsForPlaySound = new boolean[7];

        board = new Friend[BOARD_LENGTH][BOARD_LENGTH];
        friendsArrayList = new ArrayList<>();
        latestMovedPosition = new Point[2];
        LLFromVerifiedTmpBoard = new LinkedList<>();

        int obW = CanvasMain.getInstance().getWidth();
        int obH = CanvasMain.getInstance().getHeight();
        dw = obW / 22;
        dh = obH / 22;
        defaultX = 0 + 4 * dw;
        defaultY = 0 + 4 * dh;
    }

    public static GameBoard getInstance() {
        if (instance == null)
            instance = new GameBoard();
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    /* Getters */
    public Friend[][] getBoard() {
        return board;
    }

    public ArrayList<Friend> getFriendsArrayList() {
        return friendsArrayList;
    }

    public int getPoint() {
        return point;
    }

    public int getDw() {
        return dw;
    }

    public int getDh() {
        return dh;
    }

    public int getDefaultX() {
        return defaultX;
    }

    public int getDefaultY() {
        return defaultY;
    }

    public void update() {
        if (latestMovedPosition[0] != null
                && latestMovedPosition[1] != null
                && !checkValidMoving()) {
            Point p1 = latestMovedPosition[0];
            Point p2 = latestMovedPosition[1];
            Friend f1 = board[(int)p1.getY()][(int)p1.getX()];
            Friend f2 = board[(int)p2.getY()][(int)p2.getX()];
            f1.moveTo(f2);
            f2.moveTo(f1);
            swap(p1, p2);
        }
        latestMovedPosition = new Point[2];

        int removedCount = remove();
        addPoints(removedCount);


        // for test
        if (previousPoint != point) {
            System.out.println("Point : " + point);
            previousPoint = point;
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(defaultX, defaultY, 7 * 2 * dw, 7 * 2 * dh);
        for (int y = 0; y < BOARD_LENGTH; y++)
            for (int x = 0; x < BOARD_LENGTH; x++) {
                g.setColor(Color.WHITE);
                g.drawRect(defaultX + x * 2 * dw, defaultY + y * 2 * dh, 2 * dw, 2 * dh);
                if (board[y][x] != null)
                    board[y][x].draw(g);
            }
        g.setColor(Color.BLACK);
        g.drawRect(defaultX, defaultY, 7 * 2 * dw, 7 * 2 * dh);
    }

    /*
     * game base operation
     */
    public boolean isFull() {
        for (int y = 0; y < BOARD_LENGTH; y++) {
            for (int x = 0; x < BOARD_LENGTH; x++)
                if (board[y][x] == null)
                    return false;
        }
        return true;
    }

    public void fillTopLine() {
        for (int x = 0; x < BOARD_LENGTH; x++)
            if (board[0][x] == null) {
                Friend f;
                if (LLFromVerifiedTmpBoard.size() != 0) {
                    f = LLFromVerifiedTmpBoard.getFirst();
                    LLFromVerifiedTmpBoard.removeFirst();
                }
                else
                    f = getElementRandomly(x, 0);

                board[0][x] = f;
                friendsArrayList.add(f);
            }
    }

    public void swap(Point p1, Point p2) {
        int x1 = (int)p1.getX();
        int y1 = (int)p1.getY();
        int x2 = (int)p2.getX();
        int y2 = (int)p2.getY();


        Friend tmp = board[y1][x1];
        board[y1][x1] = board[y2][x2];
        board[y2][x2] = tmp;

        Friend f1 = board[y1][x1];
        if (f1 != null) {
            f1.setBoardX(x1);
            f1.setBoardY(y1);
        }

        Friend f2 = board[y2][x2];
        if (f2 != null) {
            f2.setBoardX(x2);
            f2.setBoardY(y2);
        }

        latestMovedPosition[0] = p1;
        latestMovedPosition[1] = p2;
    }

    private Friend getElementRandomly(int x, int y) {
        /* Friend 개체들 중 랜덤을 뽑아 객체를 만들어 반환*/
        Friend element = null;
        switch (new Random().nextInt(FRIENDS_COUNT)) {
            case 0:
                element = new Apeach(x, y);
                break;
            case 1:
                element = new Frodo(x, y);
                break;
            case 2:
                element = new JayG(x, y);
                break;
            case 3:
                element = new Kon(x, y);
                break;
            case 4:
                element = new Muzi(x, y);
                break;
            case 5:
                element = new Neo(x, y);
                break;
            case 6:
                element = new Ryan(x, y);
                break;
            case 7:
                element = new Tube(x, y);
                break;
            default:
                assert false : "Random number is out of bound";
        }

        return element;
    }

    /* for removing valid objects sequence and calculate points */
    private int remove() {
        HashSet<Friend> removals = new HashSet<>();

        /* 판을 다 훑으며 제거할 수 있는 객체들을 넣어놓음, 중복 방지를 위해 hashSet 자료형을 이용 */
        for (int y = 0; y < BOARD_LENGTH; y++) {
            for (int x = 0; x < BOARD_LENGTH; x++) {
                Point p = new Point(x, y);
                if (isRemovableVertical(p) || isRemovableHorizontal(p))
                    removals.add(board[y][x]);
            }
        }

        // for test
//        if (removals.size() != 0)
//            printBoardForTest();


        /* 제거할 객체들의 자리에 null */
        for (Friend f : removals) {
            int x = f.getBoardX();
            int y = f.getBoardY();
            board[y][x] = null;
        }

        for (Friend f : removals)
            friendsArrayList.remove(f);

        return removals.size(); // 제거한 객체의 수를 반환
    }

    private void addPoints(int removedCount) {
        point += removedCount * 123;
    }

    /*
     * operation for validation of game
     */

    /* obj가 교환했을 때 움직임이 타당한지(돌이 3개이상 배치가 되었는지) 체크해서 배치가 되었으면 참값을 반환*/
    private boolean checkValidMoving() {
        Point p1 = latestMovedPosition[0];
        Point p2 = latestMovedPosition[1];
        return isRemovableHorizontal(p1) || isRemovableVertical(p1) || isRemovableHorizontal(p2) || isRemovableVertical(p2);
    }

    private boolean isRemovableVertical(Point p) {
        /* '|' 방향 검사 */
        int count = 0;
        int x = (int)p.getX();
        int y = (int)p.getY();
        Friend friendAtP = board[y][x];

        /* 위쪽 방향 검사 */
        while (true) {
            if (0 <= y && friendAtP.getName().equals(board[y][x].getName()))
                count++;
            else
                break;
            y--;
        }

        /* 아래쪽 방향 검사 */
        y = (int)p.getY() + 1;

        while (true) {
            if (y < BOARD_LENGTH && friendAtP.getName().equals(board[y][x].getName()))
                count++;
            else
                break;
            y++;
        }

        if (count >= 3)
            return true;
        else
            return false;
    }

    private boolean isRemovableHorizontal(Point p) {
        /* 'ㅡ' 방향 검사 */

        int count = 0;
        int x = (int)p.getX();
        int y = (int)p.getY();
        Friend friendAtP = board[y][x];

        /* 왼쪽 방향 검사 */
        while (true) {
            if (0 <= x && friendAtP.getName().equals(board[y][x].getName()))
                count++;
            else
                break;

            x--;
        }

        /* 오른쪽 방향 검사*/
        x = (int)p.getX() + 1;
        while (true) {
            if (x < BOARD_LENGTH && friendAtP.getName().equals(board[y][x].getName()))
                count++;
            else
                break;

            x++;
        }

        if (count >= 3)
            return true;
        else
            return false;
    }

    /* (아래부터) board가 게임을 진행할 수 있는 상태인지 검사하는 메소드들 */
    public boolean checkValidationBoardAndClearBoard() {
        /* 게임을 진행할 수 있는 보드인지 검사하고 그렇지 않으면 새로 보드를 갱신하는 메서드*/
        /* 보드를 깊은 복사한다. 테스르를 위해 움직여야하므로*/
        Friend[][] deepCopiedBoard = copyGameBoard();

        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 7; x++)
                if (preCheckRemovablePositionWhenMoving(deepCopiedBoard, new Point(x, y)))
                    return true;
        }

        /* board를 초기화*/
        for (int y = 0; y < BOARD_LENGTH; y++) {
            for (int x = 0; x < BOARD_LENGTH; x++)
                board[y][x] = null;
        }

        friendsArrayList.clear();

        System.out.println("invalid board");
        return false;
    }

    private boolean preCheckRemovablePositionWhenMoving(Friend[][] deepCopiedBoard, Point p) {
        int x = (int)p.getX();
        int y = (int)p.getY();

        /* 검사하고 obj들을 다시 되돌려놓는 것을 원칙 (swap 후 역swap) */
        /* 위쪽obj과 교환 했을 때 vertical & horizon 검사*/
        if (y > 0) {
            Point p2 = new Point(x, y - 1);

            swapForValidating(deepCopiedBoard, p, p2);
            boolean bP1RemovableVertical = preCheckRemovableVertical(deepCopiedBoard, p);
            boolean bP1RemovableHorizon = preCheckRemovableHorizontal(deepCopiedBoard, p);
            boolean bP2RemovableVertical = preCheckRemovableVertical(deepCopiedBoard, p2);
            boolean bP2RemovableHorizon = preCheckRemovableHorizontal(deepCopiedBoard, p2);
            swapForValidating(deepCopiedBoard, p, p2);

            if (bP1RemovableHorizon || bP1RemovableVertical || bP2RemovableHorizon || bP2RemovableVertical)
                return true;
        }

        /* 아래쪽obj과 교환 했을 때 vertical & horizon 검사*/
        if (y < BOARD_LENGTH - 1) {
            Point p2 = new Point(x, y + 1);

            swapForValidating(deepCopiedBoard, p, p2);
            boolean bP1RemovableVertical = preCheckRemovableVertical(deepCopiedBoard, p);
            boolean bP1RemovableHorizon = preCheckRemovableHorizontal(deepCopiedBoard, p);
            boolean bP2RemovableVertical = preCheckRemovableVertical(deepCopiedBoard, p2);
            boolean bP2RemovableHorizon = preCheckRemovableHorizontal(deepCopiedBoard, p2);
            swapForValidating(deepCopiedBoard, p, p2);

            if (bP1RemovableHorizon || bP1RemovableVertical || bP2RemovableHorizon || bP2RemovableVertical)
                return true;
        }

        /* 왼쪽obj과 교환 했을 때 vertical & horizon 검사*/
        if (x > 0) {
            Point p2 = new Point(x - 1, y);

            swapForValidating(deepCopiedBoard, p, p2);
            boolean bP1RemovableVertical = preCheckRemovableVertical(deepCopiedBoard, p);
            boolean bP1RemovableHorizon = preCheckRemovableHorizontal(deepCopiedBoard, p);
            boolean bP2RemovableVertical = preCheckRemovableVertical(deepCopiedBoard, p2);
            boolean bP2RemovableHorizon = preCheckRemovableHorizontal(deepCopiedBoard, p2);
            swapForValidating(deepCopiedBoard, p, p2);

            if (bP1RemovableHorizon || bP1RemovableVertical || bP2RemovableHorizon || bP2RemovableVertical)
                return true;
        }

        /* 오른쪽obj과 교환 했을 때 vertical & horizon 검사*/
        if (x < BOARD_LENGTH - 1) {
            Point p2 = new Point(x + 1, y);

            swapForValidating(deepCopiedBoard, p, p2);
            boolean bP1RemovableVertical = preCheckRemovableVertical(deepCopiedBoard, p);
            boolean bP1RemovableHorizon = preCheckRemovableHorizontal(deepCopiedBoard, p);
            boolean bP2RemovableVertical = preCheckRemovableVertical(deepCopiedBoard, p2);
            boolean bP2RemovableHorizon = preCheckRemovableHorizontal(deepCopiedBoard, p2);
            swapForValidating(deepCopiedBoard, p, p2);

            if (bP1RemovableHorizon || bP1RemovableVertical || bP2RemovableHorizon || bP2RemovableVertical)
                return true;
        }

        return false;
    }

    private boolean preCheckRemovableHorizontal(Friend[][] deepCopiedBoard, Point p) {
        /* 'ㅡ' 방향 검사 */

        int count = 0;
        int x = (int)p.getX();
        int y = (int)p.getY();
        Friend friendAtP = deepCopiedBoard[y][x];

        /* 왼쪽 방향 검사 */
        while (true) {
            if (0 <= x && friendAtP.getName().equals(deepCopiedBoard[y][x].getName())) {
                count++;
            }
            else
                break;

            x--;
        }

        /* 오른쪽 방향 검사*/
        x = (int)p.getX() + 1;
        while (true) {
            if (x < BOARD_LENGTH && friendAtP.getName().equals(deepCopiedBoard[y][x].getName())) {
                count++;
            }
            else
                break;

            x++;
        }

        if (count >= 3)
            return true;
        else
            return false;
    }

    private boolean preCheckRemovableVertical(Friend[][] deepCopiedBoard, Point p) {
        /* '|' 방향 검사 */
        int count = 0;
        int x = (int)p.getX();
        int y = (int)p.getY();
        Friend friendAtP = deepCopiedBoard[y][x];

        /* 위쪽 방향 검사 */
        while (true) {
            if (0 <= y && friendAtP.getName().equals(deepCopiedBoard[y][x].getName()))
                count++;
            else
                break;

            y--;
        }

        /* 아래쪽 방향 검사 */
        y = (int)p.getY() + 1;

        while (true) {
            if (y < BOARD_LENGTH && friendAtP.getName().equals(deepCopiedBoard[y][x].getName()))
                count++;
            else
                break;

            y++;
        }

        if (count >= 3)
            return true;
        else
            return false;
    }

    private Friend[][] copyGameBoard() {
        Friend[][] deepCopyBoard = new Friend[BOARD_LENGTH][BOARD_LENGTH];

        for (int y = 0; y < BOARD_LENGTH; y++) {
            for (int x = 0; x < BOARD_LENGTH; x++) {
                Friend f = board[y][x];
                switch (f.getName()) {
                    case "Apeach":
                        deepCopyBoard[y][x] = new Apeach(x, y);
                        break;
                    case "Frodo":
                        deepCopyBoard[y][x] = new Frodo(x, y);
                        break;
                    case "JayG":
                        deepCopyBoard[y][x] = new JayG(x, y);
                        break;
                    case "Kon":
                        deepCopyBoard[y][x] = new Kon(x, y);
                        break;
                    case "Muzi":
                        deepCopyBoard[y][x] = new Muzi(x, y);
                        break;
                    case "Neo":
                        deepCopyBoard[y][x] = new Neo(x, y);
                        break;
                    case "Ryan":
                        deepCopyBoard[y][x] = new Ryan(x, y);
                        break;
                    case "Tube":
                        deepCopyBoard[y][x] = new Tube(x, y);
                        break;
                    default:
                        assert false : "Random number is out of bound";
                }
            }
        }

        return deepCopyBoard;
    }

    private void swapForValidating(Friend[][] deepCopiedBoard, Point p1, Point p2) {
        int x1 = (int)p1.getX();
        int y1 = (int)p1.getY();
        int x2 = (int)p2.getX();
        int y2 = (int)p2.getY();

        Friend[][] board = deepCopiedBoard;

        Friend tmp = board[y1][x1];
        board[y1][x1] = board[y2][x2];
        board[y2][x2] = tmp;

        Friend f1 = board[y1][x1];
        if (f1 != null) {
            f1.setBoardX(x1);
            f1.setBoardY(y1);
        }

        Friend f2 = board[y2][x2];
        if (f2 != null) {
            f2.setBoardX(x2);
            f2.setBoardY(y2);
        }
    }

    /* 3개 이상의 연속 배치가 없고, 답이 있는 보드를 검증하여 판을 새로 갱신할 때 씀 - 1. 처음 게임을 시작할때 2. 답이 없는경우 판을 다시 갱신할 때 */
    public void initializeToVerifiedBoard() {
        Friend[][] tmpBoard = initializeTmpBoard();

        for (int y = 0; y < BOARD_LENGTH; y++) {
            for (int x = 0; x < BOARD_LENGTH; x++) {
                Friend f = tmpBoard[BOARD_LENGTH - 1 - y][x];
                /* 보드의 탑라인으로 boardY, canvasY, validCanvasY을 초기화 -> 죄다 */
                f.setBoardY(0);
                f.setBoardX(x);
                f.setCanvasX(defaultX + x * 2 * dw);
                f.setCanvasY(defaultY);
                f.setValidCanvasX(defaultX + x * 2 * dw);
                f.setValidCanvasY(defaultY);
                LLFromVerifiedTmpBoard.addLast(f);
            }
        }
    }

    private Friend[][] initializeTmpBoard() {
        while (true) {
            Friend[][] tmpBoard = new Friend[BOARD_LENGTH][BOARD_LENGTH];

            /* 초기화 된 tmpBoard를 랜덤으로 채우기*/
            for (int y = 0; y < BOARD_LENGTH; y++)
                for (int x = 0; x < BOARD_LENGTH; x++)
                    tmpBoard[y][x] = getElementRandomly(x, y);

            while (removeForInitializeTmpBoard(tmpBoard) != 0) {
                /* 3개 이상으로 연속배치된 object들 지우고 난 후 그 자리에 다시 채우기 */
                for (int y = 0; y < BOARD_LENGTH; y++)
                    for (int x = 0; x < BOARD_LENGTH; x++)
                        if (tmpBoard[y][x] == null)
                            tmpBoard[y][x] = getElementRandomly(x, y);
            }

            /* 게임을 진행할 수 있는 board인지 판단, 진행가능하면 바로 tmpBoard를 반환*/
            for (int y = 0; y < BOARD_LENGTH; y++)
                for (int x = 0; x < BOARD_LENGTH; x++)
                    if (preCheckRemovablePositionWhenMoving(tmpBoard, new Point(x, y)))
                        return tmpBoard;
        }
    }

    private int removeForInitializeTmpBoard(Friend[][] tmpBoard) {
        HashSet<Friend> removals = new HashSet<>();

        /* 판을 다 훑으며 제거할 수 있는 객체들을 넣어놓음, 중복 방지를 위해 hashSet 자료형을 이용 */
        for (int y = 0; y < BOARD_LENGTH; y++) {
            for (int x = 0; x < BOARD_LENGTH; x++) {
                Point p = new Point(x, y);
                if (isRemovableVertical(tmpBoard, p) || isRemovableHorizontal(tmpBoard, p))
                    removals.add(tmpBoard[y][x]);
            }
        }

        /* 제거할 객체들의 자리에 null 값을 넣음*/
        for (Friend f : removals) {
            int x = f.getBoardX();
            int y = f.getBoardY();
            tmpBoard[y][x] = null;
        }

        return removals.size(); // 제거한 객체의 수를 반환
    }

    private boolean isRemovableVertical(Friend[][] tmpBoard, Point p) {
        /* '|' 방향 검사 */
        int count = 0;
        int x = (int)p.getX();
        int y = (int)p.getY();
        Friend friendAtP = tmpBoard[y][x];

        /* 위쪽 방향 검사 */
        while (true) {
            if (0 <= y && friendAtP.getName().equals(tmpBoard[y][x].getName()))
                count++;
            else
                break;
            y--;
        }

        /* 아래쪽 방향 검사 */
        y = (int)p.getY() + 1;

        while (true) {
            if (y < tmpBoard.length && friendAtP.getName().equals(tmpBoard[y][x].getName()))
                count++;
            else
                break;
            y++;
        }

        if (count >= 3)
            return true;
        else
            return false;
    }

    private boolean isRemovableHorizontal(Friend[][] tmpBoard, Point p) {
        /* 'ㅡ' 방향 검사 */

        int count = 0;
        int x = (int)p.getX();
        int y = (int)p.getY();
        Friend friendAtP = tmpBoard[y][x];

        /* 왼쪽 방향 검사 */
        while (true) {
            if (0 <= x && friendAtP.getName().equals(tmpBoard[y][x].getName()))
                count++;
            else
                break;

            x--;
        }

        /* 오른쪽 방향 검사*/
        x = (int)p.getX() + 1;
        while (true) {
            if (x < tmpBoard.length && friendAtP.getName().equals(tmpBoard[y][x].getName()))
                count++;
            else
                break;

            x++;
        }

        if (count >= 3)
            return true;
        else
            return false;
    }

    /* print board at console for debugging */
    public void printBoardForTest() {
        for (int y = 0; y < BOARD_LENGTH; y++) {
            for (int x = 0; x < BOARD_LENGTH; x++) {
                if (board[y][x] == null)
                    System.out.printf("%-8s", String.format("<null>"));
                else
                    System.out.printf("%-8s", String.format("[%s]", board[y][x].getName()));
            }
            System.out.println();
        }
    }

    public void playSoundWhenLineIsFilled() {
        for (int y = 0; y < 7; y++)
            isFullThisLine(6 - y);
    }

    private void isFullThisLine(int y) {
        for (int x = 0; x < BOARD_LENGTH; x++) {
            if (board[y][x] == null) {
                bLineFullsForPlaySound[y] = false;
                return;
            }
        }

        if (bLineFullsForPlaySound[y] == false) {
            playSound("alter_end.wav");
            bLineFullsForPlaySound[y] = true;
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
