package com.itberries.technopark.itberries.services;

import com.itberries.technopark.itberries.models.Event;
import com.itberries.technopark.itberries.models.EventDetailDTO;
import com.itberries.technopark.itberries.models.EventShortDTO;

import java.util.List;

public interface IEventService {
    List<EventShortDTO> findAllEvents();
    EventDetailDTO getEventById(Long id);
}
