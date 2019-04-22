package com.itberries.technopark.itberries.controllers;

import com.itberries.technopark.itberries.models.User;
import com.itberries.technopark.itberries.models.UserState;
import com.itberries.technopark.itberries.requests.UserRequest;
import com.itberries.technopark.itberries.responses.CantUpdateStateException;
import com.itberries.technopark.itberries.responses.UserNotAuthorizedException;
import com.itberries.technopark.itberries.responses.models.UserStateResponse;
import com.itberries.technopark.itberries.services.IRewardService;
import com.itberries.technopark.itberries.services.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@Api(value = "Контроллер для работы с данными пользователя")
@RequestMapping("/api/")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    final IUserService iUserService;
    final IRewardService iRewardService;

    @Autowired
    public UserController(IUserService iUserService, IRewardService iRewardService) {
        this.iUserService = iUserService;
        this.iRewardService = iRewardService;
    }


    @ApiOperation(value = "Сохраняет данные о пользователе (id,score)")
    @PostMapping(value = "/user/")
    ResponseEntity<UserStateResponse> setUser(@RequestBody UserRequest user, HttpSession httpSession) {
        logger.info(String.format("user with id = %s for saving", user.getId()));
        iUserService.setUser(user.getId());
        iUserService.initUserState(user.getId());
        UserState currentUserState = iUserService.getCurrentUserState(user.getId());
        logger.info("setUser completed");
        User user1 = new User(user.getId(), 0L);
        httpSession.setAttribute("user", user1);
        List<Long> rewardsByUserId = iRewardService.getRewardsByUserId(user1.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new UserStateResponse(user1, currentUserState, rewardsByUserId));
    }

    @ApiOperation(value = "Получает данные о пользователе (id,score)")
    @GetMapping(value = "/user/{id}/")
    @ResponseBody
    UserStateResponse getUserById(@PathVariable(name = "id") Long id, HttpSession httpSession) {
        logger.info(String.format("user with id = %s for getting", id));
        User userById = iUserService.getUserById(id);
        UserState currentUserState = iUserService.getCurrentUserState(id);
        List<Long> rewardsByUserId = iRewardService.getRewardsByUserId(userById.getId());
        httpSession.setAttribute("user", userById);
        return new UserStateResponse(userById, currentUserState, rewardsByUserId);
    }

    @ApiOperation(value = "Получает текущее состояние пользователя (секция, подсекция, шаг)")
    @GetMapping(value = "/user/{id}/get_current_state/")
    @ResponseBody
    UserState getCurrentUserState(@PathVariable(name = "id") Long id, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            throw new UserNotAuthorizedException();
        } else {
            return iUserService.getCurrentUserState(id);
        }
    }

    @ApiOperation(value = "Сбрасывает текущее состояние пользователя (секция, подсекция, шаг)")
    @PatchMapping(value = "/user/{id}/reset_current_state/")
    UserState resetCurrentUserState(@PathVariable(name = "id") Long id, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            throw new UserNotAuthorizedException();
        } else {
            boolean updateResult = iUserService.resetCurrentUserState(id);
            if (updateResult) {
                return iUserService.getCurrentUserState(id);
            } else {
                throw new CantUpdateStateException();
            }
        }
    }

    @ApiOperation(value = "Производит перевод пользователя на другую секцию - секцию-ребенка")
    @PatchMapping(value = "/user/{user_id}/sections/{section_id}/")
    UserState setCurrentUserSectionCompleted(@PathVariable(name = "user_id") Long userId,
                                             @PathVariable(name = "section_id") Long sectionId,
                                             HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            throw new UserNotAuthorizedException();
        } else {
            boolean updateResult = iUserService.setCurrentUserSectionCompleted(userId, sectionId);
            if (updateResult) {
                return iUserService.getCurrentUserState(userId);
            } else {
                throw new CantUpdateStateException();
            }

        }
    }

    @ApiOperation(value = "Производит перевод пользователя на другую подсекцию - подсекцию-ребенка")
    @PatchMapping(value = "/user/{user_id}/sections/{section_id}/subsections/{subsection_id}/")
    UserState setCurrentUserSubsectionCompleted(@PathVariable(name = "user_id") Long userId,
                                                @PathVariable(name = "section_id") Long sectionId,
                                                @PathVariable(name = "subsection_id") Long subsectionId,
                                                HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            throw new UserNotAuthorizedException();
        } else {
            boolean updateResult = iUserService.setCurrentUserSubsectionCompleted(userId, sectionId, subsectionId);
            if (updateResult) {
                return iUserService.getCurrentUserState(userId);
            } else {
                throw new CantUpdateStateException();
            }

        }
    }

    @ApiOperation(value = "Производит перевод пользователя на другой шаг подсекции - шаг-ребенка")
    @PatchMapping(value = "/user/{user_id}/sections/{section_id}/subsections/{subsection_id}/steps/{step_id}/")
    UserState setCurrentUserStepCompleted(@PathVariable(name = "user_id") Long userId,
                                          @PathVariable(name = "section_id") Long sectionId,
                                          @PathVariable(name = "subsection_id") Long subsectionId,
                                          @PathVariable(name = "step_id") Long stepId,
                                          HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            throw new UserNotAuthorizedException();
        } else {
            boolean updateResult = iUserService.setCurrentUserStepCompleted(userId, sectionId, subsectionId, stepId);
            if (updateResult) {
                return iUserService.getCurrentUserState(userId);
            } else {
                throw new CantUpdateStateException();
            }
        }
    }
}
