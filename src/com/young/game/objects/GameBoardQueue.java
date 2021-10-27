package com.young.game.objects;

import java.util.LinkedList;

public class GameBoardQueue<T> {
    private LinkedList<T> friendLinkedList;

    public GameBoardQueue() {
        friendLinkedList = new LinkedList<>();
    }

    public void enqueue(T obj) {
        friendLinkedList.addLast(obj);
    }

    public T dequeue() {
        return friendLinkedList.removeFirst();
    }

    public int size() {
        return friendLinkedList.size();
    }
}
