package com.itberries.technopark.itberries.models;

import java.util.Objects;

public class User {
    private Long id;
    private Long score;

    public User() {
    }

    public User(Long id, Long score) {
        this.id = id;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public User(Long id) {
        this.id = id;
        this.score = 0L;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(score, user.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, score);
    }
}
