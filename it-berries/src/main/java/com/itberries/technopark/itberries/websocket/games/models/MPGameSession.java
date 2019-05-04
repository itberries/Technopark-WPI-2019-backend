package com.itberries.technopark.itberries.websocket.games.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MPGameSession {
    private MPGamePlayer player1;
    private MPGamePlayer player2;

    private List<MPGame> tasks = new ArrayList<>();


    private Map<MPGamePlayer, List<MPGame>> states = new HashMap<MPGamePlayer, List<MPGame>>();

    public MPGameSession(MPGamePlayer player1, MPGamePlayer player2, List<MPGame> tasks) {
        this.player1 = player1;
        this.player2 = player2;
        this.tasks = tasks;
        //todo: достаточно хранения в сессии игры, избавиться от избычтоного хранения
        this.player1.setTasks(tasks);
        this.player1.setTasks(tasks);
    }

    public List<MPGame> getTasks() {
        return tasks;
    }

    public void setTasks(List<MPGame> tasks) {
        this.tasks = tasks;
    }

    public MPGamePlayer getPlayer1() {
        return player1;
    }

    public void setPlayer1(MPGamePlayer player1) {
        this.player1 = player1;
    }

    public MPGamePlayer getPlayer2() {
        return player2;
    }

    public void setPlayer2(MPGamePlayer player2) {
        this.player2 = player2;
    }
}
