package com.itberries.technopark.itberries.controllers;


import com.itberries.technopark.itberries.models.EventDetailDTO;
import com.itberries.technopark.itberries.responses.models.EventShortResponse;
import com.itberries.technopark.itberries.services.IEventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    EventShortResponse findAllEvents() {
        return new EventShortResponse(iEventService.findAllEvents());
    }

    @GetMapping(value = "/events/{id}/")
    @ResponseBody
    @ApiOperation(value = "Получить детальную информацию о событии")
    EventDetailDTO getDetailEventInfoById(@PathVariable(name = "id") Long id) {
        return iEventService.getEventById(id);
    }
}
