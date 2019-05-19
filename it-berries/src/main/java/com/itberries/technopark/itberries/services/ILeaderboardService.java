package com.itberries.technopark.itberries.services;

import com.itberries.technopark.itberries.models.User;

import java.util.List;

public interface ILeaderboardService {
    List<User> getLeaderBoardForUser(Long userId);
    List<User> getLeaderBoardForUserAmongFriends(Long userId, List<Long> friendsIds);
}
