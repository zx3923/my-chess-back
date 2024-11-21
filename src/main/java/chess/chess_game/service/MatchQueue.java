package chess.chess_game.service;

import chess.chess_game.entity.User;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;

@Component
public class MatchQueue {
    private final Queue<User> queue = new LinkedList<>();

    public synchronized void addToQueue(User user) {
        queue.add(user);
    }

    public synchronized User getFromQueue() {
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized int size() {
        return queue.size();
    }
}