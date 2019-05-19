package com.itberries.technopark.itberries.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.itberries.technopark.itberries.models.User;
import com.itberries.technopark.itberries.models.VKUser;
import com.itberries.technopark.itberries.models.VKUserList;
import com.itberries.technopark.itberries.responses.ThereIsNoSuchUserException;
import com.itberries.technopark.itberries.responses.UserNotAuthorizedException;
import com.itberries.technopark.itberries.services.ILeaderboardService;
import com.itberries.technopark.itberries.services.IUserService;
import com.itberries.technopark.itberries.utils.ParameterStringBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@RestController
@Api(value = "Контроллер для работы с данными таблицы лидеров")
@RequestMapping("/api/")
public class LeaderboardController {

    private static final Logger logger = LoggerFactory.getLogger(LeaderboardController.class);
    final IUserService iUserService;
    final ILeaderboardService iLeaderboardService;

    @Value("${application.vk.api.access_token}")
    private String vkApiAccessToken;

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

    @ApiOperation(value = "Получает таблицу лидеров (10 человек) для заданного пользователя среди друзей с его текущей позицией в рейтинге")
    @GetMapping(value = "/leaderboard/{user_id}/friends/")
    @ResponseBody
    List<User> getLeaderBoardForUserAmongFriends(@PathVariable(name = "user_id") Long userId, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            throw new UserNotAuthorizedException();
        } else {
            try {
                URL url = new URL("https://api.vk.com/method/friends.get?user_id=" + userId.toString()
                        + "&fields=id&access_token=" + vkApiAccessToken + "&v=5.95");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json");

                con.setDoOutput(true);

                int status = con.getResponseCode();

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                con.disconnect();

                ObjectMapper mapper = new ObjectMapper();
                Map<String,Object> map = mapper.readValue(content.toString(), Map.class);
                VKUserList vkUserList = mapper.convertValue(map.get("response"), VKUserList.class);
                ArrayList<Long> vkUserIds = new ArrayList<>();
                for (VKUser vkUser : vkUserList.getItems()) {
                    vkUserIds.add(Long.parseLong(vkUser.getId()));
                }


                return iLeaderboardService.getLeaderBoardForUserAmongFriends(userId,vkUserIds);
            } catch (Exception ex) {
                throw new ThereIsNoSuchUserException();
            }
        }
    }
}
