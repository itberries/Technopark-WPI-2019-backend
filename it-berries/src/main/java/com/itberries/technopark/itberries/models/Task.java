package com.itberries.technopark.itberries.models;

import java.util.Objects;

public class Task {
    private Long id;
    private String type;
    private String level;
    private String text;
    private String subject;
    private String pictureUrl;
    private String answer;

    public Task(Long id, String type, String level, String text, String subject, String pictureUrl, String answer) {
        this.id = id;
        this.type = type;
        this.level = level;
        this.text = text;
        this.subject = subject;
        this.pictureUrl = pictureUrl;
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) &&
                Objects.equals(type, task.type) &&
                Objects.equals(level, task.level) &&
                Objects.equals(text, task.text) &&
                Objects.equals(subject, task.subject) &&
                Objects.equals(pictureUrl, task.pictureUrl) &&
                Objects.equals(answer, task.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, level, text, subject, pictureUrl, answer);
    }
}
