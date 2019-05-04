package com.itberries.technopark.itberries.websocket.games.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MPGamePlayer {
    private Long id;
    private LocalDateTime dateTimeStart;
    private List<MPGame> tasks = new ArrayList<>();
    private int currentPosition;

    public MPGamePlayer(Long id, Integer currentPosition) {
        this.id = id;
        this.dateTimeStart = dateTimeStart;
        this.currentPosition = currentPosition;
        //todo: сформировать задачи для пользователя Q-C-M  Q-C-M  Q-C-M

    }

    public List<MPGame> getTasks() {
        return tasks;
    }

    public void setTasks(List<MPGame> tasks) {
        this.tasks = tasks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTimeStart() {
        return dateTimeStart;
    }

    public void setDateTimeStart(LocalDateTime dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    public String getCurrentGameType() {
        return tasks.get(currentPosition).getType();
    }

    public String getCurrentGameAnswer() {
        return tasks.get(currentPosition).getAnswer();
    }


    /**
     * В случае верного ответа игрока сдвигаем на
     * одну позицию
     */
    public void movePosition() {
        currentPosition += 1;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
}
