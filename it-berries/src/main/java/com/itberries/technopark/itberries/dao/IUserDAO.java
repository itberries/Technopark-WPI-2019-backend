package com.itberries.technopark.itberries.dao;

import com.itberries.technopark.itberries.models.User;

import java.util.List;

public interface IUserDAO {
    User getUserById(Long id);
    Integer setUser(Long id);
    void updateScore(Integer value,  Long userId);
    void setScore(Integer value,  Long userId);
    List<User> getLeaderboardForUser(Long userId);
    List<User> getLeaderboardForUserAmongFriends(Long userId, List<Long> friendsIds);
}
