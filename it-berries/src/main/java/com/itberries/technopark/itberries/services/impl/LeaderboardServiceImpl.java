package com.itberries.technopark.itberries.services.impl;


import com.itberries.technopark.itberries.dao.IUserDAO;
import com.itberries.technopark.itberries.models.User;
import com.itberries.technopark.itberries.services.ILeaderboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class LeaderboardServiceImpl  implements ILeaderboardService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    final IUserDAO userDAO;

    public LeaderboardServiceImpl(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public List<User> getLeaderBoardForUser(Long userId) {
        return userDAO.getLeaderboardForUser(userId);
    }
}
