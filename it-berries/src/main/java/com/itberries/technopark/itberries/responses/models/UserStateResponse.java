package com.itberries.technopark.itberries.responses.models;

import com.itberries.technopark.itberries.models.User;
import com.itberries.technopark.itberries.models.UserState;

import java.util.List;

public class UserStateResponse {
    private User user;
    private UserState userState;
    private List<Long> rewards;
    public UserStateResponse(User user, UserState userState, List<Long> rewards) {
        this.user = user;
        this.userState = userState;
        this.rewards = rewards;
    }

    public List<Long> getRewards() {
        return rewards;
    }

    public void setRewards(List<Long> rewards) {
        this.rewards = rewards;
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
