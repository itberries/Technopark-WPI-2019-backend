package com.itberries.technopark.itberries.controllers;

import com.itberries.technopark.itberries.models.Card;
import com.itberries.technopark.itberries.models.CardInteractive;
import com.itberries.technopark.itberries.models.User;
import com.itberries.technopark.itberries.responses.ThereIsNoCardsException;
import com.itberries.technopark.itberries.responses.UserNotAuthorizedException;
import com.itberries.technopark.itberries.responses.models.InteractiveCardListResponse;
import com.itberries.technopark.itberries.services.IMiniGamesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@Api(value = "Контроллер для работы с мини-играми")
@RequestMapping("/api/")
public class MiniGameController {
    private IMiniGamesService iMiniGamesService;

    @Autowired
    public MiniGameController(IMiniGamesService iMiniGamesService) {
        this.iMiniGamesService = iMiniGamesService;
    }

    @GetMapping(value = "/{step_id}/minigames/")
    @ResponseBody
    @ApiOperation(value = "Получить все карточки интерактива для нужного шага по идентификатору")
    ResponseEntity<InteractiveCardListResponse> getInteractiveCardsByStepId(@PathVariable(name = "step_id") Long stepId,
                                                                            HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            throw new UserNotAuthorizedException();
        }

        List<CardInteractive> stepCards = null;
        stepCards = iMiniGamesService.getCardsByStepId(stepId);
        if (stepCards == null) {
            throw new ThereIsNoCardsException();
        }
        List<Long> gamesIdByStepId = iMiniGamesService.getGamesIdByStepId(stepId);

        String gameTypeByGameId = iMiniGamesService.getGameTypeByGameId(gamesIdByStepId.get(0));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new InteractiveCardListResponse(gameTypeByGameId, stepCards));
    }
}
