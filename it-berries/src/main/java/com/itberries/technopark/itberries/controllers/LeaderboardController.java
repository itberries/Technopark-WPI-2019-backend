package com.itberries.technopark.itberries.controllers;

import com.itberries.technopark.itberries.models.User;
import com.itberries.technopark.itberries.responses.UserNotAuthorizedException;
import com.itberries.technopark.itberries.services.ILeaderboardService;
import com.itberries.technopark.itberries.services.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@Api(value = "Контроллер для работы с данными таблицы лидеров")
@RequestMapping("/api/")
public class LeaderboardController {

    private static final Logger logger = LoggerFactory.getLogger(LeaderboardController.class);
    final IUserService iUserService;
    final ILeaderboardService iLeaderboardService;

    @Autowired
    public LeaderboardController(IUserService iUserService, ILeaderboardService iLeaderboardService) {
        this.iUserService = iUserService;
        this.iLeaderboardService = iLeaderboardService;
    }

    @ApiOperation(value = "Получает таблицу лидеров (10 человек) для заданного пользователя с его текущей позицией в рейтинге")
    @GetMapping(value = "/leaderboard/{user_id}/")
    @ResponseBody
    List<User> getLeaderBoardForUser(@PathVariable(name = "user_id") Long userId, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            throw new UserNotAuthorizedException();
        } else {
            return iLeaderboardService.getLeaderBoardForUser(userId);
        }
    }
}
