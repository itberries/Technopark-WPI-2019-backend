package com.itberries.technopark.itberries.websocket.games.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MPGame {
    private Long id;
    private String task;
    @JsonIgnore
    private String answer;
    private String type;
    private Boolean resolved;

    public MPGame(Long id, String task, String answer, String type) {
        this.id = id;
        this.task = task;
        this.answer = answer;
        this.type = type;
        this.resolved = Boolean.FALSE;
    }

    public Boolean getResolved() {
        return resolved;
    }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }

    @Override
    public String toString() {
        return "MPGame{" +
                "id=" + id +
                ", task='" + task + '\'' +
                ", answer='" + answer + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MPGame(String answer) {
        this.answer = answer;
    }

    @JsonIgnore
    public String getAnswer() {
        return answer;
    }

    @JsonIgnore
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
