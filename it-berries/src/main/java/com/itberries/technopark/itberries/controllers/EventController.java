package com.itberries.technopark.itberries.controllers;


import com.itberries.technopark.itberries.responses.models.EventResponse;
import com.itberries.technopark.itberries.services.IEventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Контроллер для работы с событиями MAIL.RU")
@RequestMapping("/api/")
public class EventController {

    private IEventService iEventService;

    @Autowired
    public EventController(IEventService iEventService) {
        this.iEventService = iEventService;
    }

    @GetMapping(value = "/events/")
    @ResponseBody
    @ApiOperation(value = "Получить все события MAIL.RU")
    EventResponse findAllEvents() {
        return new EventResponse(iEventService.findAllEvents());
    }
}
