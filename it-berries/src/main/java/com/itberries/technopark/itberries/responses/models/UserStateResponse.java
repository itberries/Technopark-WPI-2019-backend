package com.itberries.technopark.itberries.responses.models;

import com.itberries.technopark.itberries.models.User;
import com.itberries.technopark.itberries.models.UserState;

public class UserStateResponse {
    private User user;
    private UserState userState;

    public UserStateResponse(User user, UserState userState) {
        this.user = user;
        this.userState = userState;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }
}
