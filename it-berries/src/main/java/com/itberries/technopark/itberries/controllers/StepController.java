package com.itberries.technopark.itberries.controllers;

import com.itberries.technopark.itberries.models.Card;
import com.itberries.technopark.itberries.models.User;
import com.itberries.technopark.itberries.responses.ThereIsNoCardsException;
import com.itberries.technopark.itberries.responses.UserNotAuthorizedException;
import com.itberries.technopark.itberries.services.IStepService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/steps/")
public class StepController {
    final IStepService iStepService;

    public StepController(IStepService iStepService) {
        this.iStepService = iStepService;
    }

    @GetMapping(value = "/{id}/cards/")
    @ResponseBody
    @ApiOperation(value = "Получить все карточки для нужного шага по идентификатору")
    List<Card> getCardsByStepId(@PathVariable(name = "id") Integer id, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            throw new UserNotAuthorizedException();
        }

        List<Card> stepCards = null;
        stepCards = iStepService.getCardsByStepId(id);
        if (stepCards == null) {
            throw new ThereIsNoCardsException();
        }

        return stepCards;
    }
}
