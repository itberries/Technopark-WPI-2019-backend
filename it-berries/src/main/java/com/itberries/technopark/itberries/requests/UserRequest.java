package com.itberries.technopark.itberries.requests;

public class UserRequest {

    private Long id;

    public UserRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRequest() {
    }
}
