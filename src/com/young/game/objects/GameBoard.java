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
import com.young.game.ui.CanvasGameOp;

import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class GameBoard {
    private int previousPoint; // for test
    private int point;
    private Friend[][] board;
    private Friend[] latestSwappedFriends;
    private LinkedList<Friend> linkedListOfBoard; // 보드에 만들에 만든어진 Friends 기억, 처음엔 HashSet으로 했지만 순서가 중요하다는 것을 깨닫고 ArrayList씀
    private GameBoardQueue<Friend> queueOfVerifiedInitializedBoard;

    /* 게임보드의 Canvas 상 left - top 좌표*/
    public static final int DEFAULT_X;
    public static final int DEFAULT_Y;

    public static final int FRIENDS_COUNT;
    public static final int BOARD_LENGTH;

    private static GameBoard instance;

    static {
        DEFAULT_X = 0 + 4 * CanvasGameOp.DW;
        DEFAULT_Y = 0 + 4 * CanvasGameOp.DH;
        FRIENDS_COUNT = 8;
        BOARD_LENGTH = 7;
    }

    private GameBoard() {
        board = new Friend[BOARD_LENGTH][BOARD_LENGTH];
        latestSwappedFriends = new Friend[2];
        linkedListOfBoard = new LinkedList<>();
        queueOfVerifiedInitializedBoard = new GameBoardQueue();
    }

    public static GameBoard getInstance() {
        if (instance == null)
            instance = new GameBoard();
        return instance;
    }

    public static void reset() {
        instance.board = null;
        instance = null;
    }

    /* Getters */
    public Friend getFriend(int x, int y) {
        return board[y][x];
    }

    public LinkedList<Friend> getLinkedListOfBoard() {
        return linkedListOfBoard;
    }

    public int getPoint() {
        return point;
    }

    public void update() {
        /* 마우스이벤트에의해 swap이 되었다면, 올바른 이동인지 확인 후 그렇지 않다면, 다시 역스왑*/
        if (latestSwappedFriends[0] != null
                && latestSwappedFriends[1] != null
                && !checkValidMoving()) {
            swap(latestSwappedFriends[0], latestSwappedFriends[1]);
        }

        latestSwappedFriends[0] = null;
        latestSwappedFriends[1] = null;

        int removedCount = remove(board);
        addPoints(removedCount);

        // for test
        if (previousPoint != point) {
            System.out.println("Point : " + point);
            previousPoint = point;
        }
    }

    public void draw(Graphics g) {
        int dw = CanvasGameOp.DW;
        int dh = CanvasGameOp.DH;

        g.setColor(Color.BLACK);
        g.fillRect(DEFAULT_X, DEFAULT_Y, 7 * 2 * dw, 7 * 2 * dh);

        for (int y = 0; y < BOARD_LENGTH; y++)
            for (int x = 0; x < BOARD_LENGTH; x++) {
                g.setColor(Color.WHITE);
                g.drawRect(DEFAULT_X + x * 2 * dw, DEFAULT_Y + y * 2 * dh, 2 * dw, 2 * dh);
                if (board[y][x] != null)
                    board[y][x].draw(g);
            }

        g.setColor(Color.BLACK);
        g.drawRect(DEFAULT_X, DEFAULT_Y, 7 * 2 * dw, 7 * 2 * dh);
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
                Friend f = null;

                if (queueOfVerifiedInitializedBoard.size() != 0)
                    f = queueOfVerifiedInitializedBoard.dequeue();
                else
                    f = getElementRandomly(x, 0);

                board[0][x] = f;
                linkedListOfBoard.add(f);
            }
    }

    public void swap(Friend f1, Friend f2) {
        f1.moveTo(f2);
        f2.moveTo(f1);

        int x1 = f1.getBoardX();
        int y1 = f1.getBoardY();
        int x2 = f2.getBoardX();
        int y2 = f2.getBoardY();

        Friend tmp = board[y1][x1];
        board[y1][x1] = board[y2][x2];
        board[y2][x2] = tmp;

        f1.setBoardX(x2);
        f1.setBoardY(y2);

        f2.setBoardX(x1);
        f2.setBoardY(y1);

        latestSwappedFriends[0] = f1;
        latestSwappedFriends[1] = f2;
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
    private int remove(Friend[][] board) {
        HashSet<Friend> removals = new HashSet<>();

        /* 판을 다 훑으며 제거할 수 있는 객체들을 넣어놓음, 중복 방지를 위해 hashSet 자료형을 이용 */
        for (int y = 0; y < BOARD_LENGTH; y++) {
            for (int x = 0; x < BOARD_LENGTH; x++) {
                Point p = new Point(x, y);
                if (isRemovableVertical(board, p) || isRemovableHorizontal(board, p))
                    removals.add(board[y][x]);
            }
        }

        /* 제거할 객체들의 자리에 null */
        for (Friend f : removals) {
            int x = f.getBoardX();
            int y = f.getBoardY();
            board[y][x] = null;
        }

        /* 옳은 코드인가? */
        if (board == this.board) {
            for (Friend f : removals)
                linkedListOfBoard.remove(f);
        }

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
        Friend f1 = latestSwappedFriends[0];
        Friend f2 = latestSwappedFriends[1];
        Point p1 = new Point(f1.getBoardX(), f1.getBoardY());
        Point p2 = new Point(f2.getBoardX(), f2.getBoardY());
        return isRemovableHorizontal(board, p1) || isRemovableVertical(board, p1) || isRemovableHorizontal(board, p2) || isRemovableVertical(board, p2);
    }

    private boolean isRemovableVertical(Friend[][] board, Point p) {
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

    private boolean isRemovableHorizontal(Friend[][] board, Point p) {
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
        Friend[][] deepCopiedBoard = copyDeeplyGameBoardForValidation();

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

        linkedListOfBoard.clear();

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

            swapForValidation(deepCopiedBoard, p, p2);
            boolean bP1RemovableVertical = isRemovableVertical(deepCopiedBoard, p);
            boolean bP1RemovableHorizon = isRemovableHorizontal(deepCopiedBoard, p);
            boolean bP2RemovableVertical = isRemovableVertical(deepCopiedBoard, p2);
            boolean bP2RemovableHorizon = isRemovableHorizontal(deepCopiedBoard, p2);
            swapForValidation(deepCopiedBoard, p, p2);

            if (bP1RemovableHorizon || bP1RemovableVertical || bP2RemovableHorizon || bP2RemovableVertical)
                return true;
        }

        /* 아래쪽obj과 교환 했을 때 vertical & horizon 검사*/
        if (y < BOARD_LENGTH - 1) {
            Point p2 = new Point(x, y + 1);

            swapForValidation(deepCopiedBoard, p, p2);
            boolean bP1RemovableVertical = isRemovableVertical(deepCopiedBoard, p);
            boolean bP1RemovableHorizon = isRemovableHorizontal(deepCopiedBoard, p);
            boolean bP2RemovableVertical = isRemovableVertical(deepCopiedBoard, p2);
            boolean bP2RemovableHorizon = isRemovableHorizontal(deepCopiedBoard, p2);
            swapForValidation(deepCopiedBoard, p, p2);

            if (bP1RemovableHorizon || bP1RemovableVertical || bP2RemovableHorizon || bP2RemovableVertical)
                return true;
        }

        /* 왼쪽obj과 교환 했을 때 vertical & horizon 검사*/
        if (x > 0) {
            Point p2 = new Point(x - 1, y);

            swapForValidation(deepCopiedBoard, p, p2);
            boolean bP1RemovableVertical = isRemovableVertical(deepCopiedBoard, p);
            boolean bP1RemovableHorizon = isRemovableHorizontal(deepCopiedBoard, p);
            boolean bP2RemovableVertical = isRemovableVertical(deepCopiedBoard, p2);
            boolean bP2RemovableHorizon = isRemovableHorizontal(deepCopiedBoard, p2);
            swapForValidation(deepCopiedBoard, p, p2);

            if (bP1RemovableHorizon || bP1RemovableVertical || bP2RemovableHorizon || bP2RemovableVertical)
                return true;
        }

        /* 오른쪽obj과 교환 했을 때 vertical & horizon 검사*/
        if (x < BOARD_LENGTH - 1) {
            Point p2 = new Point(x + 1, y);

            swapForValidation(deepCopiedBoard, p, p2);
            boolean bP1RemovableVertical = isRemovableVertical(deepCopiedBoard, p);
            boolean bP1RemovableHorizon = isRemovableHorizontal(deepCopiedBoard, p);
            boolean bP2RemovableVertical = isRemovableVertical(deepCopiedBoard, p2);
            boolean bP2RemovableHorizon = isRemovableHorizontal(deepCopiedBoard, p2);
            swapForValidation(deepCopiedBoard, p, p2);

            if (bP1RemovableHorizon || bP1RemovableVertical || bP2RemovableHorizon || bP2RemovableVertical)
                return true;
        }

        return false;
    }

    private Friend[][] copyDeeplyGameBoardForValidation() {
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

    private void swapForValidation(Friend[][] deepCopiedBoard, Point p1, Point p2) {
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
    public void makeQueueOfVerifiedInitializedBoard() {
        Friend[][] verifiedBoard = makeVerifiedInitializedBoard();

        int dw = CanvasGameOp.DW;

        for (int y = 0; y < BOARD_LENGTH; y++) {
            for (int x = 0; x < BOARD_LENGTH; x++) {
                Friend f = verifiedBoard[BOARD_LENGTH - 1 - y][x];
                /* 보드의 탑라인으로 boardY, canvasY, validCanvasY을 초기화 -> 죄다 */
                f.setBoardY(0);
                f.setBoardX(x);
                f.setCanvasX(DEFAULT_X + x * 2 * dw);
                f.setCanvasY(DEFAULT_Y);
                f.setValidCanvasX(DEFAULT_X + x * 2 * dw);
                f.setValidCanvasY(DEFAULT_Y);
                /* bottom - left 부터 top - right까지 queue를 만듦 */
                queueOfVerifiedInitializedBoard.enqueue(f);
            }
        }
    }

    private Friend[][] makeVerifiedInitializedBoard() {
        while (true) {
            Friend[][] verifiedInitializedBoard = new Friend[BOARD_LENGTH][BOARD_LENGTH];

            /* 초기화 된 tmpBoard를 랜덤으로 채우기*/
            for (int y = 0; y < BOARD_LENGTH; y++)
                for (int x = 0; x < BOARD_LENGTH; x++)
                    verifiedInitializedBoard[y][x] = getElementRandomly(x, y);

            while (remove(verifiedInitializedBoard) != 0) {
                /* 3개 이상으로 연속배치된 object들 지우고 난 후 그 자리에 다시 채우기 */
                for (int y = 0; y < BOARD_LENGTH; y++)
                    for (int x = 0; x < BOARD_LENGTH; x++)
                        if (verifiedInitializedBoard[y][x] == null)
                            verifiedInitializedBoard[y][x] = getElementRandomly(x, y);
            }

            /* 게임을 진행할 수 있는 board인지 판단, 진행가능하면 바로 tmpBoard를 반환*/
            for (int y = 0; y < BOARD_LENGTH; y++)
                for (int x = 0; x < BOARD_LENGTH; x++)
                    if (preCheckRemovablePositionWhenMoving(verifiedInitializedBoard, new Point(x, y)))
                        return verifiedInitializedBoard;
        }
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

    public boolean isEmptyBelow(Friend friend) {
        return friend.getBoardY() != BOARD_LENGTH - 1 && board[friend.getBoardY() + 1][friend.getBoardX()] == null;
    }

    public void alteredSlipDown(Friend friend) {
        int x = friend.getBoardX();
        int y = friend.getBoardY();
        board[y][x] = null;
        board[y + 1][x] = friend;
    }
}
