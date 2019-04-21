package com.itberries.technopark.itberries.controllers;


import com.itberries.technopark.itberries.models.Reward;
import com.itberries.technopark.itberries.models.User;
import com.itberries.technopark.itberries.responses.UserNotAuthorizedException;
import com.itberries.technopark.itberries.services.IRewardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@Api(value = "Контроллер для работы с достижениями пользователя")
@RequestMapping("/api/rewards/")
public class RewardController {

    private IRewardService iRewardService;

    @Autowired
    public RewardController(IRewardService iRewardService) {
        this.iRewardService = iRewardService;
    }

    @GetMapping(value = "/")
    @ResponseBody
    @ApiOperation(value = "Получить список всех ачивок")
    public List<Reward> getRewards(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            throw new UserNotAuthorizedException();
        }
        return iRewardService.getRewards();
    }
}
