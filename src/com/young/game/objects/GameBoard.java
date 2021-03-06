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
    private Friend[][] logicBoard;
    private Friend[] latestSwappedFriends;
    private LinkedList<Friend> linkedListOfBoardForThreadUpdating; // 보드에 만들에 만든어진 Friends 기억, 처음엔 HashSet으로 했지만 leftBottom ~ rightTop 순서로 각 캐릭터를 update 해야하기 때문에 순서가 중요하다는 것을 깨닫고 ArrayList 사용
    private GameBoardQueue<Friend> queueOfVerifiedInitializedBoard;

    /* 게임보드의 Canvas 상 left - top 좌표*/
    public static final int DEFAULT_CANVAS_X;
    public static final int DEFAULT_CANVAS_Y;

    public static final int FRIENDS_COUNT;
    public static final int BOARD_LENGTH;

    private static GameBoard instance;

    static {
        DEFAULT_CANVAS_X = 0 + 4 * CanvasGameOp.DW;
        DEFAULT_CANVAS_Y = 0 + 4 * CanvasGameOp.DH;
        FRIENDS_COUNT = 8;
        BOARD_LENGTH = 7;
    }

    private GameBoard() {
        logicBoard = new Friend[BOARD_LENGTH][BOARD_LENGTH];
        latestSwappedFriends = new Friend[2];
        linkedListOfBoardForThreadUpdating = new LinkedList<>();
        queueOfVerifiedInitializedBoard = new GameBoardQueue<>();
    }

    public static GameBoard getInstance() {
        if (instance == null)
            instance = new GameBoard();
        return instance;
    }

    public static void reset() {
        instance.logicBoard = null;
        instance = null;
    }

    /* Getters */
    public Friend getFriend(int x, int y) {
        return logicBoard[y][x];
    }

    public LinkedList<Friend> getLinkedListOfBoardForThreadUpdating() {
        return linkedListOfBoardForThreadUpdating;
    }

    public int getPoint() {
        return point;
    }

    public void update() {
        /* 마우스이벤트에의해 swap이 되었다면, 올바른 이동인지 확인 후 그렇지 않다면, 다시 역스왑*/
        if (latestSwappedFriends[0] != null
                && latestSwappedFriends[1] != null
                && !checkFriendMovingValid()) {
            swap(latestSwappedFriends[0], latestSwappedFriends[1]);
        }

        latestSwappedFriends[0] = null;
        latestSwappedFriends[1] = null;

        int removedCount = remove(logicBoard);
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
        g.fillRect(DEFAULT_CANVAS_X, DEFAULT_CANVAS_Y, 7 * 2 * dw, 7 * 2 * dh);

        for (int y = 0; y < BOARD_LENGTH; y++)
            for (int x = 0; x < BOARD_LENGTH; x++) {
                g.setColor(Color.WHITE);
                g.drawRect(DEFAULT_CANVAS_X + x * 2 * dw, DEFAULT_CANVAS_Y + y * 2 * dh, 2 * dw, 2 * dh);
                if (logicBoard[y][x] != null)
                    logicBoard[y][x].draw(g);
            }

        g.setColor(Color.BLACK);
        g.drawRect(DEFAULT_CANVAS_X, DEFAULT_CANVAS_Y, 7 * 2 * dw, 7 * 2 * dh);
    }

    /*
     * game base operation
     */
    public boolean isEmptyBelow(Friend friend) {
        return (friend.getLogicBoardY() != BOARD_LENGTH - 1) && (logicBoard[friend.getLogicBoardY() + 1][friend.getLogicBoardX()] == null);
    }

    public void noticeSlipDownOf(Friend friend) {
        int x = friend.getLogicBoardX();
        int y = friend.getLogicBoardY();
        logicBoard[y][x] = null;
        logicBoard[y + 1][x] = friend;
    }

    public boolean isFull() {
        for (int y = 0; y < BOARD_LENGTH; y++) {
            for (int x = 0; x < BOARD_LENGTH; x++)
                if (logicBoard[y][x] == null)
                    return false;
        }
        return true;
    }

    public void fillTopLine() { // 보드의 맨 윗 줄(1행)에 빈 곳이 있다면 캐릭터를 랜덤으로 채워넣는다.
        for (int x = 0; x < BOARD_LENGTH; x++)
            if (logicBoard[0][x] == null) {
                Friend f = null;

                if (queueOfVerifiedInitializedBoard.size() != 0) // 보드를 초기화할 때 작동하는 코드, 미리 검증된 보드를 큐에 집어 넣어 (leftBottom ~ rightTop) 하나씩 꺼낸다.
                    f = queueOfVerifiedInitializedBoard.dequeue();
                else // 게임 중 정답처리되어 부분부분 빌때 랜덤으로 하나씩 넣음
                    f = getElementRandomly(x, 0);

                logicBoard[0][x] = f;
                linkedListOfBoardForThreadUpdating.add(f);
            }
    }

    public void swap(Friend f1, Friend f2) {
        // 화면에 보이는 Canvas 위치를 이동함
        f1.moveToOnCanvas(f2);
        f2.moveToOnCanvas(f1);

        // 게임 로직 계산을 위한 2차원 배열인 GameBoard 위치를 이동함
        int x1 = f1.getLogicBoardX();
        int y1 = f1.getLogicBoardY();
        int x2 = f2.getLogicBoardX();
        int y2 = f2.getLogicBoardY();

        Friend tmp = logicBoard[y1][x1];
        logicBoard[y1][x1] = logicBoard[y2][x2];
        logicBoard[y2][x2] = tmp;

        // 각 캐릭터도 자신의 logic board 위치를 기억
        f1.setLogicBoardX(x2);
        f1.setLogicBoardY(y2);

        f2.setLogicBoardX(x1);
        f2.setLogicBoardY(y1);

        // 역스왑을 위해 지금 swap한 캐릭터 두 개를 기억
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
            int x = f.getLogicBoardX();
            int y = f.getLogicBoardY();
            board[y][x] = null;
        }

        /* 옳은 코드인가? => 실전 배치 된 판 인 경우 이 조건의 코드가 실행 됨, 실전 배치 전 verifiedInitializedBoard 는 해당하지 않음*/
        if (board == this.logicBoard) {
            for (Friend f : removals)
                linkedListOfBoardForThreadUpdating.remove(f);
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
    private boolean checkFriendMovingValid() {
        Friend f1 = latestSwappedFriends[0];
        Friend f2 = latestSwappedFriends[1];
        Point p1 = new Point(f1.getLogicBoardX(), f1.getLogicBoardY());
        Point p2 = new Point(f2.getLogicBoardX(), f2.getLogicBoardY());
        return isRemovableHorizontal(logicBoard, p1) || isRemovableVertical(logicBoard, p1) || isRemovableHorizontal(logicBoard, p2) || isRemovableVertical(logicBoard, p2);
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

        /* board, linkedListOfBoard 초기화*/
        for (int y = 0; y < BOARD_LENGTH; y++) {
            for (int x = 0; x < BOARD_LENGTH; x++)
                logicBoard[y][x] = null;
        }
        linkedListOfBoardForThreadUpdating.clear();

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
                Friend f = logicBoard[y][x];
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
        f1.setLogicBoardX(x1);
        f1.setLogicBoardY(y1);

        Friend f2 = board[y2][x2];
        f2.setLogicBoardX(x2);
        f2.setLogicBoardY(y2);
    }

    /* 3개 이상의 연속 배치가 없고, 답이 있는 보드인지 검증하여 판을 새로 갱신할 때 씀 - 1. 처음 게임을 시작할때 2. 답이 없는경우 판을 다시 갱신할 때 */
    public void makeQueueOfVerifiedInitializedBoard() {
        Friend[][] verifiedBoard = getVerifiedInitializedBoard();

        int dw = CanvasGameOp.DW;

        for (int y = 0; y < BOARD_LENGTH; y++) {
            for (int x = 0; x < BOARD_LENGTH; x++) {
                Friend f = verifiedBoard[BOARD_LENGTH - 1 - y][x];
                /* 보드의 탑라인으로 boardY, canvasY, validCanvasY을 초기화 -> 죄다 */
                f.setLogicBoardY(0);
                f.setLogicBoardX(x);
                f.setPresentCanvasX(DEFAULT_CANVAS_X + x * 2 * dw);
                f.setPresentCanvasY(DEFAULT_CANVAS_Y);
                f.setTargetCanvasX(DEFAULT_CANVAS_X + x * 2 * dw);
                f.setTargetCanvasY(DEFAULT_CANVAS_Y);
                /* bottom - left 부터 top - right 까지 queue 를 만듦 */
                queueOfVerifiedInitializedBoard.enqueue(f);
            }
        }
    }

    private Friend[][] getVerifiedInitializedBoard() {
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
                if (logicBoard[y][x] == null)
                    System.out.printf("%-8s", String.format("<null>"));
                else
                    System.out.printf("%-8s", String.format("[%s]", logicBoard[y][x].getName()));
            }
            System.out.println();
        }
    }

}
