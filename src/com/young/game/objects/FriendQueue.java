package com.young.game.objects;

import com.young.game.objects.Friends.Friend;

import java.util.LinkedList;

public class FriendQueue {
    private LinkedList<Friend> friendLinkedList;

    public FriendQueue() {
        friendLinkedList = new LinkedList<>();
    }

    public void enqueue(Friend friend) {
        friendLinkedList.addLast(friend);
    }

    public Friend dequeue() {
        return friendLinkedList.removeFirst();
    }

    public int size() {
        return friendLinkedList.size();
    }
}
